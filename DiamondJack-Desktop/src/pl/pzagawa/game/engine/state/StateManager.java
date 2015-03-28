package pl.pzagawa.game.engine.state;

import java.util.ArrayList;
import pl.pzagawa.game.engine.object.anim.FrameSetStates;
import pl.pzagawa.game.engine.objects.set.DynamicGameObject;

public abstract class StateManager
{		
	protected State currentState = null;
	private State[] states = new State[32];	

	public StateManager()
	{
	}
	
	public void addState(State state)
	{
		states[state.getIndex()] = state;
	}
	
	public abstract void update(DynamicGameObject gameObject);
	public abstract void onExit(State state);
	public abstract void onEnter(State state);
	public abstract void onFrameChange(int stateIndex, int frameIndex);

	public void setState(int stateIndex)
	{
		if (currentState == states[stateIndex])
			return;
		
		if (currentState != null)
			onExit(currentState);

		currentState = states[stateIndex];
		
		onEnter(currentState);
	}
	
	public State getStateObject()
	{
		return currentState;
	}
		
	public void setFrames(FrameSetStates frames)
	{
		for (State state : states)
		{
			if (state != null)
			{
				ArrayList<Integer> framesList = frames.getFramesByState(state.getName());
				
				state.setFrames(framesList);								
			}
		}		
	}

	public int getFrameIndex()
	{
		if (currentState != null)
			return currentState.getFrameIndex();
		
		return -1;
	}

	public int getState()
	{
		if (currentState != null)
			return currentState.getIndex();
			
		return State.UNDEFINED;		
	}
	
}
