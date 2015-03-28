package pl.pzagawa.game.engine;

public abstract class DelayTimer
	extends ActionInTime
{
	private int framesToWait = 60;
	private int frameCounter = 0;

	public DelayTimer(float secondsToWait)
	{
		this.framesToWait = (int) (secondsToWait * 60);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		
		this.frameCounter = 0;
	}
	
	@Override
	public boolean onUpdate()
	{
		if (isEnabled())
		{
			frameCounter++;
			
			if (frameCounter >= framesToWait)
				return false;
		}
			
		return true;
	}
	
}
