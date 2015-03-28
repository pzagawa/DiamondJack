package pl.pzagawa.game.engine.objects;

public class GameObjectEvents
{
	//movement events
	private boolean moveLeft = false;
	private boolean moveRight = false;
	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean action = false;

	//collision events
	private boolean isGroundCollision = false;
	private boolean isCollisionBottom = false;
	private boolean isCollisionTop = false;
	private boolean isCollisionLeft = false;
	private boolean isCollisionRight = false;
	
	//misc events
	private boolean isDead = false;
	
	public void clearAll()
	{
		moveLeft = false;
		moveRight = false;
		moveUp = false;
		moveDown = false;
		action = false;

		isGroundCollision = false;		
		isCollisionBottom = false;
		isCollisionTop = false;
		isCollisionLeft = false;
		isCollisionRight = false;
		
		isDead = false;
	}

	public void clearMovement()
	{
		moveLeft = false;
		moveRight = false;
		moveUp = false;
		moveDown = false;
		action = false;
	}
	
	public void setMoveLeft()
	{
		moveLeft = true;
		moveRight = false;
	}

	public void setMoveRight()
	{
		moveRight = true;
		moveLeft = false;
	}

	public void setMoveUp()
	{
		moveUp = true;
		moveDown = false;
	}

	public void setMoveDown()
	{
		moveDown = true;
		moveUp = false;
	}

	public void setAction()
	{
		action = true;
	}
	
	public void clearAction()
	{
		action = false;
	}	

	public void setCollisionGround()
	{
		isGroundCollision = true;
	}

	public void setCollisionBottom()
	{
		isCollisionBottom = true;
	}
	
	public void setDead()
	{
		isDead = true;
	}

	public void clearCollisionGround()
	{
		isGroundCollision = false;
	}
	
	public void clearCollisionLeft()
	{
		isCollisionLeft = false;
	}

	public void clearCollisionRight()
	{
		isCollisionRight = false;
	}
	
	public void clearCollisionTop()
	{
		isCollisionTop = false;
	}
	
	public void clearCollisionBottom()
	{
		isCollisionBottom = false;
	}
	
	public void setCollisionTop()
	{
		isCollisionTop = true;
	}
	
	public void setCollisionLeft()
	{
		isCollisionLeft = true;
	}

	public void setCollisionRight()
	{
		isCollisionRight = true;
	}
	
	public boolean isMoveLeft()
	{
		return moveLeft;
	}

	public boolean isMoveRight()
	{
		return moveRight;
	}

	public boolean isMoveUp()
	{
		return moveUp;
	}

	public boolean isMoveDown()
	{
		return moveDown;
	}

	public boolean isAction()
	{
		return action;
	}

	public boolean isGroundCollision()
	{
		return isGroundCollision;
	}

	public boolean isCollisionBottom()
	{
		return isCollisionBottom;
	}	

	public boolean isCollisionTop()
	{
		return isCollisionTop;
	}	

	public boolean isCollisionLeft()
	{
		return isCollisionLeft;
	}

	public boolean isCollisionRight()
	{
		return isCollisionRight;
	}
	
	public boolean isCollision()
	{
		return isCollisionLeft || isCollisionRight || isCollisionTop || isCollisionBottom;
	}
	
	public boolean isDead()
	{
		return isDead;
	}

}
