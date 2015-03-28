package pl.pzagawa.game.engine.objects.set;

import java.util.ArrayList;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.map.tiles.TileLayer;

public class StaticGameObjectList
{
	private GameInstance game;
	private TileLayer layer;
	private ObstacleList obstacles;
	private StaticGameObject[] objects;
	private LevelGateList levelGates;
	
	public StaticGameObjectList(GameInstance game, TileLayer layer)
	{
		this.game = game;
		this.layer = layer;
	}
		
	public void build()
	{
		obstacles = new ObstacleList();
		levelGates = new LevelGateList(game);
		
		ArrayList<StaticGameObject> items = new ArrayList<StaticGameObject>();
		
		final TileItem[] tiles = layer.getTileData();
		
		final int tileWidth = layer.getTileWidth();
		final int tileHeight = layer.getTileHeight();
		
		for (TileItem tile : tiles)
		{			
			if (tile != null)
			{
				final int objectType = tile.id;
				
				StaticGameObject object = StaticGameObjectType.createObject(objectType, game, tile, tileWidth, tileHeight);
				
				items.add(object);
				
				if (StaticGameObjectType.isObstacle(objectType))
					obstacles.add(game, object);
				
				if (StaticGameObjectType.isLevelGate(objectType))
					levelGates.add(game, object);
				
			}
		}

		objects = items.toArray(new StaticGameObject[items.size()]);		
		
		obstacles.build(this);
		levelGates.build(this);
	}

	public ArrayList<StaticGameObject> getObjectsInside(Obstacle obstacle)
	{
		ArrayList<StaticGameObject> list = new ArrayList<StaticGameObject>(); 
		
		if (objects != null)
		{
			for (StaticGameObject object : objects)
			{
				if (StaticGameObjectType.isObstacleElement(obstacle, object))
				{
					if (object.isInside(obstacle))
						list.add(object);
				}
			}
		}
		
		return list;
	}
	
	public void update()
	{
		for (int index = 0; index < objects.length; index++)
			objects[index].update();
		
		Obstacle[] obstacles = getObstacles();
		
		for (Obstacle obstacle : obstacles)
			obstacle.update();			
	}
	
	public void load()
	{
		for (int index = 0; index < objects.length; index++)
			objects[index].load();
	}

	public void dispose()
	{
		for (int index = 0; index < objects.length; index++)
			objects[index].dispose();
	}
	
	public StaticGameObject[] getObjects()
	{
		return objects;
	}
	
	public Obstacle[] getObstacles()
	{
		return obstacles.getObstacles();
	}
	
	public LevelGate[] getLevelGates()
	{
		return levelGates.getLevelGates();
	}
	
	public LevelGate getLevelGate(int gateType)
	{
		return levelGates.getGate(gateType);
	}
	
	public LevelGate getStartingGate()
	{
		return levelGates.getStartingGate();
	}
	
}
