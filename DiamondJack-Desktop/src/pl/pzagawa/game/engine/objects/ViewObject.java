package pl.pzagawa.game.engine.objects;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.map.LevelData;

public class ViewObject
	extends GameObject
{
	private int roomLeft = 0;
	private int roomTop = 0;
	
	public ViewObject(GameInstance game)
	{
		super(game);
	}
	
	@Override
	public void reset()
	{
	}

	@Override
	public void dispose()
	{
	}

	@Override
	protected void update()
	{
	}

	private void correctViewToMapBoundary()
	{
		LevelData level = game.getLevel();
		
		int mapPixelWidth = level.getMapPixelWidth();
		int mapPixelHeight = level.getMapPixelHeight();
		
		//disable view going outside the map boundary
		if (x >= mapPixelWidth - roomWidth)
			x = mapPixelWidth - roomWidth;				
		
		//disable view going outside the map boundary
		if (x <= roomLeft)
			x = roomLeft;
		
		//disable view going outside the map boundary
		if (y >= mapPixelHeight - roomHeight)
			y = mapPixelHeight - roomHeight;
		
		//disable view going outside the map boundary
		if (y <= roomTop)
			y = roomTop;		
	}
	
	public void setToObject(GameObject target)
	{
		x = (target.width >> 1) - (getWidth() >> 1);
		y = (target.height >> 1) - (getHeight() >> 1);
		
		correctViewToMapBoundary();
	}
	
	public void followObject(GameObject target)
	{		
		float targetX = target.getCenterX() - this.getCenterX();
		float targetY = target.getCenterY() - this.getCenterY();
				
		float speedX = (Math.abs(targetX) * 0.01f) * 6.0f;
		float speedY = (Math.abs(targetY) * 0.01f) * 6.0f;
		
		if (targetX > 0)
			x += speedX;

		if (targetX < 0)
			x -= speedX;

		if (targetY > 0)
			y += speedY;

		if (targetY < 0)
			y -= speedY;

		correctViewToMapBoundary();
	}
	
}
