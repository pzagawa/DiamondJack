package pl.pzagawa.game.engine.objects.set;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.ViewObject;

//static object, that does nothing but renders
public class DoorElementGameObject
	extends StaticGameObject
{	
	private boolean openDoors = false;
	
	public DoorElementGameObject(GameInstance game, TileItem tile, int tileWidth, int tileHeight)
	{
		super(game, tile, tileWidth, tileHeight);
	}
		
	@Override
	public void update()
	{
		if (!enabled)
			return;
	
		//slide door element tile down to parentObject (base)
		if (openDoors)
		{
			if (y < parentObject.top())
			{
				y += 4;
			} else {
				//hide it at the base
				enabled = false;
			}
		}
	}
	
	public void checkCollision(BoundingBox box)
	{	
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
