package pl.pzagawa.game.engine.object.anim;

import java.util.ArrayList;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.objects.ViewObject;
import pl.pzagawa.game.engine.objects.set.StaticGameObject;

public class SimpleAnimation
{
	private final static int MARGIN_X = 0;
	private final static int MARGIN_Y = 0;
	
	protected AnimationManager animation;
	
	private FrameSet frameSet;
	private ArrayList<Integer> framesList;
	private int frameListIndex = 0;
	
	//offset of animation releated to StaticGameObject
	private int xOffset = 0;
	private int yOffset = 0;	
	
	public SimpleAnimation(AnimationManager animation, int xOffset, int yOffset)
	{
		this.animation = animation;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public void load()
	{
		animation.load(MARGIN_X, MARGIN_X, MARGIN_Y, MARGIN_Y);
		
		frameSet = animation.getFrameSet();
		
		framesList = animation.getFrameSetStates().getFramesByState("ALL");
		
		setRandomStart();
	}

	public void setZeroStart()
	{
		frameListIndex = 0;		
	}
	
	public void setRandomStart()
	{
		frameListIndex = (int)(Math.random() * framesList.size());		
	}
	
	public void dispose()
	{
		animation.dispose();
	}	
	
	public void update(StaticGameObject object)
	{	
		if (animation.update(object))
		{
			final int frameIndex = getNextFrameIndex();
			
			frameSet.setFrame(frameIndex);
		}
	}
	
	private int getNextFrameIndex()
	{
		int frameIndex = framesList.get(frameListIndex);
		
		//if frame index in list is -1, then last frame is returned (loop)
		if (frameIndex == -1)
		{
			return framesList.get(frameListIndex - 1);			
		}

		frameListIndex++;
		if (frameListIndex > (framesList.size() - 1))
			frameListIndex = 0;
		
		return frameIndex;
	}
	
	public void render(Screen screen, ViewObject viewObject, StaticGameObject object)
	{
		animation.render(screen, viewObject, object.x + xOffset, object.y + yOffset);
	}
	
}
