package pl.pzagawa.game.engine.controls;

import pl.pzagawa.game.engine.AlphaColorFadeOut;
import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.DelayTimer;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.GameState;
import pl.pzagawa.game.engine.gfx.Renderer;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.LevelData;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class RendererControls
	extends Renderer
{
	private final int tileWidth;
	private final int tileHeight;
	
	private final Texture controlsTexture;
		
	private Color tintColor = new Color(1.0f, 0.8f, 0.9f, 1.0f);
	private AlphaColorFadeOut alphaColorFadeOut = new AlphaColorFadeOut();
	
	private final static float MIN_ALPHA = 0.5f;
	
	public RendererControls(GameInstance game)
	{
		super(game, true);
		
		tileWidth = UserControls.BUTTON_WIDTH;
		tileHeight = UserControls.BUTTON_HEIGHT;
		
		controlsTexture = CommonData.get().controlsTexture;
	}
	
	@Override
	public void onLevelLoaded(LevelData level)
	{
		alphaColorFadeOut.reset();
		controlsFadeOutDelay.start();
	}
	
	@Override
	public void onGameStateChange(int gameState)
	{		
	}
	
	private DelayTimer controlsFadeOutDelay = new DelayTimer(4)
	{
		@Override
		public void onFinish(Object object)
		{
			alphaColorFadeOut.start();
		}
	};
	
	@Override
	public void update()
	{
		super.update();
		
		alphaColorFadeOut.update();			
		controlsFadeOutDelay.update();
	}
	
	@Override
	public void render(Screen screen, float speed, float shake)
	{
		if (game.getState() != GameState.GAMEPLAY)
			return;
		
		if (GameInstance.IS_APPLET_VERSION)
			return;
		
		final Color defaultColor = screen.batch.getColor();
		
		if (alphaColorFadeOut.isFinished())
		{
			tintColor.a = MIN_ALPHA;
		} else {
			tintColor.a = MIN_ALPHA + (alphaColorFadeOut.getAlphaValue() * MIN_ALPHA);
		}
				
		screen.batch.begin();
		screen.batch.enableBlending();

		int tileSrcX = 0;
		int tileSrcY = 0;

		final UserControls controls = screen.getControls();
		
		final int screenRevertedAxisY = screen.getHeight() - tileHeight;
		
		for (UserControlButton button : controls.getButtons())
		{
			final float defaultAlpha = tintColor.a;
			final float defaultG = tintColor.g;
			
			if (button.isDown())
			{
				tintColor.a = 1.0f;
				tintColor.g = 0.0f;
			}

			screen.batch.setColor(tintColor);			
			
			if (button.isPositionSet())
			{
				tileSrcX = button.getTileIndex() * tileWidth;
			
				final int buttonPosY = screenRevertedAxisY - button.getPosY();
				
				screen.batch.draw(controlsTexture, button.getPosX(), buttonPosY,
						tileSrcX, tileSrcY, tileWidth, tileHeight);
			}
			
			tintColor.a = defaultAlpha;
			tintColor.g = defaultG;
		}

		screen.batch.setColor(defaultColor);
		
		screen.batch.end();
	}
	
}
