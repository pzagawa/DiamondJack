package pl.pzagawa.game.engine.objects.set;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.gfx.effects.EffectsManager;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.GameObject;
import pl.pzagawa.game.engine.objects.ViewObject;

public abstract class StaticGameObject
	extends GameObject
{
	public final TileItem tile;	
	public final int objectType;
	public final EffectsManager effectsManager;
	
	//parentObject - base obstacle object, for example DoorGameObject 
	protected StaticGameObject parentObject;
	
	public StaticGameObject(GameInstance game, TileItem tile, int sizeWidth, int sizeHeight)
	{
		super(game);
		
		this.tile = tile;
		this.objectType = tile.id;
		this.effectsManager = game.getObjects().getEffectsManager();		
		
		this.setPosition(tile.posX, tile.posY);
		this.setSize(sizeWidth, sizeHeight);
	}
	
	@Override
	public void reset()
	{
	}
		
	@Override
	public void dispose()
	{
	}
	
	public abstract void update();
	
	public abstract void checkCollision(BoundingBox box);
	
	public void setCollision()
	{
	}
	
	public void setParent(StaticGameObject parentObject)
	{
		this.parentObject = parentObject;
	}

	public abstract void render(Screen screen, ViewObject viewObject);
	
	public boolean isTileVisible()
	{
		return true;
	}
	
}
