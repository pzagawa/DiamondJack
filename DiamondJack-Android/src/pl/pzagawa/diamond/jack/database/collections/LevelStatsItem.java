package pl.pzagawa.diamond.jack.database.collections;

import java.security.NoSuchAlgorithmException;
import org.json.JSONException;
import org.json.JSONObject;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.Utils;
import android.content.Context;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LevelStats")
public class LevelStatsItem
	extends DataItem
{
	@DatabaseField(id = true)
	private long levelId = -1;
	
	@DatabaseField
	private int score = 0;
	
	@DatabaseField
	private int deadCount = 0;
	
	@DatabaseField
	private int completedCount = 0; 

	@DatabaseField
	private long startTime = 0;
	
	@DatabaseField
	private long playPassTimeMs = 0;

	@DatabaseField
	private long totalPlayTimeMs = 0;
	
	@DatabaseField
	private String completedExits = "";

	@DatabaseField
	private int totalExits = 0;

	@DatabaseField
	private int version = 0;

	@DatabaseField
	private String checksum = "";
	
	//empty ctor
	public LevelStatsItem()
	{
	}
	
	public boolean isNew()
	{
		if (score > 0)
			return false;
		
		if (deadCount > 0)
			return false;
		
		if (completedCount > 0)
			return false;
		
		return true;
	}
	
	public void setLevelId(long value)
	{
		this.levelId = value;
	}
	
	public long getLevelId()
	{
		return levelId;
	}
	
	public void playStart()
	{
		startTime = System.currentTimeMillis();
		
		updateChecksum();		
	}

	public void playFinishSuccess()
	{
		completedCount++;

		final long time = System.currentTimeMillis() - startTime;
		
		if (time < playPassTimeMs)
			playPassTimeMs = time;
					
		if (playPassTimeMs == 0)
			playPassTimeMs = time;
		
		updateTotalPlayTime();

		updateChecksum();
		
		setChanged();
	}

	public void playFinishFailure()
	{	
		deadCount++;

		updateTotalPlayTime();
		
		updateChecksum();

		setChanged();
	}
	
	private void updateTotalPlayTime()
	{
		final long time = System.currentTimeMillis() - startTime;
		
		totalPlayTimeMs += time;
	}
	
	public void updateChecksum()
	{
		StringBuilder value = new StringBuilder();
		
		value.append("*");
		value.append(levelId);
		value.append(score);
		value.append(deadCount);
		value.append(completedCount);		
		value.append(playPassTimeMs);
		value.append(totalPlayTimeMs);

		try
		{
			checksum = Utils.getMD5(value.toString());
		}
		catch (NoSuchAlgorithmException e)
		{
			checksum = "";
		}
	}
	
	public long getPlayPassTimeMs()
	{
		return playPassTimeMs;
	}
	
	public String getPlayPassTimeText()
	{
		if (playPassTimeMs == 0)
			return "?";
		
		return Utils.formatTimeAsText((int)(playPassTimeMs / 1000));
	}

	public long getTotalPlayTimeMs()
	{
		return totalPlayTimeMs;
	}

	public String getTotalPlayTimeText()
	{
		if (totalPlayTimeMs == 0)
			return "?";
		
		return Utils.formatTimeAsText((int)(totalPlayTimeMs / 1000));
	}
	
	public void addScore(int value)
	{
		this.score += value; 
	}

	public int getScore()
	{
		return score;
	}
	
	public int getDeadCount()
	{
		return deadCount;
	}
	
	public int getCompletedCount()
	{
		return completedCount;
	}

	public void addCompletedExit(String gateSymbol)
	{
		if (completedExits.contains(gateSymbol))
			return;
				
		completedExits += gateSymbol;
	}
	
	public String getCompletedExits()
	{
		return completedExits;
	}

	public void setTotalExits(int value)
	{
		totalExits = value;
	}
	
	public int getTotalExits()
	{
		return totalExits;
	}
	
	public int getVersion()
	{
		return version;
	}

	public void setVersion(int value)
	{
		this.version = value;
	}
	
	public String getCompletedExitsText(Context context)
	{
		if (completedExits.length() == 0)
			return context.getString(R.string.noExitsCompleted);

		return Integer.toString(completedExits.length()) + "/" +
			Integer.toString(totalExits);
	}	

	public static String getStatsSummaryText(Context c, LevelStatsItem item)
	{
    	String text = "";
	
		if (item == null)
		{
			text = c.getString(R.string.statsNotAvailable);			
		}
		else
		{
			text += "• " + c.getString(R.string.statsCompletionCount) + " " + Integer.toString(item.getCompletedCount()) + "\n";
			text += "• " + c.getString(R.string.statsCompletedExits) + " " + item.getCompletedExitsText(c) + "\n";
			text += "• " + c.getString(R.string.statsSuicides) + " " + Integer.toString(item.getDeadCount()) + "\n";
			text += "• " + c.getString(R.string.statsPlayTime) + " " + item.getTotalPlayTimeText() + "\n";
			text += "• " + c.getString(R.string.statsBestTime) + " " + item.getPlayPassTimeText();
		}
		
		return text;
	}
	
	@Override
	public String toString()
	{
		return Long.toString(levelId) + ". " + Integer.toString(getScore());
	}
	
	private String getNoDuplicateExits(String s)
	{
		String newList = "";
		
		if (s.contains("a"))
			newList += "a";
		
		if (s.contains("b"))
			newList += "b";
		
		if (s.contains("c"))
			newList += "c";
		
		return newList;
	}
	
	public void aggregate(LevelStatsItem item)
	{
		this.score += item.score;
		this.deadCount += item.deadCount;
		this.completedCount += item.completedCount;
		
		if (item.playPassTimeMs > 0)
		{
			if (item.playPassTimeMs < this.playPassTimeMs)
				this.playPassTimeMs = item.playPassTimeMs;
			if (this.playPassTimeMs == 0)
				this.playPassTimeMs = item.playPassTimeMs;			
		}

		this.totalPlayTimeMs += item.totalPlayTimeMs;
		
		this.totalExits = item.totalExits;

		//update gates list
		this.completedExits = getNoDuplicateExits(this.completedExits + item.completedExits);
	}

	public JSONObject toJSON()
		throws JSONException
	{
		JSONObject jobj = new JSONObject();

		jobj.put("levelId", getLevelId());
		jobj.put("version", getVersion());
		
		//return only levelId and version if 0
		if (getVersion() == 0)
			return jobj;

		jobj.put("score", getScore());
		jobj.put("deadCount", getDeadCount());
		jobj.put("completedCount", getCompletedCount());
		jobj.put("playPassTimeMs", getPlayPassTimeMs());
		jobj.put("totalPlayTimeMs", getTotalPlayTimeMs());
		jobj.put("completedExits", getCompletedExits());
		jobj.put("totalExits", getTotalExits());
		
		jobj.put("checksum", checksum); 
		
		return jobj;
	}
	
	public void resetValues()
	{
		this.score = 0;
		this.deadCount = 0;
		this.completedCount = 0; 		
		this.playPassTimeMs = 0L;
		this.totalPlayTimeMs = 0L;		
		this.completedExits = "";
		this.totalExits = 0;
		this.version = 0;
	}
	
	public void parse(JSONObject jobj)
		throws JSONException
	{
		this.score = jobj.getInt("score"); 
		this.deadCount = jobj.getInt("deadCount"); 
		this.completedCount = jobj.getInt("completedCount"); 
		this.playPassTimeMs = jobj.getLong("playPassTimeMs"); 
		this.totalPlayTimeMs = jobj.getLong("totalPlayTimeMs"); 
		this.completedExits = jobj.getString("completedExits");
		this.totalExits = jobj.getInt("totalExits");
		this.version = jobj.getInt("version");
	}
	
}
