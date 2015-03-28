package pl.pzagawa.game.engine.objects;

import pl.pzagawa.game.engine.EngineException;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.map.LevelData;
import pl.pzagawa.game.engine.objects.enemies.EnemyGameObjectList;
import pl.pzagawa.game.engine.objects.set.LevelGate;
import pl.pzagawa.game.engine.objects.set.Obstacle;
import pl.pzagawa.game.engine.objects.set.StaticGameObject;
import pl.pzagawa.game.engine.objects.set.StaticGameObjectList;
import pl.pzagawa.game.engine.shape.GroundShape;
import pl.pzagawa.game.engine.shape.TileShapes;

public class CollisionManager
	implements LevelData.Events
{
	protected final GameInstance game;
	
	private GroundShape groundShape = null;
	private BoundingBox[] groundBoxes = null;
	
	public CollisionManager(GameInstance game)
	{
		this.game = game;
	}

	@Override
	public void onLevelLoaded(LevelData level)
	{
		groundShape = level.getGroundShape();
		groundBoxes = groundShape.getBoundingBoxes(); 
	}

	public void update(PlayerObject playerObject, StaticGameObjectList staticObjects, EnemyGameObjectList enemyObjects)
	{
		if (playerObject == null)
			throw new EngineException("Unable to make collision test. PlayerObject is null");

		if (groundShape == null)
			throw new EngineException("Unable to make collision test. GroundShape is null");
		
		if (staticObjects == null)
			throw new EngineException("Unable to make collision test. StaticGameObjectList is null");

		playerObject.updateProjectedBox();		

		final BoundingBox playerBoxHead = playerObject.getProjectedBoxHead();
		final StaticGameObject[] objects = staticObjects.getObjects();
		final Obstacle[] obstacles = staticObjects.getObstacles();
		final LevelGate[] levelGates = staticObjects.getLevelGates();
		final EnemyGameObject[] enemies = enemyObjects.getObjects();
		
		//check objects with ground shape collision
		for (BoundingBox groundBox : groundBoxes)
		{
			if (groundBox.tileShape == TileShapes.BOX)
			{
				playerObject.checkCollision(groundBox.tileShape, groundBox);
				enemyObjects.checkCollision(groundBox);
			}
			else if (groundBox.tileShape == TileShapes.BOUNCER)
			{
				enemyObjects.checkCollision(groundBox);				
			}
			else if (groundBox.tileShape == TileShapes.SHELF)
			{
				playerObject.checkCollision(groundBox.tileShape, groundBox);
				enemyObjects.checkCollision(groundBox);				
			}
		}
		
		//check player with static objects collision
		for (StaticGameObject object : objects)
		{
			object.checkCollision(playerBoxHead);
			
			if (object.isDeadlyObject())
			{
				if (!object.isCollisionWithPlayer())
				{					
					if (playerObject.checkObjectCollision(object))
					{						
						object.setCollisionWithPlayer();						
						playerObject.events.setDead();
						playerObject.startVanishing();
						break;
					}
				}
			}
		}

		//check player with level gates collision
		for (LevelGate gate : levelGates)
			playerObject.checkLevelGateCollision(gate);
		
		//check player with obstacles collision
		for (Obstacle obstacle : obstacles)
			if (obstacle.isEnabled())
				if (playerObject.checkObstacleCollision(obstacle))
					obstacle.setCollision();
		
		//check enemies with obstacles collision		
		for (Obstacle obstacle : obstacles)
			if (obstacle.isEnabled())
				enemyObjects.checkCollision(obstacle);		
				
		//check player with enemies collision
		for (EnemyGameObject enemy : enemies)
		{
			if (!enemy.isCollisionWithPlayer())
			{
				if (playerObject.checkEnemyCollision(enemy))
				{	
					enemy.setCollisionWithPlayer();
					playerObject.events.setDead();
					break;
				}
			}
		}
		
	}
	
}
