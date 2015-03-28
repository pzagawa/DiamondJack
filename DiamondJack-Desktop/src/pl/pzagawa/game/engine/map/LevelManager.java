package pl.pzagawa.game.engine.map;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.GameState;

public class LevelManager
{	
	protected GameInstance game;

	private long newLevelId = -1;
	
	private MapSetup mapSetup;
	
	private LevelData currentLevel;	
	
	public LevelManager(GameInstance game)
	{
		this.game = game;
		this.mapSetup = new MapSetup();
	}
	
	public LevelData getCurrentLevel()
	{
		return currentLevel;
	}
	
	private LevelData getLevel(long levelId)
	{
		return new LevelDataImp(levelId);
	}

	public void setMapId(long mapId)
	{
		final String mapSetupData = game.getLoader().getMapSetup(mapId);		
		
		mapSetup.parse(mapSetupData);
	}

	public MapSetup getMapSetup()
	{
		return mapSetup;
	}
	
	private void setLevelId(long levelId)
	{
		newLevelId = levelId;
	}
	
	private LevelData getLevel()
	{
		LevelData level = null;
		
		if (newLevelId != -1)
		{
			level = getLevel(newLevelId);
			
			newLevelId = -1;
		}
		
		return level;
	}

	public void resize(int width, int height)
	{
		if (currentLevel != null)
			currentLevel.resize(width, height);
	}

	public void disposeLevel()
	{
		setLevel(null);
	}
	
	private void setLevel(LevelData level)
	{
		game.setState(GameState.GAMEPLAY);

		if (currentLevel != null)
			currentLevel.dispose();
		
		currentLevel = level;

		if (level != null)
		{
			currentLevel.load();
			
			game.onLevelLoaded(level);
		}
	}
	
	public void restartLevel()
	{
		if (currentLevel != null)
		{
			game.setState(GameState.GAMEPLAY);

			if (currentLevel != null)
				currentLevel.dispose();
		
			currentLevel.load();
			
			game.onLevelLoaded(currentLevel);
		}
	}

	public void loadLevelIfSet()
	{
		final LevelData level = getLevel();
		
		if (level != null)
			setLevel(level);
	}

	public void loadLevel(long levelId)
	{
		setLevelId(levelId);
		
		loadLevelIfSet();
	}
	
}
