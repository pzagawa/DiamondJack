package pl.pzagawa.game.engine.objects;

import pl.pzagawa.game.engine.EngineException;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.GameState;
import pl.pzagawa.game.engine.Utils;
import pl.pzagawa.game.engine.controls.UserControls;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.gfx.effects.EffectsManager;
import pl.pzagawa.game.engine.map.LevelData;
import pl.pzagawa.game.engine.objects.enemies.EnemyGameObjectList;
import pl.pzagawa.game.engine.objects.set.LevelGate;
import pl.pzagawa.game.engine.objects.set.StaticGameObjectList;
import pl.pzagawa.game.engine.player.Inventory;
import com.badlogic.gdx.Gdx;

public class GameObjectsManager
	implements LevelData.Events
{
	protected final GameInstance game;

	private String playerParams = "power:1.0;friction:0.70;gravity:1.7";
	
	private final static float TIME_SPAN = 1.0f / 60.0f;
	private float timeAccum = 0;

	private ViewObject viewObject;
	private PlayerObject playerObject;
	private StaticGameObjectList staticObjects;
	private EnemyGameObjectList enemyObjects;
	private CollisionManager collisionManager;
	private EffectsManager effectsManager;
	private Inventory inventory;

	public GameObjectsManager(GameInstance game)
	{
		this.game = game;

		this.viewObject = new ViewObject(game);

		this.playerObject = new PlayerObject(game);

		this.collisionManager = new CollisionManager(game);

		this.effectsManager = new EffectsManager(game);
		this.effectsManager.load();

		this.inventory = new Inventory(game);
	}

	@Override
	public void onLevelLoaded(LevelData level)
	{
		collisionManager.onLevelLoaded(level);

		playerObject.load();
		playerObject.setParams(Utils.propertiesFromString(playerParams));

		staticObjects = level.getStaticObjects();

		enemyObjects = level.getEnemyObjects();

		// initialize player starting position
		playerObject.setPositionAtGate(getStartingGate());
	}

	public LevelGate getStartingGate()
	{
		return staticObjects.getStartingGate();
	}

	public LevelGate[] getLevelGates()
	{
		return staticObjects.getLevelGates();
	}
	
	public void dispose()
	{
		staticObjects.dispose();
		playerObject.dispose();
		effectsManager.dispose();
	}

	public void resize(int width, int height)
	{
		viewObject.setSize(width, height);
	}

	public void update(Screen screen)
	{
		final int gameState = game.getState();
		
		final boolean isGamePaused = (gameState == GameState.GAME_PAUSE);

		final UserControls controls = screen.getControls();

		timeAccum += Gdx.graphics.getDeltaTime();
		while (timeAccum > TIME_SPAN)
		{
			//renderers update
			screen.update();
			
			//pause for applet start screen
			if (GameInstance.isAppletStartScreenMode())
			{
				if (Gdx.input.isTouched())
					GameInstance.IS_APPLET_START_SCREEN_VISIBLE = false;
				
			} else
			{
				if (!isGamePaused)
				{
					//pool input
					controls.update();
		
					if (controls.isLeft())
						playerObject.goLeft();
		
					if (controls.isRight())
						playerObject.goRight();
		
					if (controls.isUp())
						playerObject.goUp();
		
					if (controls.isDown())
						playerObject.goDown();
		
					if (controls.isAction1())
						playerObject.doAction(controls.isHeldAction1());
		
					collisionManager.update(playerObject, staticObjects, enemyObjects);
			
					playerObject.update();
		
					viewObject.followObject(playerObject);
		
					staticObjects.update();
		
					enemyObjects.update();
		
					effectsManager.update();			
				}

			}

			// control frame time span
			timeAccum -= TIME_SPAN;
		}
	}

	public void updateFinish()
	{
		playerObject.udpateDeadStatus();

		// defaults
		playerObject.clearEvents();
	}

	public boolean isPlayerDead()
	{
		return playerObject.isPlayerDead();
	}

	public ViewObject getViewObject()
	{
		return viewObject;
	}

	public PlayerObject getPlayerObject()
	{
		return playerObject;
	}

	public EffectsManager getEffectsManager()
	{
		return effectsManager;
	}

	public Inventory getInventory()
	{
		return inventory;
	}

	public void setPlayerPositionAtGate(long targetLevelId, int gateType)
	{
		LevelGate gate = getStartingGate();

		if (gateType != -1)
		{
			gate = staticObjects.getLevelGate(gateType);
		}

		if (gate == null)
		{	
			String msg = "SetPlayerPosition: gate " + 
				LevelGate.getGateTypeSymbol(gateType) + " of level " + Long.toString(targetLevelId) +
				" not found";
			
			throw new EngineException(msg);
		}

		playerObject.setPositionAtGate(gate);
	}
}
