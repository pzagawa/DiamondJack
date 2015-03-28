package pl.pzagawa.game.engine.gfx.effects;

import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.objects.ViewObject;

public class DeadMsgEffect
	extends Effect
{		
	private final static float xOffset = 4;
	private final static float yOffset = 4;

	private final int FLY_UP_STEP = 1;
	private int flyUpOffset = 0;
	
	public DeadMsgEffect(EffectsManager effectsManager)
	{
		super(effectsManager, CommonData.EFFECTS + "dead-msg.png", CommonData.EFFECTS + "dead-msg-frameset.txt");
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
		final boolean step = super.update();
		
		if (step)
			flyUpOffset += FLY_UP_STEP;
		
		return step;
	}
	
	@Override
	public void render(Screen screen, ViewObject viewObject)
	{
		float xPos = x + xOffset;
		float yPos = y - yOffset - flyUpOffset;
		
		screen.batch.begin();
		screen.batch.enableBlending();
		
		animation.render(screen, viewObject, xPos, yPos);
		
		screen.batch.end();
	}

	@Override
	public void finish()
	{
	}	
	
}
