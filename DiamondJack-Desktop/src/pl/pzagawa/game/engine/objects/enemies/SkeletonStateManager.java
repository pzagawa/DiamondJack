package pl.pzagawa.game.engine.objects.enemies;

import pl.pzagawa.game.engine.audio.Sounds;
import pl.pzagawa.game.engine.objects.set.DynamicGameObject;
import pl.pzagawa.game.engine.state.State;
import pl.pzagawa.game.engine.state.StateManager;

public class SkeletonStateManager
	extends StateManager
{
	public SkeletonStateManager()
	{
		super();

		addState(new State(this, "IDLE", State.IDLE));
		addState(new State(this, "ALERT", State.ALERT));
		addState(new State(this, "ATTACK", State.ATTACK));
	}

	@Override
	public void update(DynamicGameObject object)
	{
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
		//if animation starts to repeat, enter IDLE
		if (frameIndex == 0)
			setState(State.IDLE);
		
		if (stateIndex == State.ALERT)
		{
			if (frameIndex % 5 == 0)
			{
				Sounds.skeletonAlert();
				return;
			}			
		}
		
		if (stateIndex == State.ATTACK)
		{			
			if (frameIndex == 1)
			{
				Sounds.skeletonAttack();
				return;
			}
			if (frameIndex == 4)
			{			
				Sounds.skeletonAttackArms();
				return;
			}
		}
	}
	
}
