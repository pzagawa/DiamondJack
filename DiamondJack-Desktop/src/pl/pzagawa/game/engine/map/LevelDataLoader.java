package pl.pzagawa.game.engine.map;

public interface LevelDataLoader
{
	String getMapSetup(long mapId);
	String getLevelSetup(long levelId);
	String getLevelData(long levelId, String name);
}
