package pl.pzagawa.diamond.jack.gfx;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.RendererMessageText;
import pl.pzagawa.game.engine.map.LevelData;

public class RendererLevelTitle
	extends RendererMessageText
{
	public RendererLevelTitle(GameInstance game)
	{
		super(game);
	}
	
	@Override
	public void onGameStateChange(int gameState)
	{

	}
	
	@Override
	public void onLevelLoaded(LevelData level)
	{
		super.onLevelLoaded(level);
		
		final String levelName = level.getLevelSetup().getLevelName();
		
		if (levelName.length() > 0)
		{
			this.setTextAndFadeOut(levelName);
		}
	}

	@Override
	protected void onTextFadedOut()
	{
	}
	
}
