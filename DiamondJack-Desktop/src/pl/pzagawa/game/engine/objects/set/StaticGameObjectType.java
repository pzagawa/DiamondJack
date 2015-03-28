package pl.pzagawa.game.engine.objects.set;

import pl.pzagawa.game.engine.EngineException;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.map.tiles.TileItem;

public class StaticGameObjectType
{
	//texture tile id mapping to object type
	
	//CollectableGameObject
	public final static int TYPE_KEY1 = 1;
	public final static int TYPE_KEY2 = 2;
	public final static int TYPE_RUBY = 3;
	public final static int TYPE_DIAMOND = 4;
	
	//DoorGameObject
	public final static int TYPE_DOOR1 = 5;
	public final static int TYPE_DOOR2 = 6;
	
	//DoorElementGameObject
	public final static int TYPE_DOORTOP = 7;
	public final static int TYPE_DOORMID = 8;

	//torchlight
	public final static int TYPE_TORCHLIGHT_R = 9;
	public final static int TYPE_TORCHLIGHT_L = 10;

	//level gates
	public final static int TYPE_GATE_A = 11;
	public final static int TYPE_GATE_B = 12;
	public final static int TYPE_GATE_C = 13;

	//lava
	public final static int TYPE_LAVA = 14;

	//CollectableGameObject
	public final static int TYPE_DIAMOND_GREEN = 15;
	
	//height of obstacle box
	private final static int DEFAULT_OBSTACLE_HEIGHT_IN_TILES = 10; 
	
	public static StaticGameObject createObject(int type, GameInstance game, TileItem tile, int tileWidth, int tileHeight)
	{
		switch (type)
		{
			case TYPE_KEY1:
			case TYPE_KEY2:
			case TYPE_RUBY:
			case TYPE_DIAMOND:
			case TYPE_DIAMOND_GREEN:
				return new CollectableGameObject(game, tile, tileWidth, tileHeight);

			case TYPE_DOOR1:
			case TYPE_DOOR2:
				return new DoorGameObject(game, tile, tileWidth, tileHeight);

			case TYPE_DOORTOP:
			case TYPE_DOORMID:
				return new DoorElementGameObject(game, tile, tileWidth, tileHeight);

			case TYPE_TORCHLIGHT_R:
				return new TorchlightGameObject(game, tile, tileWidth, tileHeight, -1);
			case TYPE_TORCHLIGHT_L:
				return new TorchlightGameObject(game, tile, tileWidth, tileHeight, 13);
				
			case TYPE_GATE_A:
			case TYPE_GATE_B:
			case TYPE_GATE_C:
				return new LevelGateGameObject(game, tile, tileWidth, tileHeight);
				
			case TYPE_LAVA:
				return new LavaGameObject(game, tile, tileWidth, tileHeight);
		}
		
		throw new EngineException("Error creating static object. Type: " + Integer.toString(type));
	}

	public static boolean isLevelGate(int type)
	{
		switch (type)
		{
			case TYPE_GATE_A:
			case TYPE_GATE_B:
			case TYPE_GATE_C:
				return true;
			default:
				return false;
		}
	}
	
	public static boolean isObstacle(int type)
	{
		switch (type)
		{
			case TYPE_DOOR1:
			case TYPE_DOOR2:
				return true;
			default:
				return false;
		}
	}

	public static int getObstacleWidth(int type, int tileWidth)
	{
		switch (type)
		{
			case TYPE_DOOR1:
			case TYPE_DOOR2:
				return tileWidth;
			default:
				return 0;
		}
	}

	public static int getObstacleHeight(int type, int tileHeight)
	{
		switch (type)
		{
			case TYPE_DOOR1:
			case TYPE_DOOR2:
				return tileHeight * DEFAULT_OBSTACLE_HEIGHT_IN_TILES;
			default:
				return 0;
		}
	}
	
	public static boolean isObstacleElement(Obstacle obstacle, StaticGameObject object)
	{
		switch (obstacle.getObjectType())
		{
			case TYPE_DOOR1:
			case TYPE_DOOR2:
				return (object.objectType == TYPE_DOORTOP || object.objectType == TYPE_DOORMID);
			default:
				return false;
		}
	}
	
}
