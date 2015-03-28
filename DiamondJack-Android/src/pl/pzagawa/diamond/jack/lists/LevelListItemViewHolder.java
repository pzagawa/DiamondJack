package pl.pzagawa.diamond.jack.lists;

import java.sql.SQLException;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.database.collections.LevelDataItem;
import pl.pzagawa.diamond.jack.database.collections.LevelStatsItem;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LevelListItemViewHolder
	extends LevelsListAdapter.ItemViewHolder
{
	private TextView name;
	private TextView score;
	
	private TextView textCompleted;
	private TextView textDeads;
	private TextView textTime;
	
	private ImageView statusIconNew;
	private ImageView statusIconCompleted;
	private ImageView statusIconSkull;
	private ImageView statusIconTime;
	
	private ImageView statusIconSelected;
	
	private String textNoStats;
	private String textScore;
	
	public LevelListItemViewHolder(LayoutInflater inflater, int itemResourceId)
	{
		super(inflater, itemResourceId);
	}

	@Override
	public void initializeViews(View convertView)
	{
		name = (TextView)convertView.findViewById(R.id.textName);
		score = (TextView)convertView.findViewById(R.id.textScore);
		
		textCompleted = (TextView)convertView.findViewById(R.id.textCompleted);
		textDeads = (TextView)convertView.findViewById(R.id.textDeads);
		textTime = (TextView)convertView.findViewById(R.id.textTime);

		statusIconSelected = (ImageView)convertView.findViewById(R.id.statusIconSelected);
		
		statusIconNew = (ImageView)convertView.findViewById(R.id.statusIconNew);
		statusIconCompleted = (ImageView)convertView.findViewById(R.id.statusIconCompleted);
		statusIconSkull = (ImageView)convertView.findViewById(R.id.statusIconSkull);
		statusIconTime = (ImageView)convertView.findViewById(R.id.statusIconTime);
		
		textNoStats = MainApplication.getContext().getString(R.string.statsNotAvailable);
		textScore = MainApplication.getContext().getString(R.string.statsScore);		
	}
	
	@Override
	public void mapItemToViews(LevelDataItem item)
		throws SQLException
	{
		name.setText(item.getName());		

		//read stats data		
		final LevelStatsItem statsItem = MainApplication.getData().levelStats.getItemById(item.getLevelId());

		statusIconSelected.setVisibility(View.GONE);
		
		statusIconNew.setVisibility(View.GONE);		
		statusIconCompleted.setVisibility(View.GONE);
		statusIconSkull.setVisibility(View.GONE);
		statusIconTime.setVisibility(View.GONE);

		textCompleted.setVisibility(View.GONE);
		textDeads.setVisibility(View.GONE);
		textTime.setVisibility(View.GONE);
		
		if (statsItem.isNew())
		{
			//score
			score.setText(textNoStats);
			score.setVisibility(View.GONE);

			statusIconNew.setVisibility(View.VISIBLE);

		} else {
			//score
			score.setText(textScore + " " + Integer.toString(statsItem.getScore()));
			score.setVisibility(View.VISIBLE);

			if (item.isLevelSelected())
				statusIconSelected.setVisibility(View.VISIBLE);

			//completed icon
			if (statsItem.getCompletedCount() > 0)
			{
				statusIconCompleted.setVisibility(View.VISIBLE);
				textCompleted.setVisibility(View.VISIBLE);
				textCompleted.setText(Integer.toString(statsItem.getCompletedCount()));
			}

			//kills icon
			if (statsItem.getDeadCount() > 0)
			{
				statusIconSkull.setVisibility(View.VISIBLE);
				textDeads.setVisibility(View.VISIBLE);
				textDeads.setText(Integer.toString(statsItem.getDeadCount()));
			}
			
			//best time icon
			if (statsItem.getTotalPlayTimeMs() == 0)
			{
				statusIconTime.setVisibility(View.GONE);
				textTime.setVisibility(View.GONE);				
			}
			else
			{
				statusIconTime.setVisibility(View.VISIBLE);
				textTime.setVisibility(View.VISIBLE);
				textTime.setText(statsItem.getTotalPlayTimeText());				
			}
		}
	}
	
}
