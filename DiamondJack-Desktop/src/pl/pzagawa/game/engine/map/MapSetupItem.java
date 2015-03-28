package pl.pzagawa.game.engine.map;

import pl.pzagawa.game.engine.objects.set.LevelGate;

public class MapSetupItem
{
	public final long levelId;
	
	private MapGateItem[] gates = new MapGateItem[LevelGate.MAX_GATE_COUNT];
	
	public MapSetupItem(String data)
	{
		String[] items = data.split("\\.");
		
		this.levelId = Long.parseLong(items[0].trim());
		
		//parse gates if they exists
		if (items.length > 1)
		{
			String[] gatesList = items[1].trim().split(",");		
			
			for (String gateData : gatesList)
			{
				MapGateItem mapGateItem = new MapGateItem(levelId, gateData);
				
				gates[mapGateItem.sourceGateType] = mapGateItem;
			}
		}		
	}
	
	public MapGateItem getGate(int sourceGateType)	
	{
		return gates[sourceGateType];
	}

	@Override
	public String toString()
	{
		String gatesList = "";
		
		for (int index = 0; index < gates.length; index++)
		{
			MapGateItem gateItem = gates[index];
			if (gateItem != null)
				gatesList += gateItem.toString() + ",";
		}			
		
		return Long.toString(levelId) + "." + gatesList;
	}
	
}
