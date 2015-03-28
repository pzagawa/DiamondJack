package pl.pzagawa.game.engine;

import com.badlogic.gdx.Gdx;

public abstract class FrameSpeedController
{
	public final static float TARGET_FPS = 60.0f;
	
	private final static float TIME_SPAN = 1.0f / TARGET_FPS;	
	private float timeAccum = 0;

	public void update()
	{		
		timeAccum += Gdx.graphics.getDeltaTime();
    while(timeAccum > TIME_SPAN)
    {			
    	onUpdate();

			//control frame time span
			timeAccum -= TIME_SPAN;
		}
	}
	
	public abstract void onUpdate();

}
