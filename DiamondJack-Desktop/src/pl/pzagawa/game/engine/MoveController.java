package pl.pzagawa.game.engine;

import java.util.Map;

import pl.pzagawa.game.engine.objects.GameObjectEvents;

public class MoveController
{
	private float x = 0;
	private float y = 0;
	
  private float power = 0;
  private float xSpeed = 0;
  private float ySpeed = 0;
  private float friction = 0;
  private float gravity = 0;

  public MoveController()
  {
	  power = 0.5f;
	  xSpeed = 0;
	  ySpeed = 0;
	  friction = 0;
	  gravity = 0;
  }
	
	public void setParams(Map<String, String> map)
	{
		if (map.containsKey("x"))
			x = Integer.parseInt(map.get("x"));
		if (map.containsKey("y"))
			y = Integer.parseInt(map.get("y"));
		
		setPower(Float.parseFloat(map.get("power")));
		setFriction(Float.parseFloat(map.get("friction")));
		setGravity(Float.parseFloat(map.get("gravity")));
	}

	public float getProjectedX(GameObjectEvents events, int TestTreshold)
	{
		float new_x = x;
		float new_xSpeed = xSpeed;
		
		//process movement
		if (events.isMoveLeft())
			new_xSpeed -= power;

		if (events.isMoveRight())
			new_xSpeed += power;
		
		//update pos
		if (friction != 0)
			new_xSpeed = (new_xSpeed * friction);

		new_x += new_xSpeed;		
		
		if (events.isMoveLeft())
			new_x -= TestTreshold;
		
		if (events.isMoveRight())
			new_x += TestTreshold;		
						
		return new_x;		
	}

	public float getProjectedY(GameObjectEvents events, int TestTreshold)
	{		
		float new_y = y;
		float new_ySpeed = ySpeed;
		
		//process movement
		if (events.isMoveUp())
			new_ySpeed -= power;

		if (events.isMoveDown())
			new_ySpeed += power;		
		
		//update pos
		if (friction != 0)
			new_ySpeed = (new_ySpeed * friction);
		
		if (gravity != 0)
			new_ySpeed += gravity;

		new_y += new_ySpeed;
		
		if (events.isMoveUp())
			new_y -= TestTreshold;
		
		if (events.isMoveDown())
			new_y += TestTreshold;		
		
		return new_y;		
	}
	
	public void update(GameObjectEvents events)
	{		
		//process movement
		if (events.isMoveLeft())
			xSpeed -= power;

		if (events.isMoveRight())
			xSpeed += power;

		if (events.isMoveUp())
			ySpeed -= power;

		if (events.isMoveDown())
			ySpeed += power;
				
		//update pos
		if (friction != 0)
		{
			xSpeed = (xSpeed * friction);
			ySpeed = (ySpeed * friction);
		}
		
		if (gravity != 0)
		{
			ySpeed += gravity;
		}
		
		if (events.isCollisionBottom())
			ySpeed = 0;

		if (events.isCollisionLeft() || events.isCollisionRight())
			xSpeed = 0;
		
		x += xSpeed;
		y += ySpeed;
	}

	//default: 0.5
	public void setPower(float value)
	{
		power = value;
	}
	
	//default: 0.95
	public void setFriction(float value)
	{
		friction = value;
	}	

	//default: 0.1
	public void setGravity(float value)
	{
		gravity = value;
	}	

	public void setX(float value)
	{
		x = value;
	}

	public void setY(float value)
	{
		y = value;
	}
	
	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}
	
	public boolean isZeroSpeedX()
	{
		return (((long)Math.floor(xSpeed)) == 0);
	}

	public boolean isZeroSpeedY()
	{
		return (((long)Math.floor(ySpeed)) == 0);
	}

	public float getSpeedY()
	{
		return ySpeed;
	}

	public void setSpeedY(float value)
	{
		ySpeed = value;
	}

}
