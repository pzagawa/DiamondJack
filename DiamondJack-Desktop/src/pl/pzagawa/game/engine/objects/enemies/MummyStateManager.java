package pl.pzagawa.game.engine.objects.enemies;

import pl.pzagawa.game.engine.audio.Sounds;
import pl.pzagawa.game.engine.objects.set.DynamicGameObject;
import pl.pzagawa.game.engine.state.State;
import pl.pzagawa.game.engine.state.StateManager;

public class MummyStateManager
	extends StateManager
{
	public MummyStateManager()
	{
		super();

		addState(new State(this, "IDLE", State.IDLE));
		addState(new State(this, "WALK", State.WALK));
		addState(new State(this, "WAIT", State.WAIT));
		addState(new State(this, "ALERT", State.ALERT));
	}

	@Override
	public void update(DynamicGameObject object)
	{
		final int state = getState();

		if (state == State.IDLE)
		{
			object.events.setMoveLeft();
			setState(State.WALK);
		}
				
		if (object.events.isCollisionLeft())
		{
			object.events.clearCollisionLeft();
			object.events.setMoveRight();
			setState(State.WALK);
		}

		if (object.events.isCollisionRight())
		{
			object.events.clearCollisionRight();
			object.events.setMoveLeft();
			setState(State.WALK);
		}
	}

	@Override
	public void onExit(State state)
	{
	}

	@Override
	public void onEnter(State state)
	{
		state.resetFrameIndex();
		
		if (state.getIndex() == State.ALERT)
			delayCounter = 0;			
	}

	private int delayCounter = 0;
	
	@Override
	public void onFrameChange(int stateIndex, int frameIndex)
	{
		if (stateIndex == State.ALERT)
		{
			if (frameIndex == 4)
			{
				if (delayCounter == 0)
				{
					Sounds.mummyGrowl();
				}
				
				delayCounter++;
				if (delayCounter > 2)
					delayCounter = 0;
			}
		}		
	}
	
}
