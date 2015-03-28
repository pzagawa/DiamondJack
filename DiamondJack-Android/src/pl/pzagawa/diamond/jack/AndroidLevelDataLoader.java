package pl.pzagawa.diamond.jack;

import java.sql.SQLException;
import pl.pzagawa.diamond.jack.database.collections.LevelDataItem;
import pl.pzagawa.game.engine.EngineException;
import pl.pzagawa.game.engine.map.LevelDataLoader;
import pl.pzagawa.game.engine.map.tiles.TileLayer;
import android.content.Context;

public class AndroidLevelDataLoader
	implements LevelDataLoader
{
	private LevelDataItem levelItem; 
	
	public AndroidLevelDataLoader(Context context)
	{
	}

	@Override
	public String getMapSetup(long mapId)
	{
		//map setup not used
		return "";
	}

	@Override
	public String getLevelSetup(long levelId)
	{
		try
		{
			levelItem = MainApplication.getData().levelData.getItemById(levelId);

		} catch (SQLException e)
		{
			throw new EngineException("LevelDataLoader. Error loading LevelSetup: " + Long.toString(levelId));
		}
		
		return levelItem.getDataSetup();
	}
	
	@Override
	public String getLevelData(long levelId, String name)
	{
		if (levelItem == null)
			throw new EngineException("LevelDataLoader. LevelData item is null: " + Long.toString(levelId) + " for " + name);
		
		if (name == TileLayer.BACKGROUND)
			return levelItem.getDataBackground();

		if (name == TileLayer.GROUND)
			return levelItem.getDataGround();

		if (name == TileLayer.SHAPE)
			return levelItem.getDataShape();

		if (name == TileLayer.OBJECTS)
			return levelItem.getDataObjects();

		if (name == TileLayer.ENEMIES)
			return levelItem.getDataEnemies();
		
		throw new EngineException("LevelDataLoader. No data for LevelData: " + Long.toString(levelId) + " for " + name);
	}

}
