package pl.pzagawa.diamond.jack.gfx;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Renderer;
import pl.pzagawa.game.engine.map.LevelData;

public class RendererGround
	extends Renderer
{
	public RendererGround(GameInstance game)
	{
		super(game, true);
	}
	
	@Override
	public void onLevelLoaded(LevelData level)
	{
		setTileLayer(level.getGroundLayer());		
		setTintColor(level.getLevelSetup().getThemeColor());
	}

	@Override
	public void onGameStateChange(int gameState)
	{
	}
	
}
