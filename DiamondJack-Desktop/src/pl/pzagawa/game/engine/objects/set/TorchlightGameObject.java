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
public class TorchlightGameObject
	extends StaticGameObject
{	
	private SimpleAnimation animation;
	
	public TorchlightGameObject(GameInstance game, TileItem tile, int tileWidth, int tileHeight, int xOffset)
	{
		super(game, tile, tileWidth, tileHeight);
		
		AnimationManager am = new AnimationManager(CommonData.ENV + "torchlight.png", CommonData.ENV + "torchlight-frameset.txt");
		
		animation = new SimpleAnimation(am, xOffset, -7);
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
	public void setCollision()
	{
	}	
	
	@Override
	public void render(Screen screen, ViewObject viewObject)
	{
		animation.render(screen, viewObject, this);
	}
	
}
