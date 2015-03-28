package pl.pzagawa.game.engine;

import pl.pzagawa.game.engine.objects.set.DynamicGameObject;

public class JumpController
{
	private final static int MAX_JUMP_STEPS = 16;
	
	private boolean isJumping = false;
	private int jumpStep = 0;
	
	public void reset()
	{
		stop();
	}
	
	public void start()
	{
		if (!isJumping)
		{			
			isJumping = true;
			jumpStep = 0;
		}
	}

	private void stop()
	{
		isJumping = false;
		jumpStep = 0;
	}
	
	public boolean isJumping()
	{
		return isJumping;
	}

	public void update(DynamicGameObject gameObject)
	{
		if (isJumping)
		{
			float y_speed = MAX_JUMP_STEPS - jumpStep;
						
			if (y_speed >= 0)
			{
				float pos_y = gameObject.position.getY();

				if (gameObject.events.isCollisionTop())
				{
					stop();
					return;
				}
				
				pos_y -= y_speed;
	
				jumpStep++;
								
				gameObject.position.setY(pos_y);
				
			} else {
				stop();				
			}
		}
	}
	
}
