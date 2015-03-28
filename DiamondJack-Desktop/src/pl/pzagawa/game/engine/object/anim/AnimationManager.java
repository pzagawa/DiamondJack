package pl.pzagawa.game.engine.object.anim;

import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.ViewObject;
import pl.pzagawa.game.engine.objects.set.DynamicGameObject;
import pl.pzagawa.game.engine.objects.set.StaticGameObject;

public class AnimationManager
{
	private FrameSet frameSet;
	private FrameSetData frameSetData;
	private FrameSetStates frameSetStates;
	
	private int frameSkipCount = 0;
	private int skipCounter = 0;
	
	private BoundingBox collisionBox = new BoundingBox();	
	private BoundingBox collisionMargins;
	
	public AnimationManager(String textureFileName, String frameSetFileName)
	{
		this.frameSet = new FrameSet(textureFileName);
		this.frameSetData = new FrameSetData(frameSetFileName);
	}
	
	public void load(int leftMargin, int rightMargin, int topMargin, int bottomMargin)
	{
		frameSetData.load(leftMargin, rightMargin, topMargin, bottomMargin);		
		
		frameSetStates = frameSetData.getFrameSetStates();		
		
		collisionMargins = frameSetData.getCollisionMargins();
		
		frameSet.load(frameSetData.getTileWidth(), frameSetData.getTileHeight());
		frameSet.setBoundingBoxes(frameSetData.getBoundingBoxes());
				
		frameSet.setFrame(0);
		
		frameSkipCount = (60 / frameSetData.getSpeedFPS());		
	}
	
	public FrameSet getFrameSet()
	{
		return frameSet;
	}
	
	public BoundingBox getCollisionMargins()
	{
		return collisionMargins;
	}
	
	public int getFrameSkipCount()
	{
		return frameSkipCount;
	}
	
	public void dispose()
	{
		frameSet.dispose();
	}
	
	public void render(Screen screen, ViewObject viewObject, float x, float y)
	{	
		frameSet.render(screen, viewObject, x - collisionMargins.x, y - collisionMargins.y);
	}

	public BoundingBox getCollisionBox()
	{		
		collisionBox.set(frameSet.getBoundingBox(),
			collisionMargins.x,
			collisionMargins.y,
			collisionMargins.width,
			collisionMargins.height);
		
		return collisionBox;
	}
	
	public FrameSetStates getFrameSetStates()
	{
		return frameSetStates;
	}

	public void update(DynamicGameObject gameObject)
	{
		if (frameSkipCount != 0)
		{				
			skipCounter++;
			if (skipCounter < frameSkipCount)
				return;
			skipCounter = 0;
		}

		frameSet.update(gameObject.events);
		
		frameSet.setFrame(gameObject.state.getFrameIndex());
	}

	public boolean update(StaticGameObject gameObject)
	{
		if (frameSkipCount != 0)
		{				
			skipCounter++;
			
			if (skipCounter < frameSkipCount)
				return false;
			
			skipCounter = 0;
		}
		
		return true;
	}
		
	public void setFrame(int frameIndex)
	{		
		frameSet.setFrame(frameIndex);		
	}
	
}
