package pl.pzagawa.game.engine.gfx.effects;

import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.objects.ViewObject;

public class BonusLifeMsgEffect
	extends Effect
{		
	private final static float xOffset = 32;
	private final static float yOffset = 32;
	
	public BonusLifeMsgEffect(EffectsManager effectsManager)
	{
		super(effectsManager, CommonData.EFFECTS + "bonus-life-msg.png", CommonData.EFFECTS + "bonus-life-msg-frameset.txt");
	}

	@Override
	protected void reset()
	{
		super.reset();
	}
	
	@Override
	public boolean update()
	{
		return super.update();
	}
	
	@Override
	public void render(Screen screen, ViewObject viewObject)
	{
		float xPos = x + xOffset;
		float yPos = y - yOffset;
		
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
