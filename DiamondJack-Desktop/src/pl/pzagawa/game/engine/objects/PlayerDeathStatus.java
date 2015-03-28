package pl.pzagawa.game.engine.objects;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.effects.Effect;
import pl.pzagawa.game.engine.gfx.effects.EffectsManager;
import pl.pzagawa.game.engine.player.Inventory;
import pl.pzagawa.game.engine.state.State;

public class PlayerDeathStatus
{
	private Inventory inventory;
	private EffectsManager effectsManager;
	
	private boolean deathStart = false;
	private boolean deathFinish = false;
	private boolean deathProcessDone = false;

	public void reset(GameInstance game)
	{
		this.inventory = game.getObjects().getInventory();
		
		this.effectsManager = game.getObjects().getEffectsManager();
		
		deathStart = false;
		deathFinish = false;
		deathProcessDone = false;
	}
	
	public void udpate(PlayerObject object)
	{
		if (object.state.getState() == State.DEAD)
		{
			//deadStart works like a mutex, because this method is called every frame and events are cleared
			if (object.events.isDead())
				deathStart = true;
			
			if (deathStart && object.events.isCollisionBottom())
			{
				inventory.playerKill();
								
				effectsManager.play(object, Effect.DEAD_MSG);

				deathStart = false;
				deathFinish = true;
			}
			
			if (deathFinish && !effectsManager.isActive(Effect.DEAD_MSG))
			{
				deathProcessDone = true;
			}			
		}
	}
	
	public boolean isPlayerDead(PlayerObject object)
	{
		if (object.state.getState() == State.DEAD)
		{
			return deathProcessDone;
		}
		
		return false;
	}
		
}
