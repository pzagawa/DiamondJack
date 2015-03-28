package pl.pzagawa.game.engine.gfx.effects;

import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.objects.ViewObject;

public class DustEffect
	extends Effect
{
	private final int FLY_UP_STEP = 1;
	private int flyUpOffset = 0;
	
	public DustEffect(EffectsManager effectsManager)
	{
		super(effectsManager, CommonData.EFFECTS + "dust.png", CommonData.EFFECTS + "dust-frameset.txt");
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
		float centerX = x + (width >> 1) - (effectWidth >> 1);
		float yPos = y + effectHeight + flyUpOffset;
		
		screen.batch.begin();
		screen.batch.enableBlending();
		
		animation.render(screen, viewObject, centerX, yPos);
		
		screen.batch.end();
	}	

	@Override
	public void finish()
	{
	}	
	
}
