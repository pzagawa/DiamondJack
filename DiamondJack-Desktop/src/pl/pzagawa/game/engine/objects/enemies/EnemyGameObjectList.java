package pl.pzagawa.game.engine.objects.enemies;

import java.util.ArrayList;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.map.tiles.TileLayer;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.EnemyGameObject;

public class EnemyGameObjectList
{
	private GameInstance game;
	private TileLayer layer;
	private EnemyGameObject[] objects;
	
	public EnemyGameObjectList(GameInstance game, TileLayer layer)
	{
		this.game = game;
		this.layer = layer;
	}
		
	public void build()
	{
		ArrayList<EnemyGameObject> items = new ArrayList<EnemyGameObject>();
		
		final TileItem[] tiles = layer.getTileData();
		
		final int tileWidth = layer.getTileWidth();
		final int tileHeight = layer.getTileHeight();
		
		for (TileItem tile : tiles)
		{			
			if (tile != null)
			{
				final int objectType = tile.id;
				
				EnemyGameObject object = EnemyGameObjectType.createObject(objectType, game, tile, tileWidth, tileHeight);
				
				items.add(object);
			}
		}

		objects = items.toArray(new EnemyGameObject[items.size()]);		
	}
	
	public EnemyGameObject[] getObjects()
	{
		return objects;
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
	
	public void update()
	{
		for (int index = 0; index < objects.length; index++)
			objects[index].update();
	}

	public void checkCollision(BoundingBox box)
	{
		for (int index = 0; index < objects.length; index++)
			objects[index].checkCollision(box);		
	}
	
}
