package pl.pzagawa.game.engine.map;

import java.util.HashMap;
import java.util.Map;

public class MapSetup
{
	//levelId, MapSetupItem
	private Map<Long, MapSetupItem> items = new HashMap<Long, MapSetupItem>(); 
	
	public void parse(String data)
	{
		items.clear();
		
		final String[] lines = data.trim().split("\n");
		
		for (String item : lines)
		{
			final String itemString = item.trim();
			if (itemString.length() > 0)
			{
				MapSetupItem mapSetupItem = new MapSetupItem(itemString);
				items.put(mapSetupItem.levelId, mapSetupItem);
			}
		}			
	}
	
	public MapGateItem getTargetGateItem(long sourceLevelId, int sourceGateType)
	{
		if (items.containsKey(sourceLevelId))
		{
			MapSetupItem mapSetupItem = items.get(sourceLevelId);
						
			return mapSetupItem.getGate(sourceGateType);
		}
		
		return null;		
	}
	
}
