package pl.pzagawa.game.engine.objects;

import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.object.anim.AnimationManager;
import pl.pzagawa.game.engine.objects.set.DynamicGameObject;
import pl.pzagawa.game.engine.objects.set.LevelGate;
import pl.pzagawa.game.engine.objects.set.Obstacle;
import pl.pzagawa.game.engine.shape.TileShapes;
import pl.pzagawa.game.engine.state.State;

//Coords 0,0 at upper left corner of screen, this is top edge of player box
public class PlayerObject
	extends DynamicGameObject
{	
	private final static int TEST_TRESHOLD = 2;
		
	private final static int MARGIN_X = 1;
	private final static int MARGIN_Y = 0;
	
	private AnimationManager animation;
	
	private BoundingBox projectedBox = new BoundingBox();
	private BoundingBox projectedBoxHead = new BoundingBox();
	
	private PlayerDeathStatus playerDeath = new PlayerDeathStatus();
	
	private boolean isVanishing = false;
	
	public PlayerObject(GameInstance game)
	{
		super(game, new PlayerStateManager());
		
		this.animation = new AnimationManager(CommonData.GFX + "player.png", CommonData.GFX + "player-frameset.txt");
	}
	
	@Override
	public void load()
	{
		super.load();

		playerDeath.reset(game);
		
		animation.load(MARGIN_X, MARGIN_X, MARGIN_Y, MARGIN_Y);
		
		this.state.setFrames(animation.getFrameSetStates());

		this.setSize(animation.getCollisionBox());
		
		state.setState(State.IDLE);
		
		this.isVanishing = false;
	}
	
	@Override
	public void dispose()
	{
		animation.dispose();
	}
	
	@Override
	public void render(Screen screen, ViewObject viewObject)
	{
		animation.render(screen, viewObject, x, y);
	}	
	
	@Override
	protected void goLeft()
	{	
		events.setMoveLeft();
	}
	
	@Override
	protected void goRight()
	{	
		events.setMoveRight();
	}

	protected void doAction(boolean isHeld)
	{	
		if (!isHeld)
			events.setAction();
	}
	
	@Override
	public void update()
	{
		if (state.getState() == State.DEAD)
		{
			events.clearMovement();
		}
		
		//process movement events
		position.update(events);
		
		state.update(this);
								
		jump.update(this);

		x = position.getX();
		y = position.getY();
		
		animation.update(this);
	}

	public void updateProjectedBox()
	{
		this.setSize(animation.getCollisionBox());
		
		float projX = position.getProjectedX(events, TEST_TRESHOLD);
		float projY = position.getProjectedY(events, TEST_TRESHOLD);
		
		projectedBox.setPosition(projX, projY);
		projectedBox.setSize(width, height);
	}
	
	public BoundingBox getProjectedBox()
	{
		return projectedBox;
	}
	
	public BoundingBox getProjectedBoxHead()
	{
		final int halfWidth = width >> 1;
		final int halfHeight = height >> 1;
		final int margin = halfWidth >> 1;
		
		projectedBoxHead.setSize(halfWidth, halfHeight);
		projectedBoxHead.setPosition(x + margin, y + margin);

		projectedBoxHead.height = (int)Math.abs((projectedBoxHead.top() - bottom()));  

		return projectedBoxHead;
	}
	
	public void checkCollision(int tileShape, BoundingBox box)
	{	
		if (projectedBox.isCollision(box))
		{
			events.setCollisionGround();
			
			final float boxRight = box.x + box.width;
			final float boxBottom = box.y + box.height;
			
			//edge cover test
			final boolean coversBoxRightEdge = projectedBox.x < boxRight;			
			final boolean coversBoxLeftEdge = (projectedBox.x + width) > box.x;			
			final boolean coversBoxTopEdge = (projectedBox.y + height) > box.y;			
			final boolean coversBoxBottomEdge = projectedBox.y < boxBottom;
			
			//box orientation test
			final boolean isOnBoxRightSide = projectedBox.x > box.x;			
			final boolean isOnBoxLeftSide = (projectedBox.x + width) < boxRight;			
			final boolean isOnBoxTopSide = (projectedBox.y + height) > boxBottom;
			final boolean isOnBoxBottomSide = projectedBox.y > box.y;
			
			//test LEFT MAP BOUNDARY
			if (!events.isCollisionLeft())
			{
				if (projectedBox.x < 0)
				{
					events.setCollisionLeft();
				}					
			}			

			//test RIGHT MAP BOUNDARY
			if (!events.isCollisionRight())
			{
				if ((projectedBox.x + width) > mapPixelWidth)
				{
					events.setCollisionRight();
				}					
			}			
			
			//test LEFT COLLISION: if player has moved into box from its right side
			if (tileShape != TileShapes.SHELF)
			{
				if (!events.isCollisionLeft())
				{
					if (coversBoxRightEdge && isOnBoxRightSide)
					{
						//test if player is on the left side of the box					
						if (isOnBoxTopSide || isOnBoxBottomSide)
						{
							events.setCollisionLeft();
	
							//correct x position to box right edge
							if (state.getState() != State.JUMP)
								position.setX(box.x + box.width);
						}
					}
				}
			}
			
			//test RIGHT COLLISION: if player has moved into box from its left side
			if (tileShape != TileShapes.SHELF)
			{
				if (!events.isCollisionRight())
				{
					if (coversBoxLeftEdge && isOnBoxLeftSide)
					{
						//test if player is on the right side of the box
						if (isOnBoxTopSide || isOnBoxBottomSide)
						{
							events.setCollisionRight();
		
							//correct x position to box left edge
							if (state.getState() != State.JUMP)
								position.setX(box.x - width);
						}
					}
				}
			}

			//vertical test
			final float testBoxTreshold = (box.height >> 2);

			final boolean coversBoxTop = (projectedBox.y + height) < (box.y + testBoxTreshold);
			final boolean coversBoxBottom = projectedBox.y > (box.y + testBoxTreshold);
			
			
			//test BOTTOM COLLISION: if player has moved down inside ground box
			if (!events.isCollisionBottom())
			{				
				if (coversBoxTopEdge && coversBoxTop)
				{
					events.setCollisionBottom();

					//correct Y position to box top
					position.setY(box.y - height);
				}
			}
			
			//test TOP COLLISION: if player has moved up inside ground box
			if (tileShape != TileShapes.SHELF)
			{			
				if (!events.isCollisionTop())
				{
					if (coversBoxBottomEdge && coversBoxBottom)
					{
						events.setCollisionTop();
					}
				}
			}
			
			//#fix for JUMP state
			//clear BOTTOM collision to avoid jumping on blocks stacked one on another
			//first, test if box is not obstacle
			if (box.height <= box.width)
			{
				//not obstacle, just simple ground shape tile
				if (state.getState() == State.JUMP)
				{
					if (events.isCollisionLeft())
						if (x >= (box.x + box.width))
						{
							events.clearCollisionBottom();
							events.clearCollisionTop();
						}
	
					if (events.isCollisionRight())
						if ((x + width) <= box.x)
						{
							events.clearCollisionBottom();
							events.clearCollisionTop();
						}
				}
			}
			
		}
		
	}

	public boolean checkObstacleCollision(Obstacle obstacle)
	{	
		if (projectedBox.isCollision(obstacle))
		{			
			final float boxRight = obstacle.x + obstacle.width;
			
			//edge cover test
			final boolean coversBoxRightEdge = projectedBox.x < boxRight;			
			final boolean coversBoxLeftEdge = (projectedBox.x + width) > obstacle.x;
						
			//box orientation test
			final boolean isOnBoxRightSide = projectedBox.x > obstacle.x;			
			final boolean isOnBoxLeftSide = (projectedBox.x + width) < boxRight;						
			
			//test LEFT COLLISION: if player has moved into box from its right side
			if (!events.isCollisionLeft())
			{
				if (coversBoxRightEdge && isOnBoxRightSide)
				{
					events.setCollisionLeft();
					return true;
				}
			}
			
			//test RIGHT COLLISION: if player has moved into box from its left side
			if (!events.isCollisionRight())
			{
				if (coversBoxLeftEdge && isOnBoxLeftSide)
				{
					events.setCollisionRight();
					return true;
				}
			}
			
		}
		
		return false;
	}

	public boolean checkEnemyCollision(EnemyGameObject enemy)
	{	
		if (enemy.isCollisionTestEnabled())
			if (isCollision(enemy))
				return true;

		return false;
	}

	public boolean checkObjectCollision(GameObject object)
	{	
		if (isCollision(object))
			return true;

		return false;
	}
	
	public void startVanishing()
	{
		this.isVanishing = true;		
	}	
	
	public boolean isVanishing()
	{
		return this.isVanishing;		
	}	
	
	public void udpateDeadStatus()
	{
		playerDeath.udpate(this);
	}
	
	public boolean isPlayerDead()
	{
		return playerDeath.isPlayerDead(this);
	}
	
	public void checkLevelGateCollision(LevelGate gate)
	{
		if (!gate.enabled)
			return;

		if (projectedBox.isCollision(gate.getCollisionBox()))
		{
			if (gate.isCollision())
				return;

			gate.enabled = false;

			game.goToNewLevelGate(gate);

		} else {
			gate.clearCollision();
		}
	}

	public void setPositionAtGate(LevelGate gate)
	{
		float playerX = gate.getCenterX() - (width >> 1);
		float playerY = gate.getCenterY() - (height >> 1);			

		this.position.setX(playerX);
		this.position.setY(playerY);

		this.x = playerX;
		this.y = playerY;
		
		game.getObjects().getViewObject().setToObject(this);
	}
	
}
