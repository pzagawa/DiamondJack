package pl.pzagawa.diamond.jack.activities;

import java.sql.SQLException;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.database.collections.LevelDataItem;
import pl.pzagawa.diamond.jack.database.collections.LevelStatsItem;
import pl.pzagawa.diamond.jack.ui.ActivityStarter;
import pl.pzagawa.diamond.jack.ui.CommonActivity;
import pl.pzagawa.diamond.jack.ui.OperationWithProgress;
import pl.pzagawa.game.engine.EngineException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayActivity
	extends CommonActivity
{
	private LevelDataItem levelItem;
	
	private LinearLayout llayStats;
	private LinearLayout llayPlayRight;
	private LinearLayout llayPlayBottom;
	
	private TextView labelName;
	private TextView labelDescription;
	private TextView labelAuthor;
	private TextView labelScore;	
	private TextView labelStats;
	
	private Button btnPlayRight;
	private Button btnPlayBottom;

	private String textStatsScore;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_play);
        
        llayStats = (LinearLayout)this.findViewById(R.id.llayStats);
        llayPlayRight = (LinearLayout)this.findViewById(R.id.llayPlayRight);
        llayPlayBottom = (LinearLayout)this.findViewById(R.id.llayPlayBottom);
        
        labelName = (TextView)this.findViewById(R.id.labelName);
        labelDescription = (TextView)this.findViewById(R.id.labelDescription);
        labelAuthor = (TextView)this.findViewById(R.id.labelAuthor);
        labelScore = (TextView)this.findViewById(R.id.labelScore);
        labelStats = (TextView)this.findViewById(R.id.labelStats);
        btnPlayBottom = (Button)this.findViewById(R.id.btnPlayBottom);
        btnPlayBottom.setOnClickListener(onButtonPlayClick);
        btnPlayRight = (Button)this.findViewById(R.id.btnPlayRight);
        btnPlayRight.setOnClickListener(onButtonPlayClick);

    	textStatsScore = getString(R.string.statsScore);
    }

	@Override
	protected void onStart()
	{
		super.onStart();

		long levelId = starter.getLevelId();
		if (levelId != -1)
		{
			try
			{
				levelItem = MainApplication.getData().levelData.getItemById(levelId);
				
			} catch (SQLException e)
			{
				throw new EngineException("PlayActivity. Error loading LevelDataItem: " + Long.toString(levelId));
			}
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();

		updatePlayButtonLayout();
		
		if (levelItem != null)
		{
			updateLevelDescription();
			updateLevelStats();
		}
	}	
	
	private void updatePlayButtonLayout()
	{
		if (isLandScape())
		{
			llayPlayBottom.setVisibility(View.GONE);
			llayPlayRight.setVisibility(View.VISIBLE);
		} else {
			llayPlayBottom.setVisibility(View.VISIBLE);
			llayPlayRight.setVisibility(View.GONE);
		}
	}

	private void updateLevelDescription()
	{
		labelName.setText(levelItem.getName());

		labelDescription.setVisibility(View.VISIBLE);
		labelDescription.setText(levelItem.getDescription());
		
		labelAuthor.setText(levelItem.getAuthor());
	}
	
	private void updateLevelStats()
	{
		llayStats.setVisibility(View.GONE);
		
		labelScore.setVisibility(View.GONE);
		labelStats.setVisibility(View.GONE);
		
		try
		{
			LevelStatsItem statsItem = MainApplication.getData().levelStats.getItemById(levelItem.getLevelId());

			if (!statsItem.isNew())
			{
				llayStats.setVisibility(View.VISIBLE);
				
				labelScore.setText(textStatsScore + " " + Integer.toString(statsItem.getScore()));				
				labelScore.setVisibility(View.VISIBLE);
				
				labelStats.setText(LevelStatsItem.getStatsSummaryText(this, statsItem));				
				labelStats.setVisibility(View.VISIBLE);				
			}			
		}
		catch (SQLException e)
		{
		}
	}
	
	private View.OnClickListener onButtonPlayClick = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (levelItem != null)
			{
				startGame();
			}
		}
	};
	
	private void startGame()
	{
		MainApplication.getSettings().setSelectedLevelId(levelItem.getLevelId());
		
		String dialogTitle = getString(R.string.dialog_please_wait_title);
		String dialogText = getString(R.string.dialog_starting_game_text);
	
		OperationWithProgress operationStartGame = new OperationWithProgress(dialogTitle, dialogText)
		{
			@Override
			protected void runWorkerThread()
			{
				starter.openGameActivityForResult(starter.getLevelId());
			}
			@Override
			protected void runFinishUiThread()
			{
				PlayActivity.this.finish();
			}
		};

		operationStartGame.run(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == ActivityStarter.REQUEST_GAME_ACTIVITY)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				//reload level data after completion
				updateLevelStats();
			}
			
			if (resultCode == Activity.RESULT_CANCELED)
			{
				PlayActivity.this.finish();
			}
		}
	}
	
}
