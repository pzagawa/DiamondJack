package pl.pzagawa.diamond.jack.portal;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserLevels
{
	private final List<UserLevelItem> levels = new ArrayList<UserLevelItem>();

	public UserLevels()
	{		
	}
	
	public UserLevels(JSONArray jLevels)
		throws JSONException
	{		
		for (int index = 0; index < jLevels.length(); index++)
		{
			JSONObject jLevelItem = jLevels.getJSONObject(index);
			
			levels.add(new UserLevelItem(jLevelItem));			
		}

	}
	
	public List<UserLevelItem> getLevels()
	{
		return levels;
	}

	public List<UserLevelItem> cloneLevels()
	{
		List<UserLevelItem> result = new ArrayList<UserLevelItem>();
		
		for (UserLevelItem item : levels)
			result.add(item);
		
		return result;
	}	

}
