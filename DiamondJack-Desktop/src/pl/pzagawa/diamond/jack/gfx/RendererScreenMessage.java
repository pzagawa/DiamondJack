package pl.pzagawa.diamond.jack.gfx;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.RendererMessageText;
import pl.pzagawa.game.engine.map.LevelData;

public class RendererScreenMessage
	extends RendererMessageText
{
	public RendererScreenMessage(GameInstance game)
	{
		super(game);
		
		this.isAppletStartScreen = true;
	}

	@Override
	public void onLevelLoaded(LevelData level)
	{						
		if (GameInstance.IS_APPLET_VERSION)
			setText("Click to start!");
	}	
	
	@Override
	public void update()
	{
		if (GameInstance.IS_APPLET_VERSION && !GameInstance.IS_APPLET_START_SCREEN_VISIBLE)
			setText("Click to start!");
	}
	
	@Override
	public void onGameStateChange(int gameState)
	{
	}

	@Override
	protected void onTextFadedOut()
	{
	}
	
}
