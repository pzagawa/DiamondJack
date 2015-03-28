package pl.pzagawa.diamond.jack.ui;

import pl.pzagawa.diamond.jack.activities.GameActivity;
import pl.pzagawa.diamond.jack.activities.PlayActivity;
import android.app.Activity;
import android.content.Intent;

public class ActivityStarter
{
	public final static int REQUEST_GAME_ACTIVITY = 1; 
	
	private final Activity activity;
	
	private long levelId = -1;
	
	public ActivityStarter(Activity activity)
	{
		this.activity = activity;
		
		getStartupData();
	}
	
	private void getStartupData()
	{
		Intent intent = activity.getIntent();
		if (intent != null)
		{
			levelId = intent.getLongExtra(getBundleKeyLevelId(), -1);
		}
	}	

	public long getLevelId()
	{
		return levelId;
	}

	public boolean isLevelId()
	{
		return levelId != -1;
	}

	private String getBundleKeyLevelId()
	{
		return activity.getPackageName() + ".levelId";
	}
	
	public void open(Class<? extends Activity> target)
	{
		Intent intent = new Intent(activity, target);
		
		activity.startActivity(intent);
	}

	public void open(Class<? extends Activity> target, int requestCode)
	{
		Intent intent = new Intent(activity, target);
		
		activity.startActivityForResult(intent, requestCode);
	}
	
	public void openPlayActivity(long levelId)
	{
		Intent intent = new Intent(activity, PlayActivity.class);

		intent.putExtra(getBundleKeyLevelId(), levelId);
		
		activity.startActivity(intent);
	}	

	public void openGameActivity(long levelId)
	{
		Intent intent = new Intent(activity, GameActivity.class);

		intent.putExtra(getBundleKeyLevelId(), levelId);
	
		activity.startActivity(intent);
	}	

	public void openGameActivityForResult(long levelId)
	{
		Intent intent = new Intent(activity, GameActivity.class);

		intent.putExtra(getBundleKeyLevelId(), levelId);
	
		activity.startActivityForResult(intent, REQUEST_GAME_ACTIVITY);
	}	
	
	public void setActivityResultOk()
	{
		Intent intent = new Intent(activity, activity.getClass());
		activity.setResult(Activity.RESULT_OK, intent);
	}
	
	public void setActivityResultCanceled()
	{
		Intent intent = new Intent(activity, activity.getClass());		
		activity.setResult(Activity.RESULT_CANCELED, intent);
	}
	
}
