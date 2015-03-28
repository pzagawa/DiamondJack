package pl.pzagawa.diamond.jack.gfx;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Renderer;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.LevelData;

public class RendererBackground
	extends Renderer
{
	public RendererBackground(GameInstance game)
	{
		super(game, false);
	}
	
	@Override
	public void onLevelLoaded(LevelData level)
	{
		setTileLayer(level.getBackgroundLayer());
		setTintColor(level.getLevelSetup().getThemeColor());
	}
		
	@Override
	public void onGameStateChange(int gameState)
	{		
	}
	
	@Override
	public void render(Screen screen, float speed, float shake)
	{
		super.renderWrap(screen, speed * 0.5f);
	}	
	
}
