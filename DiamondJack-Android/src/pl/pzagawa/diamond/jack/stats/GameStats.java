package pl.pzagawa.diamond.jack.stats;

import java.sql.SQLException;
import java.util.List;
import android.app.Activity;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.Utils;
import pl.pzagawa.diamond.jack.database.collections.LevelStatsItem;
import pl.pzagawa.diamond.jack.ui.OperationWithProgress;

public abstract class GameStats
{
	private Activity activity;
	private String dialogTitle;
	private String dialogText;		
	
	private int score;
	private int completedLevelsCount;
	private int completionCount;
	private int deadCount;
	private long totalPlayTimeMs;

	public GameStats(Activity activity)
	{
		this.activity = activity;
		
		dialogTitle = activity.getString(R.string.dialog_please_wait_title);
		dialogText = activity.getString(R.string.dialog_processing_data_text);		
	}

	private void reset()
	{
		score = 0;
		completedLevelsCount = 0;
		completionCount = 0;
		deadCount = 0;
		totalPlayTimeMs = 0;
	}
	
	public void process()
	{
		reset();

		OperationWithProgress operationLoadData = new OperationWithProgress(dialogTitle, dialogText)
		{
			@Override
			protected void runWorkerThread()
				throws SQLException
			{
				LevelStatsItem newItem = new LevelStatsItem();
				
				List<LevelStatsItem> list = MainApplication.getData().levelStats.getList();
				
				for (LevelStatsItem item : list)
				{
					newItem.aggregate(item);

					if (item.getCompletedCount() > 0)
						completedLevelsCount++;				
				}
				
				score = newItem.getScore();
				completionCount = newItem.getCompletedCount();
				deadCount = newItem.getDeadCount();					
				totalPlayTimeMs = newItem.getTotalPlayTimeMs();				
			}

			@Override
			protected void runFinishUiThread()
			{
				onStatsReady();
			}
		};
		
		operationLoadData.run(activity);
	}

	public String getScoreText()
	{
		return Integer.toString(score);		
	}

	public String getCompletedLevelsCount()
	{
		return Integer.toString(completedLevelsCount);		
	}

	public String getCompletionCount()
	{
		return Integer.toString(completionCount);		
	}
	
	public String getDeadCount()
	{
		return Integer.toString(deadCount);		
	}

	public String getTotalPlayTime()
	{
		return Utils.formatTimeAsText((int)(totalPlayTimeMs / 1000));
	}
	
	public abstract void onStatsReady();
	
}
