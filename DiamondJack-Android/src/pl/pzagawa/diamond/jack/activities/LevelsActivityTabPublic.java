package pl.pzagawa.diamond.jack.activities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.database.collections.LevelDataItem;
import pl.pzagawa.diamond.jack.lists.LevelsListAdapter;
import pl.pzagawa.diamond.jack.ui.CommonTab;
import pl.pzagawa.diamond.jack.ui.CommonTabActivity;
import pl.pzagawa.diamond.jack.ui.OperationWithProgress;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class LevelsActivityTabPublic
	extends CommonTab
{
	private ListView levelsList;
	private LinearLayout emptyPublicListView;	
	private TextView labelTip;
	
	private List<LevelDataItem> data = new ArrayList<LevelDataItem>();
	
	public LevelsActivityTabPublic(CommonTabActivity parent, AdapterView.OnItemClickListener onListItemClick)
	{
		super(parent);
		
		levelsList = (ListView)parent.findViewById(R.id.publicLevelsList);
		levelsList.setOnItemClickListener(onListItemClick);		
		emptyPublicListView = (LinearLayout)parent.findViewById(R.id.emptyPublicListView);				
		labelTip = (TextView)parent.findViewById(R.id.labelTipPublic);		
	}
	
	@Override
	protected int getTabIndex()
	{
		return 0;
	}
	
	@Override
	protected int getTabTextResId()
	{
		return R.string.tab_public;		
	}

	@Override
	protected int getTabIconResId()
	{
		return R.drawable.tab_public;
	}

	@Override
	protected int getTabContentResId()
	{
		return R.id.contentTab1;
	}	
	
	public boolean isListEmpty()
	{
		return data.isEmpty();
	}
	
	public void updateListView()
	{
		LevelsListAdapter adapter = new LevelsListAdapter(parent, data, R.layout.listview_level_item);			

		levelsList.setAdapter(adapter);

		updateEmptyView(levelsList, emptyPublicListView);
	}
		
	@Override
	public void onLoadData()
	{
		//update account tip visibility
		labelTip.setVisibility(View.VISIBLE);		
		if (MainApplication.isUserProfileOnline())
			if (MainApplication.getUserProfile().accessRights.canDownloadPublicLevels())		
				labelTip.setVisibility(View.GONE);		
		
		//list loader
		String dialogTitle = parent.getString(R.string.dialog_please_wait_title);
		String dialogText = parent.getString(R.string.dialog_loading_data_text);
		
		OperationWithProgress operationLoadData = new OperationWithProgress(dialogTitle, dialogText)
		{
			@Override
			protected void runWorkerThread()
				throws SQLException
			{
				data = MainApplication.getData().levelData.getPublicLevels();
			}

			@Override
			protected void runFinishUiThread()
			{				
				updateListView();
			}
		};
		
		operationLoadData.run(parent);
	}
	
}
