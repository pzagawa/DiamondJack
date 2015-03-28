package pl.pzagawa.game.engine.objects.set;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.map.MapGateItem;
import pl.pzagawa.game.engine.map.MapSetup;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.GameObject;

public class LevelGate
	extends GameObject
{
	private final static int COLLISION_MARGIN = 8;
	
	public final static int GATE_A = 0;
	public final static int GATE_B = 1;
	public final static int GATE_C = 2;
	public final static int MAX_GATE_COUNT = 3;
	
	public final int gateType;
	
	private long targetLevelId = -1;
	private int targetLevelGateType = -1;
	
	private boolean isCollision = true;
	
	private BoundingBox collisionBox = new BoundingBox();
		
	public LevelGate(GameInstance game, int gateType)
	{
		super(game);
		this.gateType = gateType;
	}

	public boolean isEqual(LevelGate gate)
	{
		if (this.gateType != gate.gateType)
			return false;

		if (this.targetLevelId != gate.targetLevelId)
			return false;
		
		if (this.targetLevelGateType != gate.targetLevelGateType)
			return false;
	
		return true;
	}
	
	//converts StaticGameObject index to GateType index
	public static int objectTypeToGateType(int objectType)
	{
		switch (objectType)
		{
			case StaticGameObjectType.TYPE_GATE_A:
				return LevelGate.GATE_A;
			case StaticGameObjectType.TYPE_GATE_B:
				return LevelGate.GATE_B;
			case StaticGameObjectType.TYPE_GATE_C:
				return LevelGate.GATE_C;
		}

		return -1;
	}	
	
	public static String getGateTypeSymbol(int gateType)
	{
		switch(gateType)
		{
			case LevelGate.GATE_A:
				return "a";
			case LevelGate.GATE_B:
				return "b";
			case LevelGate.GATE_C:
				return "c";			
		}
		
		return "(none)";		
	}

	public String getGateTypeSymbol()
	{
		return getGateTypeSymbol(gateType);
	}
	
	@Override
	public void reset()
	{
	}

	@Override
	public void dispose()
	{
	}

	@Override
	protected void update()
	{
	}
	
	public void updateData()
	{
		final long sourceLevelId = game.getLevel().levelId;

		MapSetup mapSetup = game.getLevelManager().getMapSetup();

		MapGateItem mapGateItem = mapSetup.getTargetGateItem(sourceLevelId, gateType);
		
		if (mapGateItem == null)
		{
			this.targetLevelId = -1;
			this.targetLevelGateType = -1;
		} else {			
			this.targetLevelId = mapGateItem.targetLevelId;
			this.targetLevelGateType = mapGateItem.targetGateType;
		}
		
		collisionBox.set(this);
		
		collisionBox.addHeight(-(COLLISION_MARGIN * 2));
		collisionBox.addWidth(-(COLLISION_MARGIN * 2));
		
		collisionBox.x += COLLISION_MARGIN;
		collisionBox.y += COLLISION_MARGIN;
	}
	
	public BoundingBox getCollisionBox()
	{
		return collisionBox;
	}
	
	public long getTargetLevelId()
	{
		return targetLevelId;
	}

	public int getTargetLevelGateType()
	{
		return targetLevelGateType;
	}
	
	public boolean isCollision()
	{
		return isCollision;
	}

	public void clearCollision()
	{
		isCollision = false;
	}

	@Override
	public String toString()
	{
		String gateTypeString = getGateTypeSymbol(this.gateType).toUpperCase();
		
		String targetGateTypeString = getGateTypeSymbol(this.targetLevelGateType).toUpperCase();
		
		return "Gate " + gateTypeString + ", target: " + Long.toString(this.targetLevelId) + "/" + targetGateTypeString;
	}
	
}
