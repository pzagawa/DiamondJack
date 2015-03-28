package pl.pzagawa.game.engine.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds
{
	private static Sound player_walk;
	private static Sound player_drop;
	private static Sound player_scream;
	private static Sound player_fall;

	private static Sound object_key;
	private static Sound object_diamond;
	
	private static Sound door_open;
	private static Sound level_start;
	
	private static Sound skeleton_alert;
	private static Sound skeleton_attack;
	private static Sound skeleton_attack_arms;

	private static Sound mummy;
	private static Sound snake;
	private static Sound scorpion;
	private static Sound spider;
	
	public static void load()
	{
		player_walk = loadSound("player_walk.wav");
		player_drop = loadSound("player_drop.wav");
		player_scream = loadSound("player_scream.wav");
		player_fall = loadSound("player_fall.wav");
		
		object_key = loadSound("object_key.wav");		
		object_diamond = loadSound("object_diamond.wav");
		
		door_open = loadSound("door_open.wav");
		level_start = loadSound("level_start.wav");
		
		skeleton_alert = loadSound("skeleton_alert.wav"); 
		skeleton_attack = loadSound("skeleton_attack.wav");		
		skeleton_attack_arms = loadSound("skeleton_attack_arms.wav");
		
		mummy = loadSound("mummy.wav");
		snake = loadSound("snake.wav");
		scorpion = loadSound("scorpion.wav");
		spider = loadSound("spider.wav");
	}
	
	public static void dispose()
	{
		player_walk.dispose();
		player_drop.dispose();
		player_scream.dispose();
		player_fall.dispose();
		
		object_key.dispose();
		object_diamond.dispose();
		door_open.dispose();
		level_start.dispose();
		
		skeleton_alert.dispose();
		skeleton_attack.dispose();
		skeleton_attack_arms.dispose();
		
		mummy.dispose();
		snake.dispose();
		scorpion.dispose();
		spider.dispose();
	}

	private static Sound loadSound(String name)
	{
		return Gdx.audio.newSound(Gdx.files.internal("data/sounds/" + name));		
	}

	public static void playerWalk()
	{
		player_walk.stop();
		player_walk.play(0.7f);
	}
	  
	public static void playerDrop()
	{
		player_drop.stop();
		player_drop.play(1f);
	}  

	public static void playerScream()
	{
		player_scream.play(0.6f);
	}  

	public static void playerFall()
	{
		player_fall.play(1f);
	}  

	public static void objectKey()
	{
		object_key.play(1f);
	}

	public static void objectDiamond()
	{
		object_diamond.play(1f);
	}
	
	public static void doorOpen()
	{
		door_open.stop();
		door_open.play(1f);
	}
	
	public static void levelStart()
	{
		level_start.stop();
		level_start.play(1f);
	}
	
	public static void skeletonAlert()
	{
		skeleton_alert.play(1f);		
	}
	
	public static void skeletonAttack()
	{
		skeleton_attack.play(1f);		
	}
	
	public static void skeletonAttackArms()
	{
		skeleton_attack_arms.play(1f);		
	}
	
	public static void mummyGrowl()
	{
		mummy.stop();
		mummy.play(0.7f);		
	}
	
	public static void snake()
	{
		snake.stop();
		snake.play(0.3f);
	}
	
	public static void scorpion()
	{
		scorpion.stop();
		scorpion.play(0.3f);
	}
	
	public static void spider()
	{
		spider.stop();
		spider.play(0.5f);
	}
	
}
