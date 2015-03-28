package pl.pzagawa.game.engine;

public interface GameplayFeedback
{
	public static int EFFECT_PLAYER_DROP = 0;
	public static int EFFECT_OPEN_DOORS = 1;
	public static int EFFECT_PLAYER_FALL = 2;
	
	void playEffect(int effectType);
}
