package pl.pzagawa.game.engine.objects.enemies;

import pl.pzagawa.game.engine.DelayTimer;
import pl.pzagawa.game.engine.objects.set.DynamicGameObject;
import pl.pzagawa.game.engine.state.State;
import pl.pzagawa.game.engine.state.StateManager;

public class SpiderStateManager
	extends StateManager
{
	private final float MIN_ATTACK_DELAY_SECONDS = 2;
		
	private DelayTimer stateAttackDelay;
	
	public SpiderStateManager()
	{
		super();				

		addState(new State(this, "IDLE", State.IDLE));
		addState(new State(this, "ATTACK", State.ATTACK));
		addState(new State(this, "RETREAT", State.RETREAT));		
	}
	
	public void setStartPositionX(int posX)
	{
		float timePositionOffset = (posX % 10) * 0.1f;
		
		float attackDelay = MIN_ATTACK_DELAY_SECONDS + timePositionOffset;
	
		setAttackDelay(attackDelay);
	}
	
	private void setAttackDelay(float value)
	{
		stateAttackDelay = new DelayTimer(value)
		{
			@Override
			public void onFinish(Object object)
			{
				setState(State.ATTACK);
			}
		};		
	}
			
	@Override
	public void update(DynamicGameObject object)
	{
		final int state = getState();
		
		if (state == State.IDLE)
		{
			object.events.clearCollisionBottom();
			
			if (!stateAttackDelay.isEnabled())
				stateAttackDelay.start();
		}
		
		if (state == State.ATTACK)
		{
			if (object.events.isCollisionBottom())
			{
				setState(State.RETREAT);
			}
		}
		
		if (state == State.RETREAT)
		{
			if (object.events.isCollisionTop())
			{
				setState(State.IDLE);
			}
		}		
		
		stateAttackDelay.update();
	}

	@Override
	public void onExit(State state)
	{
	}

	@Override
	public void onEnter(State state)
	{
		state.resetFrameIndex();		
	}

	@Override
	public void onFrameChange(int stateIndex, int frameIndex)
	{
	}
	
}
