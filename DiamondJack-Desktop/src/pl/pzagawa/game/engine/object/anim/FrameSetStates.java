package pl.pzagawa.game.engine.object.anim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import pl.pzagawa.game.engine.EngineException;

//converts frames list by state map string to data collection
//example format: "IDLE:0;WALK:1,2,3,4,5,6,7,8;JUMP:9,10,11,12"
public class FrameSetStates
{
	private Map<String, ArrayList<Integer>> stateFramesList = new HashMap<String, ArrayList<Integer>>();
	
	public FrameSetStates(String text)
	{
		try
		{
			//get state items with frames indexes array
			String[] stateFrames = text.split(";");
			
			//iterate
			for (int index = 0; index < stateFrames.length; index++)
			{
				//get state item
				String[] stateItem = stateFrames[index].split(":");
	
				String stateName = stateItem[0];
				String[] frames = stateItem[1].split(",");
	
				ArrayList<Integer> framesList = new ArrayList<Integer>();
				
				//convert frame indexes string to integer collection
				for (String frame : frames)
				{
					//check for frame multiplication command
					if (frame.contains("*"))
					{
						processMultiFrame(frame, framesList);
						continue;
					}
					
					//add default frame
					framesList.add(Integer.parseInt(frame));
				}

				stateFramesList.put(stateName, framesList);		
			}
		} catch (Exception e)
		{
			throw new EngineException("Error parsing frameset states data: " + text + ", " + e.getMessage());
		}
	}

	private void processMultiFrame(String frameItem, ArrayList<Integer> framesList)
	{
		String[] multiFrame = frameItem.split("[*]");
		
		final int frameIndex = Integer.parseInt(multiFrame[0]);
		final int frameCount = Integer.parseInt(multiFrame[1]);
		
		for (int index = 0; index < frameCount; index++)
		{
			framesList.add(frameIndex);
		}		
	}
	
	public ArrayList<Integer> getFramesByState(String stateName)
	{
		if (!stateFramesList.containsKey(stateName))
			throw new EngineException("Frames list for state '" + stateName + "', has not been found");
			
		return stateFramesList.get(stateName);
	}
	
}
