package pl.pzagawa.game.engine.objects.set;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.gfx.effects.Effect;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.ViewObject;
import pl.pzagawa.game.engine.player.Inventory;

public class CollectableGameObject
	extends StaticGameObject
{
	private final int collisionMargin;
	private final Inventory inventory;
	private BoundingBox collisionBox = new BoundingBox();
	
	private final int maxJumpHeight;
	private int currentJumpStep = (int)(System.nanoTime() % 16);
	private boolean directionUp = true;
	
	public CollectableGameObject(GameInstance game, TileItem tile, int tileWidth, int tileHeight)
	{
		super(game, tile, tileWidth, tileHeight);

		this.inventory = game.getObjects().getInventory();
		
		this.maxJumpHeight = tileHeight >> 2;		
		
		this.collisionMargin = tileWidth >> 2;
		
		this.collisionBox.setPosition(x + collisionMargin, y + collisionMargin);
		this.collisionBox.setSize(tileWidth - (collisionMargin * 2), tileHeight - (collisionMargin * 2));
	}
		
	@Override
	public void update()
	{		
		if (!enabled)
			return;
		
		//clamp result to integer, skip too small changes
		int speed = (int)Math.pow(1.3f, currentJumpStep * 0.5);

		//process jump move
		y = tile.posY + speed;
		
		if (directionUp)
		{
			if ((y - tile.posY) >= maxJumpHeight)
			{
				directionUp = false;
			} else {
				currentJumpStep++;
			}
		} else {
			if (tile.posY >= y)
			{
				directionUp = true;
			} else {
				currentJumpStep--;
			}			
		}
		
		this.collisionBox.setPosition(x + collisionMargin, y + collisionMargin);
	}
	
	public void checkCollision(BoundingBox box)
	{	
		if (!enabled)
			return;
		
		if (collisionBox.isCollision(box))
		{
			enabled = false;

			inventory.addItem(this);

			effectsManager.play(this, Effect.STARS);
		}
	}

	@Override
	public void render(Screen screen, ViewObject viewObject)
	{
	}
	
}
