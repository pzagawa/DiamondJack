package pl.pzagawa.game.engine;

public interface GameStatsEvents
{
	void onPlayStart(long levelId);
	void onPlayFinish(long levelId, int score, int totalGates, String completedGate);
	void onPlayerDead(long levelId);
	int getTotalScore(long levelId);
}
