package pl.pzagawa.game.engine.state;

import java.util.ArrayList;

public class State
{		
	public final static int UNDEFINED = -1;	
	public final static int IDLE = 0;
	public final static int WALK = 1;
	public final static int JUMP = 2;	
	public final static int ATTACK = 3;
	public final static int RETREAT = 4;
	public final static int DEAD = 5;	
	public final static int ALERT = 6;
	public final static int WAIT = 7;
	
	private StateManager stateManager;
	private String name;
	private int index = State.UNDEFINED;
	private ArrayList<Integer> framesList;
	private int frameListIndex = 0;
	
	public State(StateManager stateManager, String name, int index)
	{
		this.stateManager = stateManager; 
		this.name = name;
		this.index = index;		
	}

	@Override
	public String toString()
	{
		return Integer.toString(index) + "." + name;
	}
	
	public String getName()
	{
		return name;
	}

	public int getIndex()
	{
		return index;
	}

	public void setFrames(ArrayList<Integer> framesList)
	{
		this.framesList = framesList;
	}

	public void resetFrameIndex()
	{
		frameListIndex = 0;
	}
	
	public int getFrameIndex()
	{
		int frameIndex = framesList.get(frameListIndex);
		
		//if frame index in list is -1, then last frame is returned (loop)
		if (frameIndex == -1)
		{
			return framesList.get(frameListIndex - 1);			
		}

		frameListIndex++;
		if (frameListIndex > (framesList.size() - 1))
			resetFrameIndex();
		
		stateManager.onFrameChange(index, frameListIndex);
		
		return frameIndex;
	}
	
}
