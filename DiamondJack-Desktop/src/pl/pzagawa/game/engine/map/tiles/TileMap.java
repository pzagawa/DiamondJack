package pl.pzagawa.game.engine.map.tiles;

//loads tile id's data
public class TileMap
{
	private int tilesCountX = 0;
	private int tilesCountY = 0;
	private final int tileWidth = 32;
	private final int tileHeight = 32;
	private int[] tiles = null;
	
	public TileMap()
	{
	}
		
	public void parse(String tileMapSize, String tileMapData)
	{
		String[] mapSize = tileMapSize.split("\\*");
				
		tilesCountX = Integer.parseInt(mapSize[0]); 
		tilesCountY = Integer.parseInt(mapSize[1]);
		
		final int tileCount = tilesCountX * tilesCountY;

		String[] tilesList = tileMapData.split(",");

		tiles = new int[tileCount];

		for (int index = 0; index < tileCount; index++)
			tiles[index] = Integer.parseInt(tilesList[index]);				
	}

	public int getTilesCountX()
	{
		return tilesCountX;
	}

	public int getTilesCountY()
	{
		return tilesCountY;
	}
	
	public int getTileWidth()
	{
		return tileWidth;
	}

	public int getTileHeight()
	{
		return tileHeight;
	}
	
	public int[] getTiles()
	{
		return tiles;
	}
	
	public int getMapPixelWidth()
	{
		return tilesCountX * tileWidth;
	}

	public int getMapPixelHeight()
	{
		return tilesCountY * tileHeight;
	}
	
}
