package pl.pzagawa.gae.auth;

import java.util.Observable;
import java.util.Observer;
import pl.pzagawa.events.Event;
import pl.pzagawa.events.EventId;
import pl.pzagawa.events.GameUpdateOperation;
import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;

public class AuthManager
	extends Observable
	implements Observer
{
	private final Activity activity;
	
	private TokenRequest tokenRequest;	
	private CookieRequest cookieRequest;

	public AuthManager(Activity activity)
	{
		this.activity = activity;
	    this.tokenRequest = new TokenRequest(activity);
	    this.cookieRequest = new CookieRequest();
	}
	
	public void clearAccountName()
	{
		PreferencesUtils.clearDefaultAccountName(activity);
	}
	
	public void selectAccountName(String accountName)
	{
		PreferencesUtils.setDefaultAccountName(activity, accountName);
	}
	
	public String getSelectedAccountName()
	{
		return PreferencesUtils.getSelectedAccountName(activity);
	}

	public void showAccountSelector(final String[] accountsNames)
	{
	    SelectorDialog selectorDialog = new SelectorDialog(activity, "Select account", accountsNames, -1)
	    {
			@Override
			public void onClickButtonOK(int selectedItem)
			{
				if (selectedItem == -1)
					return;
				
				selectAccountName(accountsNames[selectedItem]);				
				process();
			}
	    };

	    selectorDialog.show();
	}

	private Account getAccount()
	{
		//check if any accounts configured
		if (tokenRequest.noAccountsConfigured())
		{
			this.setChanged();
			this.notifyObservers(new Event(EventId.NO_GOOGLE_ACCOUNTS_AVAILABLE));
			return null;
		}
		
		//check if any account name is already saved
		if (getSelectedAccountName() != null)
		{
			//if account no longer exists in system, clear its stored name   
			if (tokenRequest.getAccountByName(getSelectedAccountName()) == null)
				clearAccountName();
		}
		
		//get available account
		Account account = null; 
				
		if (tokenRequest.isOneAccount())
		{
			account = tokenRequest.getFirstAccount();
			
		} else {
			if (getSelectedAccountName() == null)
			{
				showAccountSelector(tokenRequest.getAccountsList());				
				
				this.setChanged();
				this.notifyObservers(new Event(EventId.SELECT_ACCOUNT));
				
			} else {
				//get account by saved name
				account = tokenRequest.getAccountByName(getSelectedAccountName());				
			}
		}
		
		return account;
	}
	
	public void process()
	{
		final Account account = getAccount(); 
		
		if (account == null)
			return;
		
		GameUpdateOperation owp = new GameUpdateOperation(EventId.AUTHORIZATION)
		{
			@Override
			protected void runWorkerThread()
			{
				tokenRequest.process(account);

				final String authToken = tokenRequest.getAuthToken();
				
				PreferencesUtils.setToken(activity, authToken);
				
				if (tokenRequest.isSuccess())
				{
					cookieRequest.process(authToken);
					
					PreferencesUtils.setCookie(activity, cookieRequest.getAuthCookie());
				}	
			}

			@Override
			protected boolean runFinishUiThread()
			{
				if (tokenRequest.isSuccess())
				{
					if (cookieRequest.isSuccess())
					{
						AuthManager.this.setChanged();
						AuthManager.this.notifyObservers(new Event(EventId.AUTHORIZATION_READY));						
						return true;
					}
				}
			
				Intent userInput = tokenRequest.getRequiredUserInput();
				if (userInput == null)
				{
					String description = "";
					
					if (tokenRequest.getErrorMessage() != null)
						description += "TokenRequest: " + tokenRequest.getErrorMessage();
					if (cookieRequest.getErrorMessage() != null)
						description += "CookieRequest: " + cookieRequest.getErrorMessage();

					AuthManager.this.setChanged();
					AuthManager.this.notifyObservers(new Event(EventId.AUTHORIZATION_ERROR, description));					
				}
				else
				{
					AuthManager.this.setChanged();
					AuthManager.this.notifyObservers(new Event(EventId.REQUIRED_USER_INPUT, userInput));
				}
				
				return false;
			}
		};
		
		owp.addObserver(this);
		owp.run();
	}

	public String getAuthToken()
	{
		return PreferencesUtils.getToken(activity);
	}

	public String getAuthCookie()
	{
		return PreferencesUtils.getCookie(activity);
	}
	
	public void invalidateToken()
	{
		final String authToken = getAuthToken();
		
		tokenRequest.invalidateToken(authToken);

	    PreferencesUtils.setToken(activity, null);
	    PreferencesUtils.setCookie(activity, null);
	}

	@Override
	public void update(Observable src, Object data)
	{
		Event event = (Event)data;
		
		//dispatch locally observed events up
		this.setChanged();
		this.notifyObservers(event);			
	}
	
}
