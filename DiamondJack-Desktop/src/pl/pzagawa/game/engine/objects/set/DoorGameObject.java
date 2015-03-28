package pl.pzagawa.game.engine.objects.set;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.gfx.effects.Effect;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.ViewObject;

public class DoorGameObject
	extends StaticGameObject
{	
	private final float startTopPos;
	
	private boolean openDoors = false;
	private boolean doorsOpened = false;	
	private BoundingBox startPosition; 
	
	public DoorGameObject(GameInstance game, TileItem tile, int tileWidth, int tileHeight)
	{
		super(game, tile, tileWidth, tileHeight);
		
		this.startTopPos = y;
		this.startPosition = new BoundingBox(this);
		this.startPosition.y += 8;
	}
		
	@Override
	public void update()
	{
		if (!enabled)
			return;
		
		if (doorsOpened)
			return;
				
		//slide door base tile down
		if (openDoors)
		{
			if (y < startTopPos + height)
			{
				y += 4;
			} else {
				doorsOpened = true;
				
				effectsManager.play(startPosition, Effect.DUST);
				
				game.getScreen().setStateNormal();
			}
		}		
	}
	
	@Override
	public void checkCollision(BoundingBox box)
	{
		return;
	}
	
	@Override
	public void setCollision()
	{
		openDoors = true;		
	}	
	
	@Override
	public void render(Screen screen, ViewObject viewObject)
	{
	}
	
}
