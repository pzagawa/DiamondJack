package pl.pzagawa.game.engine;

import pl.pzagawa.game.engine.audio.Sounds;
import pl.pzagawa.game.engine.gfx.RendererDebugText;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.gfx.ScreenManager;
import pl.pzagawa.game.engine.map.LevelData;
import pl.pzagawa.game.engine.map.LevelDataLoader;
import pl.pzagawa.game.engine.map.LevelManager;
import pl.pzagawa.game.engine.objects.GameObjectsManager;
import pl.pzagawa.game.engine.objects.PlayerObject;
import pl.pzagawa.game.engine.objects.ViewObject;
import pl.pzagawa.game.engine.objects.set.LevelGate;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

public abstract class GameInstance
	implements ApplicationListener
{
	public static boolean IS_APPLET_VERSION = false;
	public static boolean IS_APPLET_START_SCREEN_VISIBLE = true;
	
	public static final int RESULT_CANCELED = 0;
	public static final int RESULT_COMPLETED = 1;
	
	public interface GameplayEvents
	{	
		void onGameStateChange(int gameState);
	};

	public interface ApplicationEvents
	{	
		void onGameExit(int resultCode);
	};

	public final static int TEST_MODE_DISABLED = -1;	
	public final static int TEST_MODE_FULL = 100;
	
	//public int TEST_MODE = TEST_MODE_FULL;
	public int TEST_MODE = TEST_MODE_DISABLED;
	
	private final GameStartupParams startupParams;
	private final LevelDataLoader levelDataLoader;
	private final ApplicationEvents appEvents;
	
	private static GameInstance game;
	
	public static GameplayFeedback gameplayFeedback;
	public static GameStatsEvents gameStatsEvents;
	
	private boolean initialized = false;
	
	private ScreenManager screenManager;
	private GameObjectsManager objectsManager;
	private LevelManager levelManager;
	
	private int gameState = GameState.GAMEPLAY;
	
	public GameInstance(GameStartupParams params, LevelDataLoader loader, ApplicationEvents events)
	{
		this.startupParams = params;
		this.levelDataLoader = loader;
		this.appEvents = events;
		
		GameInstance.game = this;
	}
	
	public static GameInstance getGame()
	{
		return game;
	}
	
	public LevelDataLoader getLoader()
	{
		return levelDataLoader;		
	}
	
	@Override
	public void create()
	{
		CommonData.get().load();
		
		Sounds.load();
		
		if (!initialized)
		{
			initialized = true;
			
			objectsManager = new GameObjectsManager(this);
			screenManager = new ScreenManager(this);
			
			levelManager = new LevelManager(this);
		}		
	}
	
	public void processStartupParams()
	{
		//load level
		levelManager.loadLevel(startupParams.levelId);
	}
	
	@Override
	public void dispose()
	{
		levelManager.disposeLevel();
		screenManager.dispose();		
		objectsManager.dispose();
		
		CommonData.get().dispose();
	}

	@Override
	public void resume()
	{
	}
	
	@Override
	public void pause()
	{
	}

	private void debugStart()
	{
		RendererDebugText.clear();
		RendererDebugText.startTime();		
	}

	private void debugCheck()
	{
		ViewObject viewObject = objectsManager.getViewObject();
		PlayerObject playerObject = objectsManager.getPlayerObject();
		
		float viewPosX = viewObject.left();
		float viewPosY = viewObject.top();
		
		RendererDebugText.addText("viewPos: " + Integer.toString((int)viewPosX) + "," + Integer.toString((int)viewPosY)); 		
		RendererDebugText.addText("surface: " + Integer.toString(Gdx.graphics.getWidth()) + "," + Integer.toString(Gdx.graphics.getHeight()));
		
		RendererDebugText.addText("room pos: " + Integer.toString(viewObject.getRoomPosX()) +
			"," + Integer.toString(viewObject.getRoomPosY()));
				
		RendererDebugText.addText("player pos: " + Integer.toString((int)playerObject.left()) + "," + Integer.toString((int)playerObject.top()));
	}

	private void debugFinish()
	{
		RendererDebugText.stopTime();
	}
	
	@Override
	public void render()
	{
		if (TEST_MODE >= 0)
			debugStart();
				
		//update objects
		final Screen screen = screenManager.getScreen();
		
		objectsManager.update(screen);

		if (TEST_MODE >= 0)
			debugCheck();
		
		screenManager.render();
		
		objectsManager.updateFinish();
		
		if (TEST_MODE >= 0)
			debugFinish();
		
		//process DEAD player state
		if (processPlayerDeadState())
			return;
		
		//check if set level to load
		levelManager.loadLevelIfSet();
	}
	
	private boolean processPlayerDeadState()
	{
		if (objectsManager.isPlayerDead())
		{
			levelManager.restartLevel();				
			return true;
		}
		
		return false;
	}
	
	@Override
	public void resize(int width, int height)
	{
		//replace screen real size with room world size
		final int newWidth = roomWidth();
		final int newHeight = roomHeight();
		
		levelManager.resize(newWidth, newHeight);		
		objectsManager.resize(newWidth, newHeight);
		screenManager.resize(newWidth, newHeight);
	}
	
	public void onLevelLoaded(LevelData level)
	{
		objectsManager.getInventory().reset(level.levelId);
		
		if (gameStatsEvents != null)
			gameStatsEvents.onPlayStart(level.levelId);
		
		objectsManager.onLevelLoaded(level);
		screenManager.onLevelLoaded(level);
		
		setState(GameState.GAMEPLAY);
		
		Sounds.levelStart();
	}

	public void restartLevel()
	{
		levelManager.restartLevel();
		
		setState(GameState.GAMEPLAY);
	}

	public void gameExit(int resultCode)
	{
		if (appEvents != null)
			appEvents.onGameExit(resultCode);
	}
		
	public void setScreen(Screen screen)
	{
		screenManager.setScreen(screen);
	}
	
	public Screen getScreen()
	{
		return screenManager.getScreen();
	}

	public void goToNewLevelGate(LevelGate gate)
	{
		if (gate.getTargetLevelId() == -1)
		{
			LevelGate startingGate = objectsManager.getStartingGate();

			if (gate.isEqual(startingGate))
				return;

			onLevelCompleted(gate);
			
			setState(GameState.GAME_PAUSE);
			
			//just exit, if no target B/C gate set
			getScreen().levelFadeOut.start(null);
			return;
		}

		onLevelCompleted(gate);
		
		setState(GameState.LOADING_LEVEL);

		//go to target of entered gate
		getScreen().levelFadeOut.start(gate);
	}
	
	public void onLevelCompleted(LevelGate gate)
	{
		if (gameStatsEvents != null)
		{
			final int score = getObjects().getInventory().getScore();
			
			final int totalGates = getObjects().getLevelGates().length;

			final String completedGate = gate.getGateTypeSymbol();

			gameStatsEvents.onPlayFinish(getLevel().levelId, score, totalGates, completedGate);
		}
	}
	
	//called from getScreen().levelLoadingFadeOut.onFinish
	public void loadlevelForGate(Object object)
	{
		LevelGate gate = (LevelGate)object;
		
		//after level loading, there are NEW LevelGate objects,
		//that's why we must store OLD LevelGate target here
		final long targetLevelId = gate.getTargetLevelId();
		final int targetLevelGateType = gate.getTargetLevelGateType();
		
		if (targetLevelId == -1)
			return;
		
		levelManager.loadLevel(targetLevelId);
		
		objectsManager.setPlayerPositionAtGate(targetLevelId, targetLevelGateType);		
	}
	
	public LevelData getLevel()
	{
		return levelManager.getCurrentLevel();
	}	

	public LevelManager getLevelManager()
	{
		return levelManager;
	}	
	
	public GameObjectsManager getObjects()
	{
		return objectsManager; 
	}	
	
	public void setState(int state)
	{
		this.gameState = state;
		
		screenManager.onGameStateChange(gameState);
	}

	public int getState()
	{
		return this.gameState;
	}
	
	public static boolean isAppletStartScreenMode()
	{
		return (IS_APPLET_VERSION && IS_APPLET_START_SCREEN_VISIBLE);
	}
	
	public abstract int roomWidth();
	public abstract int roomHeight();
	
	public abstract String getName();
	
}
