package pl.pzagawa.game.engine.objects;

import java.util.Map;
import com.badlogic.gdx.math.Vector2;
import pl.pzagawa.game.engine.GameInstance;

public abstract class GameObject
	extends BoundingBox
{
	public GameInstance game;
	
	protected int roomWidth = 0;
	protected int roomHeight = 0;
	protected boolean enabled = true;	
	
	private boolean isCollisionWithPlayer = false;
		
	public GameObject(GameInstance game)
	{
		this.game = game;
  	
  	this.roomWidth = game.roomWidth();
  	this.roomHeight = game.roomHeight();  	
	}
	
	@Override
	public void setParams(Map<String, String> map)
	{
		super.setParams(map);
	}
		
	public void setRoom(Vector2 roomPos)
	{
		x = (int)roomPos.x * roomWidth;
		y = (int)roomPos.y * roomHeight;
	}

	public Vector2 getRoom()
	{
		return new Vector2(getRoomPosX(), getRoomPosY());
	}
	
	public int getRoomPosX()
	{
		return (int)(x / roomWidth);
	}

	public int getRoomPosY()
	{
		return (int)(y / roomHeight);
	}
	
	public long getRoomId()
	{
		return (getRoomPosX() * 1000) + getRoomPosY();
	}
	
	public boolean isInView(float viewPosX, float viewPosY, int viewWidth, int viewHeight)
	{
		//test x
		if (x > (viewPosX - width))
		{
			if (x < (viewPosX + viewWidth + width))
			{
				//test y
				if (y > (viewPosY - height))
				{				
					if (y < (viewPosY + viewHeight + height))
					{
						return true;
					}
				}				
			}
		}
		
		return false;
	}
	
	public boolean isMinimumDistance(GameObject object, int minimumDistance)
	{
		return (distance(object) < minimumDistance);
	}	
	
	public abstract void reset();
	
	public void load()
	{
		reset();
				
		isCollisionWithPlayer = false;		
	}	

	public boolean isCollisionWithPlayer()
	{
		return isCollisionWithPlayer;
	}

	public void setCollisionWithPlayer()
	{
		this.isCollisionWithPlayer = true;
	}
	
	public boolean isDeadlyObject()
	{
		return false;
	}
	
	public abstract void dispose();
	
	protected abstract void update();
	
	protected void goLeft()
	{		
	}
	
	protected void goRight()
	{		
	}

	protected void goUp()
	{		
	}

	protected void goDown()
	{		
	}

	protected void doAction(boolean isHeld)
	{		
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}

}
