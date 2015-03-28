package pl.pzagawa.diamond.jack.gfx;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Renderer;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.gfx.effects.EffectsManager;
import pl.pzagawa.game.engine.map.LevelData;

public class RendererEffects
	extends Renderer
{
	private EffectsManager effectsManager;
	
	public RendererEffects(GameInstance game)
	{
		super(game, true);
	}
		
	@Override
	public void onLevelLoaded(LevelData level)
	{
		effectsManager = game.getObjects().getEffectsManager();
	}	
	
	@Override
	public void onGameStateChange(int gameState)
	{		
	}
	
	@Override
	public void render(Screen screen, float speed, float shake)
	{
		effectsManager.render(screen);
	}
	
}
