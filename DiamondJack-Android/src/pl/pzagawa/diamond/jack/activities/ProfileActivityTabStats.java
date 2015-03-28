package pl.pzagawa.diamond.jack.activities;

import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.stats.GameStats;
import pl.pzagawa.diamond.jack.ui.CommonTab;
import pl.pzagawa.diamond.jack.ui.CommonTabActivity;
import android.view.View;
import android.widget.TextView;

public class ProfileActivityTabStats
	extends CommonTab
{
	private TextView textScore;
	private TextView textCompletedLevels;
	private TextView textCompletionCount;
	private TextView textDeads;
	private TextView textPlayTime;	

	private TextView labelTipForAccountUpgrade;	
	
	public ProfileActivityTabStats(CommonTabActivity parent)
	{
		super(parent);
		
    	textScore = (TextView)parent.findViewById(R.id.textScore);
    	textCompletedLevels = (TextView)parent.findViewById(R.id.textCompletedLevels);
    	textCompletionCount = (TextView)parent.findViewById(R.id.textCompletionCount);
    	textDeads = (TextView)parent.findViewById(R.id.textDeads);
    	textPlayTime = (TextView)parent.findViewById(R.id.textPlayTime);

    	labelTipForAccountUpgrade = (TextView)parent.findViewById(R.id.labelTipForAccountUpgrade);
	}
	
	@Override
	protected int getTabIndex()
	{
		return 0;
	}
	
	@Override
	protected int getTabTextResId()
	{
		return R.string.tab_stats;
	}

	@Override
	protected int getTabIconResId()
	{
		return R.drawable.tab_stats;
	}

	@Override
	protected int getTabContentResId()
	{
		return R.id.contentTab1;
	}	
	
	@Override
	public void onLoadData()
	{
		//init labels
    	textScore.setText("...");
    	textCompletedLevels.setText("...");
    	textCompletionCount.setText("...");
    	textDeads.setText("...");
    	textPlayTime.setText("...");
    	    	
		//update account upgrade tip visibility
    	labelTipForAccountUpgrade.setVisibility(View.VISIBLE);
		if (MainApplication.isUserProfileOnline())
			if (MainApplication.getUserProfile().accessRights.canSyncStats())		
				labelTipForAccountUpgrade.setVisibility(View.GONE);
    	
    	//process gameplay stats
		GameStats gameStats = new GameStats(parent)
		{
			@Override
			public void onStatsReady()
			{
		    	textScore.setText(this.getScoreText());
		    	textCompletedLevels.setText(this.getCompletedLevelsCount());
		    	textCompletionCount.setText(this.getCompletionCount());
		    	textDeads.setText(this.getDeadCount());
		    	textPlayTime.setText(this.getTotalPlayTime());				
			}
		};	
		
		gameStats.process();		
	}
	
}
