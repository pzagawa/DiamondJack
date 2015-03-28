package pl.pzagawa.diamond.jack.activities;

import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.ui.CommonTabActivity;
import pl.pzagawa.gae.client.RequestGetMobileProfile;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ProfileActivity
	extends CommonTabActivity
{
	private ProfileActivityTabStats tabStats;
	private ProfileActivityTabAccount tabAccount;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        tabStats = new ProfileActivityTabStats(this);
        tabAccount = new ProfileActivityTabAccount(this);

        tabStats.setCurrentTab();
    }
    
	@Override
	protected void onLoadData(final int tabId)
	{
		if (tabAccount.isTabEqual(tabId))
			tabAccount.onLoadData();
		
		if (tabStats.isTabEqual(tabId))
			tabStats.onLoadData();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    MenuInflater inflater = getMenuInflater();
	    
	    inflater.inflate(R.menu.profile_menu, menu);
	    
		return true;
	}

	private boolean enableStatsUpdating()
	{
		if (getCurrentTab() == tabStats.getTabIndex())
			if (MainApplication.getUserProfile().accessRights.canSyncStats())
				return true;
		
		return false;
	}

	private boolean enableAccountUpdating()
	{
		if (getCurrentTab() == tabAccount.getTabIndex())			
			return true;
		
		return false;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		final MenuItem miUpdateProfile = menu.findItem(R.id.update_profile_id);
		
		miUpdateProfile.setVisible(false);
		miUpdateProfile.setEnabled(false);
		
		if (enableStatsUpdating())
		{
			miUpdateProfile.setVisible(true);
			miUpdateProfile.setEnabled(true);
		}
		
		if (enableAccountUpdating())
		{
			miUpdateProfile.setVisible(true);
			miUpdateProfile.setEnabled(true);			
		}
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
	    {
	    case R.id.update_profile_id:
	    	
	    	if (enableStatsUpdating())
	    		UpdaterActivity.open(ProfileActivity.this, RequestGetMobileProfile.LEVELS_RETURN_NONE);
	    	
	    	if (enableAccountUpdating())
	    		UpdaterActivity.open(ProfileActivity.this, RequestGetMobileProfile.LEVELS_RETURN_NONE);
	    	
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
}
