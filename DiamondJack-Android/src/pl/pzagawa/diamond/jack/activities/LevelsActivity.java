package pl.pzagawa.diamond.jack.activities;

import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.ui.CommonTabActivity;
import pl.pzagawa.gae.client.RequestGetMobileProfile;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

public class LevelsActivity
	extends CommonTabActivity
{
	private LevelsActivityTabPublic tabPublic;
	private LevelsActivityTabPrivate tabPrivate;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_levels);

		tabPublic = new LevelsActivityTabPublic(this, levelsListOnItemClick);
		tabPrivate = new LevelsActivityTabPrivate(this, levelsListOnItemClick);

		tabPublic.setCurrentTab();
	}

	@Override
	protected void onLoadData(final int tabId)
	{
		if (tabPublic.isTabEqual(tabId))
			tabPublic.onLoadData();

		if (tabPrivate.isTabEqual(tabId))
			tabPrivate.onLoadData();
	}
		
	private AdapterView.OnItemClickListener levelsListOnItemClick = new AdapterView.OnItemClickListener()
	{
		@SuppressWarnings("rawtypes")
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id)
		{
			selectCurrentLevel(id);
		}
	};
	
	private void selectCurrentLevel(long levelId)
	{
		starter.openPlayActivity(levelId);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    MenuInflater inflater = getMenuInflater();
	    
	    inflater.inflate(R.menu.levels_menu, menu);
	    
		return true;
	}

	private boolean enableDownloadingPublicLevels()
	{
		if (getCurrentTab() == tabPublic.getTabIndex())
			if (MainApplication.getUserProfile().accessRights.canDownloadPublicLevels())
				return true;
		
		return false;
	}

	private boolean enableDownloadingPrivateLevels()
	{
		if (getCurrentTab() == tabPrivate.getTabIndex())
			if (MainApplication.getUserProfile().accessRights.canDownloadPrivateLevels())
				return true;
		
		return false;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		final MenuItem miUpdateLevels = menu.findItem(R.id.update_levels_id);
		
		miUpdateLevels.setVisible(false);
		miUpdateLevels.setEnabled(false);

		if (enableDownloadingPublicLevels())
		{
			miUpdateLevels.setVisible(true);
			miUpdateLevels.setEnabled(true);
		}
		
		if (enableDownloadingPrivateLevels())
		{
			miUpdateLevels.setVisible(true);
			miUpdateLevels.setEnabled(true);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
	    {
	    case R.id.update_levels_id:
	    	
			if (enableDownloadingPublicLevels())
				UpdaterActivity.open(LevelsActivity.this, RequestGetMobileProfile.LEVELS_RETURN_PUBLIC);
			
			if (enableDownloadingPrivateLevels())
				UpdaterActivity.open(LevelsActivity.this, RequestGetMobileProfile.LEVELS_RETURN_PRIVATE);

	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
}
