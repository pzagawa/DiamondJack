package pl.pzagawa.game.engine.gfx.effects;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.ViewObject;

public class EffectsManager
{	
	private final GameInstance game;
	private final Effect[] effects = new Effect[Effect.LAST]; 
	
	public EffectsManager(GameInstance game)
	{
		this.game = game;
		
		effects[Effect.STARS] = new StarsEffect(this);
		effects[Effect.DUST] = new DustEffect(this);
		effects[Effect.DEAD_MSG] = new DeadMsgEffect(this);
		effects[Effect.BONUS_LIFE] = new BonusLifeMsgEffect(this);
	}

	public void load()
	{
		for (Effect effect : effects)
			effect.load();
	}
	
	public void dispose()
	{
		for (Effect effect : effects)
			effect.dispose();		
	}
	
	public void update()
	{
		for (Effect effect : effects)
			if (effect.isActive())			
				effect.update();
	}

	public void play(BoundingBox target, int effectType)
	{
		effects[effectType].play(target);		
	}

	public boolean isActive(int effectType)
	{
		return effects[effectType].isActive();		
	}
	
	public void render(Screen screen)
	{
		ViewObject viewObject = game.getObjects().getViewObject();

		for (Effect effect : effects)
			if (effect.isActive())			
				effect.render(screen, viewObject);
	}
	
}
