package pl.pzagawa.game.engine.gfx;

import pl.pzagawa.game.engine.AlphaColorFadeOut;
import pl.pzagawa.game.engine.DelayTimer;
import pl.pzagawa.game.engine.GameInstance;

public abstract class RendererMessageText
	extends RendererCenterText
{
	private final static int VISIBLE_DELAY = 2;
	
	private AlphaColorFadeOut alphaColorFadeOut = new AlphaColorFadeOut();
	
	public RendererMessageText(GameInstance game)
	{
		super(game);
	}
		
	public void setTextAndFadeOut(String text)
	{
		super.setText(text);		
		
		fadeOutDelay.start();
		alphaColorFadeOut.reset();
	}
	
	private DelayTimer fadeOutDelay = new DelayTimer(VISIBLE_DELAY)
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
		
		fadeOutDelay.update();
		alphaColorFadeOut.update();		
	}
	
	@Override
	public void render(Screen screen, float speed, float shake)
	{
		if (text == null)
			return;
		
		if (alphaColorFadeOut.isFinished())
		{
			setText(null);
			onTextFadedOut();
			return;
		}

		alphaValue = alphaColorFadeOut.getAlphaValue();
		
		super.render(screen, speed, shake);
	}
	
	protected abstract void onTextFadedOut();
	
}
