package pl.pzagawa.game.engine;

public class AlphaColorFadeOut
	extends ActionInTime
{
	private final static float NO_ALPHA = 1.0f;
	
	private float alphaValue = NO_ALPHA;
	private float speed = 0.01f;

	public AlphaColorFadeOut()
	{		
	}

	public AlphaColorFadeOut(float speed)
	{		
		this.speed = speed; 
	}
	
	@Override
	public void reset()
	{
		super.reset();
		
		this.alphaValue = NO_ALPHA;
	}
	
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	
	@Override
	public boolean onUpdate()
	{
		alphaValue -= speed;
		
		if (alphaValue < 0)
		{
			alphaValue = 0;
			return false;
		}
		
		return true;
	}

	public float getAlphaValue()
	{
		return alphaValue;
	}
	
	public boolean isTransparent()
	{
		return alphaValue < NO_ALPHA;
	}
		
	@Override
	public void onFinish(Object object)
	{
	}
	
}
