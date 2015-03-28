package pl.pzagawa.game.engine.map;

import pl.pzagawa.game.engine.objects.set.LevelGate;

public class MapGateItem
{
	public final int sourceGateType;
	public final long targetLevelId;
	public final int targetGateType;
	
	public MapGateItem(long levelId, String gate)
	{
		String[] items = gate.trim().split(":");
		
		sourceGateType = gateSymbolToGateType(items[0]);
				
		if (items.length > 1)
		{
			String[] gateData = items[1].split("/");
							
			if (gateData.length == 2)
			{
				targetLevelId = Long.parseLong(gateData[0]);						
				targetGateType = gateSymbolToGateType(gateData[1]);
			} else {
				targetLevelId = -1;
				targetGateType = -1;
			}
		} else {
			targetLevelId = -1;
			targetGateType = -1;			
		}
	}	

	private int gateSymbolToGateType(String gateSymbol)
	{
		if (gateSymbol.compareTo("a") == 0)
			return LevelGate.GATE_A;

		if (gateSymbol.compareTo("b") == 0)
			return LevelGate.GATE_B;

		if (gateSymbol.compareTo("c") == 0)
			return LevelGate.GATE_C;
		
		return -1;		
	}
	
	@Override
	public String toString()
	{
		return LevelGate.getGateTypeSymbol(sourceGateType) + ":" +
			Long.toString(targetLevelId) + "/" +
			LevelGate.getGateTypeSymbol(targetGateType);
	}
	
}
