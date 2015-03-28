package pl.pzagawa.diamond.jack.gfx;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Renderer;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.LevelData;
import pl.pzagawa.game.engine.objects.EnemyGameObject;
import pl.pzagawa.game.engine.objects.enemies.EnemyGameObjectList;

public class RendererEnemies
	extends Renderer
{
	private EnemyGameObject[] enemyObjects;
	
	public RendererEnemies(GameInstance game)
	{
		super(game, true);
	}
	
	@Override
	public void onLevelLoaded(LevelData level)
	{
		EnemyGameObjectList enemyObjectList = level.getEnemyObjects();		
		enemyObjects = enemyObjectList.getObjects();		
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
		
		screen.batch.begin();
		screen.batch.enableBlending();

		for (EnemyGameObject object : enemyObjects)
		{
			if (object.isEnabled())
			{
				if (object.isInView(viewPosX, viewPosY, viewWidth, viewHeight))
				{													
					object.render(screen, viewObject);
				}
			}
		}

		screen.batch.end();

		if (game.TEST_MODE >= 1)
		{
			for (EnemyGameObject object : enemyObjects)
				drawObject(screen, object);
		}		
	}
	
}
