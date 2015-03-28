package pl.pzagawa.game.engine;

public abstract class ActionInTime
{
	private boolean isEnabled = false;
	private boolean isFinished = false;	
	private Object object;

	public void start(Object object)
	{
		reset();
		
		this.isEnabled = true;
		this.object = object;
	}

	public void start()
	{
		start(null);
	}	
	
	public void update()
	{
		if (!isEnabled)
			return;
		
		if (onUpdate())
			return;
		
		finish();
	}
	
	protected void finish()
	{
		isEnabled = false;
		isFinished = true;
		
		onFinish(object);
		
		object = null;
	}
	
	public boolean isFinished()
	{
		return isFinished;
	}

	public boolean isEnabled()
	{
		return isEnabled;
	}
	
	public void reset()
	{
		isEnabled = false;
		isFinished = false;
	}
	
	public abstract boolean onUpdate();
	public abstract void onFinish(Object object);
	
}
