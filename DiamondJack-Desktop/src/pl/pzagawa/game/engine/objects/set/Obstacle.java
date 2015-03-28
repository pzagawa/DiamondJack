package pl.pzagawa.game.engine.objects.set;

import java.util.ArrayList;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.GameplayFeedback;
import pl.pzagawa.game.engine.audio.Sounds;
import pl.pzagawa.game.engine.objects.GameObject;
import pl.pzagawa.game.engine.player.Inventory;

//object generated in StaticGameObjectList for collision purposes only
public class Obstacle
	extends GameObject
{
	//baseObject - main drawable object of obstacle, for example DoorGameObject 
	private final StaticGameObject baseObject;
	private final Inventory inventory;	
	private ArrayList<StaticGameObject> elements;

	private boolean doorsCollision = false;	
	private boolean openingStarted = false;
	
	public Obstacle(GameInstance game, StaticGameObject object)
	{
		super(game);
		
		this.baseObject = object;
		this.inventory = game.getObjects().getInventory();
		
		this.width = StaticGameObjectType.getObstacleWidth(object.objectType, object.getWidth());
		this.height = StaticGameObjectType.getObstacleHeight(object.objectType, object.getHeight());
		
		this.setPosition(object.left(), object.top() - height + object.getHeight());
	}

	public void updatePosition(float newHeight)
	{
		this.setPosition(this.baseObject.left(), this.baseObject.top() - newHeight + this.baseObject.getHeight());		
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
		if (!enabled)
			return;
		
		for (StaticGameObject object : elements)
			if (object.isEnabled())
				return;
		
		enabled = false;
		
		baseObject.setCollision();
	}
	
	public void setElements(ArrayList<StaticGameObject> elements)
	{
		this.elements = elements;

		this.baseObject.setParent(baseObject);
		
		for (StaticGameObject object : elements)
			object.setParent(baseObject);
		
		if (elements.size() > 0)
		{
			//new obstacle height is a summary of its elements 
			float newHeight = 0; 
			for (StaticGameObject object : elements)
				newHeight += object.height;

			updatePosition(newHeight);
		}
	}
	
	public int getObjectType()
	{
		return baseObject.objectType;
	}
	
	public void setCollision()
	{
		if (!enabled)
			return;
		
		if (doorsCollision)
			return;
				
		if (inventory.useKey1(getObjectType()))
		{
			doorsCollision = true;
		}

		if (inventory.useKey2(getObjectType()))
		{
			doorsCollision = true;
		}
		
		if (doorsCollision)
		{
			game.getScreen().setStateShake();
			
			if (!openingStarted)
			{
				openingStarted = true;
				Sounds.doorOpen();
				
				if (GameInstance.gameplayFeedback != null)
					GameInstance.gameplayFeedback.playEffect(GameplayFeedback.EFFECT_OPEN_DOORS);				
			}			
			
			if (elements != null)
			{
				for (StaticGameObject object : elements)
					object.setCollision();
			}
		}
	}
	
}
