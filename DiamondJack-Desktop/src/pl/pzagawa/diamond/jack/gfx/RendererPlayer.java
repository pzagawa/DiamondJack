package pl.pzagawa.diamond.jack.gfx;

import pl.pzagawa.game.engine.AlphaColorFadeOut;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Renderer;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.LevelData;
import pl.pzagawa.game.engine.objects.GameObjectsManager;
import pl.pzagawa.game.engine.objects.PlayerObject;
import com.badlogic.gdx.graphics.Color;

public class RendererPlayer
	extends Renderer
{
	private GameObjectsManager objects;
	private PlayerObject playerObject;
	
	private AlphaColorFadeOut alphaColorFadeOut = new AlphaColorFadeOut(0.03f);
	private Color disappearColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	
	public RendererPlayer(GameInstance game)
	{
		super(game, true);
	}
	
	@Override
	public void onLevelLoaded(LevelData level)
	{
		this.objects = game.getObjects();
		this.playerObject = objects.getPlayerObject();
		
		this.alphaColorFadeOut.reset();
	}		
	
	@Override
	public void onGameStateChange(int gameState)
	{		
	}
	
	@Override
	public void dispose()
	{
	}

	@Override
	public void update()
	{
		super.update();
		
		alphaColorFadeOut.update();
		
		if (playerObject.isVanishing())
		{
			if (alphaColorFadeOut.isEnabled() || alphaColorFadeOut.isFinished()) 
				return;
			
			alphaColorFadeOut.start();
		}
	}
	
	@Override
	public void render(Screen screen, float speed, float shake)
	{		
		if (alphaColorFadeOut.isFinished())
		{
			return;
		}

		screen.batch.begin();
		screen.batch.enableBlending();
		
		final Color defaultColor = screen.batch.getColor();
		
		disappearColor.a = alphaColorFadeOut.getAlphaValue();
		
		screen.batch.setColor(disappearColor);		
				
		playerObject.render(screen, viewObject);		
		
		screen.batch.setColor(defaultColor);
		
		screen.batch.end();
		
		if (game.TEST_MODE >= 3)
		{
			drawBox(screen, playerObject.getProjectedBox());
			drawBox(screen, playerObject.getProjectedBoxHead());
		}		
	}
	
}
