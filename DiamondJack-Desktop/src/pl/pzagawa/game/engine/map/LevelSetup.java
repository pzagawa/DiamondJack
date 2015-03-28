package pl.pzagawa.game.engine.map;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.graphics.Color;

public class LevelSetup
{
	private Map<String, LevelSetupItem> items = new HashMap<String, LevelSetupItem>(); 

	public void parse(String data)
	{
		items.clear();		
		
		final String[] lines = data.split("\n");
		
		for (String item : lines)
		{
			LevelSetupItem setupItem = new LevelSetupItem(item);			
			items.put(setupItem.getKey(), setupItem);
		}
	}
	
	public Color getThemeColor()
	{
		final float r = 1f;
		final float g = 1f;
		final float b = 1f;
		final float a = 1f;		
		
		return new Color(r, g, b, a);
	}

	public long getLevelId()
	{
		return items.get("id").getLong();
	}
	
	public String getLevelName()
	{
		return items.get("name").getString();
	}
	
	public String getLevelDescription()
	{
		return items.get("description").getString();
	}

	public String getAuthor()
	{
		return items.get("author").getString();
	}

	public int getRating()
	{
		return items.get("rating").getInteger();
	}

	public boolean isPublic()
	{
		return items.get("isPublic").getBool();
	}
	
	public boolean isPrivate()
	{
		return items.get("isPrivate").getBool();
	}

	public String getTileMapSize(String name)
	{
		return items.get("size-" + name).getString();		
	}

}
