package pl.pzagawa.game.engine.objects.enemies;

import com.badlogic.gdx.graphics.Texture;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.Utils;
import pl.pzagawa.game.engine.audio.Sounds;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.EnemyGameObject;
import pl.pzagawa.game.engine.objects.ViewObject;
import pl.pzagawa.game.engine.state.State;

public class SpiderObject
	extends EnemyGameObject
{
	private final static int MIN_PLAYER_ATTACK_DISTANCE = 32 * 2; 

	private final static int WEB_TEXTURE_POS_X = 64;
	private final static int WEB_TEXTURE_WIDTH = 64;
	private final static int WEB_BEGIN_HEIGHT = 26;
	private final static int WEB_SEGMENT_BEGIN = 30;
	private final static int WEB_SEGMENT_HEIGHT = 8;
	
	private final static float START_Y_OFFSET = 16;
	private final static float STOP_Y_OFFSET = 32;
	
	private float startPositionY = 0;
	
	private Texture texture;
	
	private boolean checkDistance = true;
	
	public SpiderObject(GameInstance game, TileItem tile, int tileWidth, int tileHeight)
	{
		super(game, new SpiderStateManager(), tile, tileWidth, tileHeight, "spider.png", "spider-frameset.txt");

		this.startPositionOffsetY = START_Y_OFFSET;
		
		SpiderStateManager stateManager = (SpiderStateManager)state;
		
		stateManager.setStartPositionX(tile.posX);		
	}

	@Override
	public void load()
	{				
		super.load();
		
		String enemyParams = "power:2.0;friction:0.5;gravity:0.0";

		this.setParams(Utils.propertiesFromString(enemyParams));

		state.setState(State.IDLE);
		
		startPositionY = y;
		
		texture = animation.getFrameSet().getTexture();
	}
		
	@Override
	public void update()
	{
		final int objState = state.getState();

		events.clearMovement();
		
		if (objState == State.ATTACK)
		{
			events.clearCollisionTop();
			events.setMoveDown();
		}
		
		if (objState == State.RETREAT)
		{
			events.clearCollisionBottom();
			events.setMoveUp();
		}
		
		super.update();
		
		if (checkDistance)
		{
			if (playerObject.isMinimumDistance(this, MIN_PLAYER_ATTACK_DISTANCE))
			{
				this.state.setState(State.ATTACK);
				Sounds.spider();
				
				checkDistance = false;
			}
		}

		if (objState == State.IDLE)
		{
			checkDistance = true;
		}
	}
	
	@Override
	public void checkCollision(BoundingBox box)
	{
		final float boxRight = box.x + box.width;
		final float boxBottom = box.y + box.height;
		
		//edge cover test
		final boolean coversBoxRightEdge = x < boxRight;			
		final boolean coversBoxLeftEdge = (x + width) > box.x;			
		final boolean coversBoxBottomEdge = y < boxBottom;
		
		//box orientation test
		final boolean isOnBoxRightSide = x > box.x;			
		final boolean isOnBoxLeftSide = (x + width) < boxRight;			
		
		if (isOnBoxRightSide && coversBoxRightEdge)
		{
			if (isOnBoxLeftSide && coversBoxLeftEdge)
			{							
				//test BOTTOM COLLISION: if object has moved down inside ground box
				if (coversBoxBottomEdge && (y >= (box.y - STOP_Y_OFFSET)))
				{
					events.setCollisionBottom();
				}
				
				//test TOP COLLISION: if object has moved up inside ground box
				if (y < startPositionY)
				{
					position.setY(startPositionY);
					events.setCollisionTop();
				}				
				
			}	
		}
	}

	@Override
	public void render(Screen screen, ViewObject viewObject)
	{		
		float viewPosX = viewObject.left();
		float viewPosY = viewObject.top();
		
		int texturePosY = 0;

		float posX = x - viewPosX - (width >> 1);
		float posY = screen.getHeight() - WEB_BEGIN_HEIGHT + viewPosY - tileBox.y;		

		//draw web start
		screen.batch.draw(texture, posX, posY, 
				WEB_TEXTURE_WIDTH, WEB_BEGIN_HEIGHT,
				WEB_TEXTURE_POS_X, texturePosY,
				WEB_TEXTURE_WIDTH, WEB_BEGIN_HEIGHT, false, false);
		
		//draw segments
		texturePosY = WEB_SEGMENT_BEGIN;
		
		final int segmentCount = (int)((y - startPositionY) / WEB_SEGMENT_HEIGHT);
		
		for (int index = 0; index < segmentCount; index++)
		{		
			posY -= WEB_SEGMENT_HEIGHT;
			
			screen.batch.draw(texture, posX, posY,
					WEB_TEXTURE_WIDTH, WEB_SEGMENT_HEIGHT,
					WEB_TEXTURE_POS_X, texturePosY,
					WEB_TEXTURE_WIDTH, WEB_SEGMENT_HEIGHT, false, false);
		}		
		
		super.render(screen, viewObject);
	}
	
	@Override
	public boolean isInView(float viewPosX, float viewPosY, int viewWidth, int viewHeight)
	{
		final int spiderHeight = (int)(y - startPositionY);
		
		//test x
		if (x > (viewPosX - width))
		{
			if (x < (viewPosX + viewWidth + width))
			{
				//test y
				if (y > (viewPosY - spiderHeight))
				{				
					if (y < (viewPosY + viewHeight + spiderHeight))
					{
						return true;
					}
				}				
			}
		}
		
		return false;
	}	
	
}
