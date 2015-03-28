package pl.pzagawa.game.engine.gfx;

import java.util.ArrayList;
import pl.pzagawa.game.engine.AlphaColorFadeOut;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.controls.UserControls;
import pl.pzagawa.game.engine.map.LevelData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.Matrix4;

public abstract class Screen
	implements
		LevelData.Events,
		GameInstance.GameplayEvents
{	
	private GameInstance game;
	
	public final SpriteBatch batch;
	public final SpriteCache cache;	

	private final UserControls controls;
	
	private final Matrix4 projectionMatrix = new Matrix4();

	private ArrayList<Renderer> renderers = new ArrayList<Renderer>();  	
	
	private int width;
	private int height;
	
	private int screenState = ScreenState.NORMAL;
	
	public Screen(GameInstance game, boolean[] enabledButtons)
	{
		this.game = game;
	  	this.batch = new SpriteBatch();
	  	this.cache = new SpriteCache();
	  	this.controls = new UserControls(this, enabledButtons);
	}

	public GameInstance getGame()
	{
		return game;
	}
	
	public void setStateShake()
	{
		this.screenState = ScreenState.SHAKE;
	}

	public void setStateNormal()
	{
		this.screenState = ScreenState.NORMAL;
	}
	
	public void dispose()
	{
		for (Renderer r : renderers)
			r.dispose();
		
		batch.dispose();
		cache.dispose();
	}
	
	public final AlphaColorFadeOut levelFadeOut = new AlphaColorFadeOut()
	{
		@Override
		public void onFinish(Object object)
		{
			if (object == null)
			{
				game.gameExit(GameInstance.RESULT_COMPLETED);

			} else {
				game.loadlevelForGate(object);

			}				
		}		
	};
	
	@Override
	public void onLevelLoaded(LevelData level)
	{
	  	levelFadeOut.setSpeed(0.02f);
	  	levelFadeOut.reset();
	  	
	  	for (Renderer r : renderers)
			r.onLevelLoaded(level);
	}
	
	public void resize(int width, int height)
	{
		//width and height are taken from GameInstance room size (screen design resolution)
		this.width = width;
		this.height = height;

		projectionMatrix.setToOrtho2D(0, 0, this.width, this.height);
		
		batch.setProjectionMatrix(projectionMatrix);
		cache.setProjectionMatrix(projectionMatrix);
		
		controls.resize(width, height);		
		
		for (Renderer r : renderers)
			r.resize(this.width, this.height);
	}

	public void update()
	{
		if (!GameInstance.isAppletStartScreenMode())
			levelFadeOut.update();
		
		for (Renderer r : renderers)
		{
			if (!r.isAppletStartScreen && GameInstance.isAppletStartScreenMode())
	  			continue;
			
			r.update();
		}
	}
	
  public void render()
  {
  	float shake = 0;
  	
  	if (screenState == ScreenState.SHAKE)
  	{
  		shake = (float)(Math.random() * 2);
  	}
  	
  	final int renderers_size = renderers.size();
  				  
  	for (int index = 0; index < renderers_size; index++)
  	{
  		Renderer r = renderers.get(index);
  		
		if (!r.isAppletStartScreen && GameInstance.isAppletStartScreenMode())
  			continue;
  		
		if (r.isAppletStartScreen && !GameInstance.isAppletStartScreenMode())
  			continue;		
		
  		r.render(this, 1.0f, shake);
  	}  	
  }

  public UserControls getControls()
  {
  	return controls;
  }
  
  public void addRenderer(Renderer renderer)
  {
  	renderers.add(renderer);
  }
  
  @Override
  public void onGameStateChange(int gameState)
  {
	  for (Renderer r : renderers)
		r.onGameStateChange(gameState);
  }
  
  public abstract boolean isFinished();

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
	
	private float getSizeFactorX()
	{
		return (float)Gdx.graphics.getWidth() / (float)width;
	}

	private float getSizeFactorY()
	{
		return (float)Gdx.graphics.getHeight() / (float)height;
	}

	public int screenToWorldCoordX(int screenX)
	{
		return (int)(screenX / getSizeFactorX());
	}

	public int screenToWorldCoordY(int screenY)
	{
		return (int)(screenY / getSizeFactorY());
	}
	
	public float getScreenRatio()
	{
		return Gdx.graphics.getWidth() / Gdx.graphics.getHeight();		
	}

	public float getRoomRatio()
	{
		return width / height;
	}

}
