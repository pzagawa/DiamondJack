package pl.pzagawa.game.engine.objects.set;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.ViewObject;

//static object, that does nothing but renders
public class LevelGateGameObject
	extends StaticGameObject
{	
	public LevelGateGameObject(GameInstance game, TileItem tile, int tileWidth, int tileHeight)
	{
		super(game, tile, tileWidth, tileHeight);
		
		//hide from render
		this.enabled = false;
	}

	@Override
	public void update()
	{
	}
	
	@Override
	public void setCollision()
	{
	}	
	
	@Override
	public void render(Screen screen, ViewObject viewObject)
	{
	}

	public void checkCollision(BoundingBox box)
	{
	}

}
