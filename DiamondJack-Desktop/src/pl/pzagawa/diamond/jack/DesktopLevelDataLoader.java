package pl.pzagawa.diamond.jack;

import pl.pzagawa.game.engine.EngineException;
import pl.pzagawa.game.engine.Utils;
import pl.pzagawa.game.engine.map.LevelDataLoader;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class DesktopLevelDataLoader
	implements LevelDataLoader
{
	private final static String LEVELS = "data/levels/";
	
	private static String getLevelFolder(long levelId)
	{
		return LEVELS + Long.toString(levelId) + "/";
	}

	private static String getMapSetupFileName(long mapId)
	{
		return LEVELS + "map-" + Long.toString(mapId) + ".txt";
	}

	private static String getLevelSetupFileName(long levelId)
	{
		return getLevelFolder(levelId) + "setup.txt";
	}
	
	private String loadFileToString(String fileName)
	{
		try
		{
			FileHandle fileHandle = Gdx.files.getFileHandle(fileName, FileType.Internal);
						
			return Utils.ReadTextFile(fileHandle.read());
		} catch (Exception e)
		{
			throw new EngineException("LevelDataLoader. Error loading: " + fileName + ", " + e.getMessage());
		}		
	}

	@Override
	public String getMapSetup(long mapId)
	{
		final String fileName = getMapSetupFileName(mapId);

		try
		{
			return loadFileToString(fileName);
		} catch (Exception e)
		{
			//return no data if map setup not exists
			return "";
		}
	}

	@Override
	public String getLevelSetup(long levelId)
	{
		final String fileName = getLevelSetupFileName(levelId);

		return loadFileToString(fileName);
	}
	
	@Override
	public String getLevelData(long levelId, String name)
	{
		final String fileName = getLevelFolder(levelId) + name + ".txt";

		return loadFileToString(fileName);
	}
	
}
