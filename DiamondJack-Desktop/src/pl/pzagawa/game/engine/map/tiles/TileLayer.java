package pl.pzagawa.game.engine.map.tiles;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Texture;

//creates tiles layer from texture and tiles id's list
public class TileLayer
{
	public static final String BACKGROUND = "tilemap-bkg-wrap"; 
	public static final String GROUND = "tilemap-ground"; 
	public static final String SHAPE = "tilemap-shape"; 
	public static final String OBJECTS = "tilemap-objects"; 
	public static final String ENEMIES = "tilemap-enemies"; 

	private final TileTexture tileSet;
	private final TileMap tileMap;
	
	private final String textureFileName;
	private final String tileMapName;
	
	private TileItem[] tileData;
	private boolean includeNullTiles = false;
	
	public TileLayer(String textureFileName, String tileMapName, boolean includeNullTiles)
	{
		this.tileSet = new TileTexture();
		this.tileMap = new TileMap();
		
		this.textureFileName = textureFileName;
		this.tileMapName = tileMapName; 
		
		this.includeNullTiles = includeNullTiles;
	}
	
	public void dispose()
	{
		tileSet.dispose();
	}

	public String getTileMapName()
	{
		return tileMapName;
	}
	
	public void load(String tileMapSize, String tileMapData, long levelId)
	{
		tileMap.parse(tileMapSize, tileMapData);
		
		tileSet.load(textureFileName, tileMap.getTileWidth(), tileMap.getTileHeight());
		
		build();
	}
	
	public Texture getTexture()
	{
		return tileSet.getTexture();
	}
	
	public TileItem[] getTileData()
	{
		return tileData;
	}
	
	public TileItem getTileItemById(int tileId)
	{		
		for (TileItem tileItem : tileData)
			if (tileItem.id == tileId)
				return tileItem;			
		
		return null;
	}
	
	public int getTileWidth()
	{
		return tileMap.getTileWidth();
	}

	public int getTileHeight()
	{
		return tileMap.getTileHeight();
	}

	public int getMapPixelWidth()
	{
		return tileMap.getMapPixelWidth();
	}

	public int getMapPixelHeight()
	{
		return tileMap.getMapPixelHeight();
	}
		
	//builds tile placement map with (0,0) coords in upper left
	private void build()
	{
		int[] tiles = tileMap.getTiles();
		
		final int tileWidth = tileSet.getTileWidth();
		final int tileHeight = tileSet.getTileHeight();
		
		final int mapWidth = tileMap.getMapPixelWidth();

		int posX = 0;
		int posY = 0;
		
		ArrayList<TileItem> tilesList = new ArrayList<TileItem>(); 
		
		for (int index = 0; index < tiles.length; index++)
		{
			final int tileId = tiles[index];
			
			//tileId: 0, is no tile in map
			if (tileId == 0)
			{
				if (includeNullTiles)
					tilesList.add(null);
			} else
			{
				TileItem data = new TileItem();

				data.id = tileId;
	
				data.posX = posX;
				data.posY = posY;
	
				data.srcX = tileSet.getTilePosX(tileId);
				data.srcY = tileSet.getTilePosY(tileId);
	
				tilesList.add(data);
			}
			
			posX += tileWidth;
			
			if (posX >= mapWidth)
			{
				posX = 0;
				posY += tileHeight;
			}			
		}
		
		tileData = tilesList.toArray(new TileItem[tilesList.size()]);
	}
	
}
