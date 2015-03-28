package pl.pzagawa.game.engine.gfx.effects;

import java.util.ArrayList;

import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.object.anim.AnimationManager;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.ViewObject;

public abstract class Effect
	extends BoundingBox
{
	public final static int STARS = 0; 
	public final static int DUST = 1; 
	public final static int DEAD_MSG = 2;
	public final static int BONUS_LIFE = 3;
	public final static int LAST = 4;

	protected EffectsManager effectsManager; 	
	protected AnimationManager animation;
	private ArrayList<Integer> frames;
	private int listItemIndex = 0;	
	
	private boolean isActive = false;
	private int frameSkipCount = 0;
	private int skipCounter = 0;	

	protected int effectWidth = 0;
	protected int effectHeight = 0;
	
	public Effect(EffectsManager effectsManager, String textureFileName, String frameSetFileName)
	{
		this.effectsManager = effectsManager;
		this.animation = new AnimationManager(textureFileName, frameSetFileName);
	}	
	
	public void load()
	{
		animation.load(0, 0, 0, 0);
		
		this.frames = animation.getFrameSetStates().getFramesByState("ALL");

		this.frameSkipCount = animation.getFrameSkipCount();
		
		this.setSize(animation.getCollisionBox());
		
		this.effectWidth = animation.getCollisionBox().getWidth();		
		this.effectHeight = animation.getCollisionBox().getHeight();		
	}
	
	public void dispose()
	{
		animation.dispose();
	}
	
	protected void reset()
	{
		listItemIndex = 0;
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public void play(BoundingBox target)
	{
		this.set(target);

		reset();
		
		isActive = true;
	}	

	public boolean update()
	{
		if (frameSkipCount != 0)
		{		
			if (skipCounter > frameSkipCount)
			{
				skipCounter = 0;
			} else {
				skipCounter++;
				return false;
			}
		}
		
		int frameIndex = frames.get(listItemIndex);
		
		//if frame index in list is -1, then last frame is returned (loop)
		if (frameIndex == -1)
		{
			int prevFrameIndex = frames.get(listItemIndex - 1);
			animation.setFrame(prevFrameIndex);											
			return true;
		}

		listItemIndex++;
		if (listItemIndex > (frames.size() - 1))
		{
			isActive = false;
			reset();
			finish();
			return false;
		}
		
		animation.setFrame(frameIndex);
		return true;
	}
	
	public abstract void render(Screen screen, ViewObject viewObject);
	public abstract void finish();

}
