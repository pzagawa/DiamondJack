package pl.pzagawa.diamond.jack;

import java.util.Calendar;
import android.content.Context;
import android.content.SharedPreferences;

public class Settings
{
	//fields
  private static final String SETTINGS_NAME = "DiamondJackSettings";
  
  private final SharedPreferences prefs;
  
  //settings data
  public final String[] option_items = new String[2];
  public final String[] video_items = new String[3];
  public final String[] controls_items = new String[2];
  
  //settings enums
  public final static int OPTION_ENABLED = 0;
  public final static int OPTION_DISABLED = 1;
  
  public final static int VIDEOMODE_FILL = 0;
  public final static int VIDEOMODE_FIXED = 1;
  public final static int VIDEOMODE_RATIO = 2;

  public final static int MULTITOUCH_ENABLED = 0;
  public final static int MULTITOUCH_DISABLED = 1;
    
  //settings
  private int audioState = OPTION_ENABLED;
  private int videoMode = VIDEOMODE_FILL;
  private int vibratorState = OPTION_ENABLED;
  private int multiTouchState = MULTITOUCH_ENABLED;
  private int lastAppVersion = 0;
  private long selectedLevelId = -1;
  
  //misc prefs
  private long lastGameUpdateTimeMs = 0;
  
  public Settings(Context context)
  {
	  this.prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_WORLD_READABLE);
	  
	  //selector items
	  option_items[OPTION_ENABLED] = context.getString(R.string.option_enabled);
	  option_items[OPTION_DISABLED] = context.getString(R.string.option_disabled);
	
	  video_items[VIDEOMODE_FILL] = context.getString(R.string.option_video_mode_fill);
	  video_items[VIDEOMODE_FIXED] = context.getString(R.string.option_video_mode_fixed);
	  video_items[VIDEOMODE_RATIO] = context.getString(R.string.option_video_mode_ratio);
	
	  controls_items[MULTITOUCH_ENABLED] = context.getString(R.string.option_controls_multitouch);
	  controls_items[MULTITOUCH_DISABLED] = context.getString(R.string.option_controls_singletouch);
  }
  
  public void load()
  {
	  audioState = prefs.getInt("Audio", OPTION_ENABLED);
	  videoMode = prefs.getInt("VideoMode", VIDEOMODE_FILL);
	  vibratorState = prefs.getInt("Vibrator", OPTION_ENABLED);
	  multiTouchState = prefs.getInt("MultiTouch", MULTITOUCH_ENABLED);
	  lastAppVersion = prefs.getInt("LastAppVersion", 0);
	  selectedLevelId = prefs.getLong("SelectedLevelId", -1);
	  lastGameUpdateTimeMs = prefs.getLong("LastGameUpdateTimeMs", 0);
  }
    
  private boolean save()
  {
	  try
	  {
		  SharedPreferences.Editor editor = prefs.edit();
		
		  editor.putInt("Audio", audioState);
		  editor.putInt("VideoMode", videoMode);
		  editor.putInt("Vibrator", vibratorState);
		  editor.putInt("MultiTouch", multiTouchState);
		  editor.putInt("LastAppVersion", lastAppVersion);
		  editor.putLong("SelectedLevelId", selectedLevelId);
		  editor.putLong("LastGameUpdateTimeMs", lastGameUpdateTimeMs);

		  editor.commit();
      
		  return true;
	  } catch (Exception e) {
		  return false;
	  }
  	}

	public void setAudioState(int state)
	{
		this.audioState = state;
		save();
	}

	public int getAudioState()
	{
		return this.audioState;
	}

	public void setVibratorState(int state)
	{
		this.vibratorState = state;
		save();
	}

	public int getVibratorState()
	{
		return this.vibratorState;
	}

	public void setVideoMode(int mode)
	{
		this.videoMode = mode;
		save();
	}
	
	public int getVideoMode()
	{
		return this.videoMode;
	}

	public void setMultiTouchState(int state)
	{
		this.multiTouchState = state;
		save();
	}
	
	public int getMultiTouchState()
	{
		return this.multiTouchState;
	}

	public String getAudioStateText()
	{
		return option_items[audioState];
	}

	public String getVibratorStateText()
	{
		return option_items[vibratorState];
	}
	
	public String getVideoModeText()
	{
		return video_items[videoMode];
	}

	public String getMultiTouchText()
	{
		return controls_items[multiTouchState];
	}
	
	public int getLastAppVersion()
	{
		return lastAppVersion;
	}

	public void setLastAppVersion(int value)
	{
		this.lastAppVersion = value;
		save();
	}
	
	public long getSelectedLevelId()
	{
		return selectedLevelId;
	}

	public void setSelectedLevelId(long levelId)
	{
		this.selectedLevelId = levelId;
		save();
	}
	
	public long getLastGameUpdateTimeMs()
	{
		return lastGameUpdateTimeMs;
	}
	
	public void updateLastGameUpdateTimeMs()
	{		
		lastGameUpdateTimeMs = Calendar.getInstance().getTimeInMillis();
		save();
	}	

}
