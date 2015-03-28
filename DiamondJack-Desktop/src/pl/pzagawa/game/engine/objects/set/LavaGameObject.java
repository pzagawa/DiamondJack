package pl.pzagawa.game.engine.objects.set;

import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.object.anim.AnimationManager;
import pl.pzagawa.game.engine.object.anim.SimpleAnimation;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.ViewObject;

//static object, that does nothing but renders
public class LavaGameObject
	extends StaticGameObject
{	
	private SimpleAnimation animation;
	
	public LavaGameObject(GameInstance game, TileItem tile, int tileWidth, int tileHeight)
	{
		super(game, tile, tileWidth, tileHeight);
		
		AnimationManager am = new AnimationManager(CommonData.ENV + "lava.png", CommonData.ENV + "lava-frameset.txt");
		
		animation = new SimpleAnimation(am, 0, 0);
	}

	@Override
	public void load()
	{
		super.load();
		
		animation.load();
	}
		
	@Override
	public void dispose()
	{
		animation.dispose();
	}
	
	@Override
	public void update()
	{	
		animation.update(this);
	}
	
	public void checkCollision(BoundingBox box)
	{
	}
	
	@Override
	public boolean isDeadlyObject()
	{
		return true;
	}
	
	@Override
	public void setCollision()
	{
	}	
	
	@Override
	public boolean isTileVisible()
	{
		//do not render source tile
		return false;
	}
	
	@Override
	public void render(Screen screen, ViewObject viewObject)
	{
		animation.render(screen, viewObject, this);
	}
	
}
