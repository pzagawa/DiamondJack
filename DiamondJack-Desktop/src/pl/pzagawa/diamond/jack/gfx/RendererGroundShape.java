package pl.pzagawa.diamond.jack.gfx;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Renderer;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.LevelData;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.shape.GroundShape;

public class RendererGroundShape
	extends Renderer
{
	private GroundShape shape = null;
	
	public RendererGroundShape(GameInstance game)
	{
		super(game, true);
	}
	
	@Override
	public void onLevelLoaded(LevelData level)
	{
		shape = game.getLevel().getGroundShape();
	}	
	
	@Override
	public void onGameStateChange(int gameState)
	{		
	}
	
	@Override
	public void render(Screen screen, float speed, float shake)
	{
		if (game.TEST_MODE >= 2)
		{
			BoundingBox[] boxes = shape.getBoundingBoxes();
			
			for (BoundingBox box : boxes)
				drawGroundShape(screen, box);
		}
	}
	
}
