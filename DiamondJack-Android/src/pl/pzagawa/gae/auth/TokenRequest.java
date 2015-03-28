package pl.pzagawa.gae.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class TokenRequest
{
	private final static String ACCOUNT_TYPE = "com.google";
		
	private final Activity activity;
	private final AccountManager am;
	private final Account[] accounts;
	private final List<String> accountsList;

	private String authToken;
	private Intent requiredUserInput;
	private String errorMessage;		
	
	public TokenRequest(Activity activity)
	{
		this.activity = activity; 		
		this.am = AccountManager.get(activity);		
		this.accounts = am.getAccountsByType(ACCOUNT_TYPE);		
		this.accountsList = createAccountsList();
	}

	public boolean isOneAccount()
	{
		return (accounts.length == 1);
	}
	
	public Account getFirstAccount()
	{
		return accounts[0];
	}
	
	public Account getAccountByName(String accountName)
	{
		for (Account account : getAccounts())
			if (account.name.compareToIgnoreCase(accountName) == 0)
				return account;
		
		return null;
	}	
	
	private Account[] getAccounts()
	{
		return accounts;
	}

	public boolean noAccountsConfigured()
	{
		return (accounts.length == 0);
	}
	
	private List<String> createAccountsList()
	{
		List<String> list = new ArrayList<String>();
	
		for (Account account : getAccounts())
			list.add(account.name);
		
		return list;		
	}
	
	public String[] getAccountsList()
	{
		return accountsList.toArray(new String[accountsList.size()]);
	}
		
	public String getAccountName()
	{
		return PreferencesUtils.getSelectedAccountName(activity);
	}
		
	public void process(Account account)
	{
		authToken = null;
		requiredUserInput = null;
		errorMessage = null;
		
		final AccountManagerFuture<Bundle> data = am.getAuthToken(account, "ah", null, activity, null, null);

		try
		{
			Bundle authData = data.getResult();
			
			if (authData.containsKey(AccountManager.KEY_INTENT))
			{
				requiredUserInput = (Intent)authData.get(AccountManager.KEY_INTENT);
                return;
			}

			authToken = authData.get(AccountManager.KEY_AUTHTOKEN).toString();
			
		} catch (OperationCanceledException e)
		{
        	errorMessage = e.getMessage();				
        	e.printStackTrace();				
		} catch (AuthenticatorException e)
		{
        	errorMessage = e.getMessage();				
        	e.printStackTrace();				
		} catch (IOException e)
		{
        	errorMessage = e.getMessage();				
        	e.printStackTrace();				
		}
	}
	
	public boolean isSuccess()
	{
		return (authToken != null);
	}
	
	public String getAuthToken()
	{
		return authToken;
	}

	public Intent getRequiredUserInput()
	{
		return requiredUserInput;
	}
	
	//call when you get response STATUS CODE = 401 (SC_UNAUTHORIZED)
	public void invalidateToken(String authToken)
	{
		if (authToken == null)
			return;
		
		am.invalidateAuthToken(ACCOUNT_TYPE, authToken);
	}
	
	public String getErrorMessage()
	{
		return errorMessage;
	}

}
