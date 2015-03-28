package pl.pzagawa.game.engine.objects.set;

import java.util.ArrayList;
import pl.pzagawa.game.engine.GameInstance;

public class ObstacleList
{
	private ArrayList<Obstacle> obstaclesList;  
	private Obstacle[] obstacles;

	public ObstacleList()
	{
		obstaclesList = new ArrayList<Obstacle>();  		
	}
	
	public void add(GameInstance game, StaticGameObject object)
	{
		obstaclesList.add(new Obstacle(game, object));
	}
	
	public void build(StaticGameObjectList objects)
	{
		final int size = obstaclesList.size();
		
		obstacles = new Obstacle[size];

		for (int index = 0; index < size; index++)
			obstacles[index] = obstaclesList.get(index);
		
		groupObjectsInsideObstacles(objects);
	}
	
	private void groupObjectsInsideObstacles(StaticGameObjectList objects)
	{
		for (Obstacle obstacle : obstacles)
		{
			final ArrayList<StaticGameObject> elements = objects.getObjectsInside(obstacle);
			
			obstacle.setElements(elements);
		}
	}	

	public Obstacle[] getObstacles()
	{
		return obstacles;
	}
	
}
