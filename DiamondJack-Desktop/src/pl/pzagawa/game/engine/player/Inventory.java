package pl.pzagawa.game.engine.player;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.audio.Sounds;
import pl.pzagawa.game.engine.objects.set.CollectableGameObject;
import pl.pzagawa.game.engine.objects.set.StaticGameObjectType;

public class Inventory
{
	public final static int RUBY_VALUE = 1;
	public final static int DIAMOND_VALUE = 5;

	public final GameInstance game;
	
	//level score
	private int score = 0;
	
	//local level counters
	private int key1Counter = 0;
	private int key2Counter = 0;

	public Inventory(GameInstance game)
	{
		this.game = game;
		
		reset(-1);
	}

	public void reset(long levelId)
	{
		score = 0;
		key1Counter = 0;
		key2Counter = 0;
		
		//process stats
		if (levelId != -1)
		{
			
		}			
	}
	
	public void addItem(CollectableGameObject object)
	{
		switch (object.objectType)
		{
			case StaticGameObjectType.TYPE_KEY1:
				key1Counter++;
				Sounds.objectKey();
				break;
				
			case StaticGameObjectType.TYPE_KEY2:
				key2Counter++;
				Sounds.objectKey();
				break;
				
			case StaticGameObjectType.TYPE_RUBY:
				score += RUBY_VALUE;
				Sounds.objectDiamond();
				break;
				
			case StaticGameObjectType.TYPE_DIAMOND:
				score += DIAMOND_VALUE;
				Sounds.objectDiamond();
				break;
				
			case StaticGameObjectType.TYPE_DIAMOND_GREEN:
				score += (DIAMOND_VALUE + DIAMOND_VALUE);
				Sounds.objectDiamond();
				break;
		}
	}
	
	private boolean hasKey1()
	{
		return (key1Counter > 0);		
	}

	private boolean hasKey2()
	{
		return (key2Counter > 0);		
	}

	public boolean useKey1(int objectType)
	{
		if (objectType == StaticGameObjectType.TYPE_DOOR1)
		{
			if (hasKey1())
			{
				key1Counter--;
				return true;				
			}
		}
		
		return false;
	}

	public boolean useKey2(int objectType)
	{
		if (objectType == StaticGameObjectType.TYPE_DOOR2)
		{
			if (hasKey2())
			{
				key2Counter--;
				return true;		
			}
		}
		
		return false;
	}

	public void playerKill()
	{
		if (GameInstance.gameStatsEvents != null)
			GameInstance.gameStatsEvents.onPlayerDead(game.getLevel().levelId);		
	}

	public int getKey1Count()
	{
		return key1Counter;		
	}

	public int getKey2Count()
	{
		return key2Counter;		
	}
		
	public int getScore()
	{
		return score;
	}

	public int getTotalScore()
	{
		int totalScore = 0;
		
		if (GameInstance.gameStatsEvents != null)
			totalScore = GameInstance.gameStatsEvents.getTotalScore(game.getLevel().levelId);		
		
		return totalScore;
	}
	
}
