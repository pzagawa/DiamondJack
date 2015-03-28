package pl.pzagawa.game.engine.objects.enemies;

import pl.pzagawa.game.engine.EngineException;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.objects.EnemyGameObject;

public class EnemyGameObjectType
{
	//texture tile id mapping to object type	
	public final static int TYPE_SNAKE = 1;
	public final static int TYPE_MUMMY = 2;
	public final static int TYPE_SPIDER = 3;
	public final static int TYPE_SCORPION = 4;
	public final static int TYPE_SKELETON_LEFT = 5;
	public final static int TYPE_SKELETON_RIGHT = 6;

	public static EnemyGameObject createObject(int type, GameInstance game, TileItem tile, int tileWidth, int tileHeight)
	{
		switch (type)
		{
			case TYPE_SNAKE:
				return new SnakeObject(game, tile, tileWidth, tileHeight);				
			case TYPE_MUMMY:
				return new MummyObject(game, tile, tileWidth, tileHeight);
			case TYPE_SPIDER:
				return new SpiderObject(game, tile, tileWidth, tileHeight);
			case TYPE_SCORPION:
				return new ScorpionObject(game, tile, tileWidth, tileHeight);
			case TYPE_SKELETON_LEFT:
				return new SkeletonObject(game, tile, tileWidth, tileHeight, true);
			case TYPE_SKELETON_RIGHT:
				return new SkeletonObject(game, tile, tileWidth, tileHeight, false);
		}
		
		throw new EngineException("Error creating enemy object. Type: " + Integer.toString(type));
	}
	
}
