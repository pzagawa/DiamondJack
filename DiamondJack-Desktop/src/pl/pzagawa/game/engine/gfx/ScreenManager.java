package pl.pzagawa.game.engine.gfx;

import pl.pzagawa.game.engine.EngineException;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.map.LevelData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class ScreenManager
	implements LevelData.Events,
		GameInstance.GameplayEvents
{
	protected final GameInstance game;

	private Screen currentScreen = null;

	public ScreenManager(GameInstance game)
	{
  	this.game = game;
	}
	
  public void dispose()
  {
  	setScreen(null);
  }
	
	public void setScreen(Screen screen)
	{
		if (currentScreen != null)
			currentScreen.dispose();

		currentScreen = screen;
	}
	
	public Screen getScreen()
	{
		return currentScreen;
	}
		
  public void render()
  {
		if (currentScreen != null)
		{						
			GL10 gl = Gdx.graphics.getGL10();
			
			gl.glDisable(GL10.GL_DEPTH_TEST);
			
			gl.glClearColor(0, 0, 0, 1.0f);
			
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
  	
			currentScreen.render();			
		}
  }

	public void resize(int width, int height)
	{
		if (currentScreen != null)
			currentScreen.resize(width , height);		
	}
  
	@Override
	public void onLevelLoaded(LevelData level)
	{
		if (getScreen() == null)
			throw new EngineException("Screen not ready to process level loading");
		
		getScreen().onLevelLoaded(level);
	}
	
	@Override
	public void onGameStateChange(int gameState)
	{
		if (getScreen() == null)
			throw new EngineException("Screen not ready to process game state change");
		
		getScreen().onGameStateChange(gameState);
	}
	
}
