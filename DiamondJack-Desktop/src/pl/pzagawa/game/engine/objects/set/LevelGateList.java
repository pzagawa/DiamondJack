package pl.pzagawa.game.engine.objects.set;

import java.util.HashMap;
import java.util.Map;

import pl.pzagawa.game.engine.GameInstance;

public class LevelGateList
{	
	private Map<Integer, LevelGate> gates = new HashMap<Integer, LevelGate>();
	private LevelGate[] gatesArray;
	
	public LevelGateList(GameInstance game)
	{
	}
	
	public void add(GameInstance game, StaticGameObject object)
	{
		final int gateType = LevelGate.objectTypeToGateType(object.objectType);
		
		if (!gates.containsKey(gateType))
			gates.put(gateType, new LevelGate(game, gateType));			
		
		gates.get(gateType).expand(object);		
	}
	
	public void build(StaticGameObjectList objects)
	{
		for (Map.Entry<Integer, LevelGate> entry : gates.entrySet())
			entry.getValue().updateData();
		
		//convert map to array
		gatesArray = new LevelGate[gates.size()];

		int index = 0;
		for (Map.Entry<Integer, LevelGate> entry : gates.entrySet())
			gatesArray[index++] = entry.getValue();
	}	
	
	public LevelGate getStartingGate()
	{
		return gates.get(LevelGate.GATE_A);
	}

	public LevelGate getGate(int gateType)
	{
		return gates.get(gateType);
	}
	
	public LevelGate[] getLevelGates()
	{
		return gatesArray;
	}	
	
}
