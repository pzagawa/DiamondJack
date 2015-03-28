package pl.pzagawa.diamond.jack.gfx;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Renderer;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.LevelData;
import pl.pzagawa.game.engine.objects.set.LevelGate;
import pl.pzagawa.game.engine.objects.set.StaticGameObject;
import pl.pzagawa.game.engine.objects.set.StaticGameObjectList;

public class RendererObjects
	extends Renderer
{
	private StaticGameObjectList staticObjectList;
	private StaticGameObject[] staticObjects;
		
	public RendererObjects(GameInstance game)
	{
		super(game, true);
	}
	
	@Override
	public void onLevelLoaded(LevelData level)
	{
		setTileLayer(level.getObjectsLayer());
		
		staticObjectList = level.getStaticObjects();
		staticObjects = staticObjectList.getObjects();
	}	
	
	@Override
	public void onGameStateChange(int gameState)
	{		
	}
	
	@Override
	public void render(Screen screen, float speed, float shake)
	{
		float viewPosX = viewObject.left() * speed;
		float viewPosY = viewObject.top() * speed;
		int viewWidth = viewObject.getWidth();
		int viewHeight = viewObject.getHeight();
		
		float screenY = screen.getHeight() + viewPosY;
		
		float posX = 0;
		float posY = 0;
		
		screen.batch.begin();
		screen.batch.enableBlending();

		for (StaticGameObject object : staticObjects)
		{
			if (object.isEnabled())
			{
				if (object.isInView(viewPosX, viewPosY, viewWidth, viewHeight))
				{
					posX = (object.left() - viewPosX);
					posY = (screenY - object.top() - tileHeight);										

					if (object.isTileVisible())
						screen.batch.draw(texture, posX + shake, posY + shake, object.tile.srcX, object.tile.srcY, tileWidth, tileHeight);
					
					object.render(screen, viewObject);
				}
			}
		}

		screen.batch.end();
				
		//draw level gates
		if (game.TEST_MODE >= 3)
		{
			LevelGate[] gates = staticObjectList.getLevelGates();
			
			for (LevelGate gate : gates)			
				drawBox(screen, gate.getCollisionBox());
		}
		
	}
	
}
