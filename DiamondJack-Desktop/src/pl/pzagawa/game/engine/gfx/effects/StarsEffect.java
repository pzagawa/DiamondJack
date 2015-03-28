package pl.pzagawa.game.engine.gfx.effects;

import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.objects.ViewObject;

public class StarsEffect
	extends Effect
{
	private final int SIZE = 4;	
	private final int FLY_UP_STEP = 2;
	private int flyUpOffset = 0;

	public StarsEffect(EffectsManager effectsManager)
	{
		super(effectsManager, CommonData.EFFECTS + "star.png", CommonData.EFFECTS + "star-frameset.txt");
	}

	@Override
	protected void reset()
	{
		super.reset();
		flyUpOffset = 0;
	}
		
	@Override
	public boolean update()
	{
		final boolean frameStep = super.update();
		
		if (frameStep)
			flyUpOffset -= FLY_UP_STEP;
		
		return frameStep;
	}
	
	@Override
	public void render(Screen screen, ViewObject viewObject)
	{
		screen.batch.begin();
		screen.batch.enableBlending();
		
		float centerX = x + (width >> 1) - (effectWidth >> 1);
		float centerY = y + (height >> 1) - (effectHeight >> 1) + flyUpOffset;
		
		animation.render(screen, viewObject, centerX - effectWidth + SIZE, centerY);
		animation.render(screen, viewObject, centerX + effectWidth - SIZE, centerY);
		
		animation.render(screen, viewObject, centerX, centerY - effectHeight + SIZE);
		animation.render(screen, viewObject, centerX, centerY + effectHeight - SIZE);
		
		screen.batch.end();
	}

	@Override
	public void finish()
	{
	}	
	
}
