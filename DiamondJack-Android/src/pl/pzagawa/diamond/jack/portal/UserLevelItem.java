package pl.pzagawa.diamond.jack.portal;

import org.json.JSONException;
import org.json.JSONObject;

public class UserLevelItem
{
	public final long levelId;
	public final String name;
	public final boolean isPublic;
	public final boolean isFree;
	
	public UserLevelItem(JSONObject jLevelItem)
		throws JSONException
	{
		this.levelId = jLevelItem.getLong("levelId");
		this.name = jLevelItem.getString("name");
		this.isPublic = jLevelItem.getBoolean("isPublic");
		this.isFree = jLevelItem.getBoolean("isFree");
	}
	
	@Override
	public String toString()
	{
		return Long.toString(levelId);
	}
	
	public String getName()
	{	
		return name.replace("\n", "");
	}

}
