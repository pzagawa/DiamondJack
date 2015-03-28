package pl.pzagawa.game.engine.objects.enemies;

import pl.pzagawa.game.engine.audio.Sounds;
import pl.pzagawa.game.engine.objects.set.DynamicGameObject;
import pl.pzagawa.game.engine.state.State;
import pl.pzagawa.game.engine.state.StateManager;

public class SnakeStateManager
	extends StateManager
{
	public SnakeStateManager()
	{
		super();

		addState(new State(this, "IDLE", State.IDLE));
		addState(new State(this, "WALK", State.WALK));
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
	}

	@Override
	public void onFrameChange(int stateIndex, int frameIndex)
	{
		if (stateIndex == State.ALERT)
		{
			if (frameIndex == 3)
			{
				Sounds.snake();
			}
		}				
	}
	
}
