package pl.pzagawa.game.engine.objects;

import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.object.anim.AnimationManager;
import pl.pzagawa.game.engine.objects.set.DynamicGameObject;
import pl.pzagawa.game.engine.state.StateManager;

//Coords 0,0 at upper left corner of screen, this is top edge of player box
public abstract class EnemyGameObject
	extends DynamicGameObject
{	
	private final static int TEST_TRESHOLD = 8;
	
	private final static int MARGIN_X = 0;
	private final static int MARGIN_Y = 0;
	
	protected BoundingBox tileBox;
	protected AnimationManager animation;
	
	protected float startPositionOffsetY = 0;
	
	protected PlayerObject playerObject;
	
	public EnemyGameObject(GameInstance game, StateManager stateManager, TileItem tile, 
			int tileWidth, int tileHeight, String textureFileName, String frameSetFileName)
	{
		super(game, stateManager);

		this.tileBox = new BoundingBox(tile, tileWidth, tileHeight);		
		this.animation = new AnimationManager(CommonData.ENEMIES + textureFileName, CommonData.ENEMIES + frameSetFileName);		
	}
	
	@Override
	public void load()
	{
		super.load();
		
		animation.load(MARGIN_X, MARGIN_X, MARGIN_Y, MARGIN_Y);
		
		BoundingBox collisionMargins = animation.getCollisionMargins();
		
		this.state.setFrames(animation.getFrameSetStates());
		
		this.setSize(animation.getCollisionBox());
			
		//move object down to tile bottom position
		float yOffset = (tileBox.height - height) + startPositionOffsetY;
		
		//correct y pos if collision margin set
		if (startPositionOffsetY > 0)
			yOffset -= ((collisionMargins.y + collisionMargins.height));
		
		float xOffset = collisionMargins.x;
		
		this.position.setX(tileBox.x + xOffset);
		this.position.setY(tileBox.y + yOffset);

		x = position.getX();
		y = position.getY();
		
		playerObject = game.getObjects().getPlayerObject();		
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
	public void update()
	{
		//process movement events
		position.update(events);
		
		state.update(this);

		x = position.getX();
		y = position.getY();
		
		animation.update(this);
	}
	
	public void checkCollision(BoundingBox box)
	{	
		final float boxRight = box.x + box.width;
		final float boxBottom = box.y + box.height;
		
		//edge cover test
		final boolean coversBoxRightEdge = x < boxRight;		
		final boolean coversBoxLeftEdge = (x + width) > box.x;
		
		//box cover test
		final boolean coversBoxTop = (y + height) > (box.y + TEST_TRESHOLD);
		final boolean coversBoxBottom = y < (boxBottom - TEST_TRESHOLD);										
		
		//box orientation test
		final boolean isOnBoxRightSide = x > box.x;		
		final boolean isOnBoxLeftSide = (x + width) < boxRight;
		
		//test LEFT COLLISION: if object has moved into box from its right side
		if (coversBoxRightEdge && isOnBoxRightSide)
		{
			if (coversBoxTop && coversBoxBottom) 
			{
				events.setCollisionLeft();
				position.setX(box.x + box.width);
			}
		}
			
		//test RIGHT COLLISION: if object has moved into box from its left side
		if (coversBoxLeftEdge && isOnBoxLeftSide)
		{
			if (coversBoxTop && coversBoxBottom) 
			{			
				events.setCollisionRight();				
				position.setX(box.x - width);
			}
		}	
	}

	public boolean isCollisionTestEnabled()
	{
		return true;
	}
	
}
