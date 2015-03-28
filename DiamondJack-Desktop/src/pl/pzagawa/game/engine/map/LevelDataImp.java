package pl.pzagawa.game.engine.map;

import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.map.tiles.TileLayer;

public class LevelDataImp
	extends LevelData 
{	
	public LevelDataImp(long levelId)
	{
		super(levelId);
	}
	
	@Override
	protected TileLayer createBackgroundLayer()
	{
		return new TileLayer(CommonData.GFX + "background.png", TileLayer.BACKGROUND, false);
	}

	@Override
	protected TileLayer createGroundLayer()
	{
		return new TileLayer(CommonData.GFX + "ground.png", TileLayer.GROUND, false);
	}
	
	@Override
	protected TileLayer createShapeLayer()
	{
		//include null tiles required for building collision boxes list
		return new TileLayer(CommonData.GFX + "shape.png", TileLayer.SHAPE, true);
	}

	@Override
	protected TileLayer createObjectsLayer()
	{
		return new TileLayer(CommonData.GFX + "objects.png", TileLayer.OBJECTS, false);
	}
	
	@Override
	protected TileLayer createEnemiesLayer()
	{
		return new TileLayer(CommonData.GFX + "enemies.png", TileLayer.ENEMIES, false);
	}
	
}
