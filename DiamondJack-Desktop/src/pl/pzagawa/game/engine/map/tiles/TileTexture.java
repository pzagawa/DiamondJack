package pl.pzagawa.game.engine.map.tiles;

import pl.pzagawa.game.engine.EngineException;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

//loads texture with tiles
public class TileTexture
{
	private FileHandle fileHandle = null;
	private Texture texture = null;
	private int tileWidth = 0;
	private int tileHeight = 0;

	public TileTexture()
	{
	}
	
	public void dispose()
	{
		texture.dispose();
	}	
		
	public void load(String fileName, int tileWidth, int tileHeight)
	{
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		try
		{
			fileHandle = Gdx.files.getFileHandle(fileName, FileType.Internal);
			texture = new Texture(fileHandle, false);			
		} catch (Exception e)
		{
			throw new EngineException("Error loading texture: " + fileName + ", " + e.getMessage());
		}
	}
	
	public Texture getTexture()
	{
		return texture;
	}

	public int getTileCountX()
	{
		return (texture.getWidth() / tileWidth);
	}

	public int getTileCountY()
	{
		return (texture.getHeight() / tileHeight);
	}
	
	public int getTileCount()
	{
		return getTileCountX() * getTileCountY();	
	}
	
	public int getTileWidth()
	{
		return tileWidth;
	}

	public int getTileHeight()
	{
		return tileHeight;		
	}
	
	public int getTilePosX(int tileId)
	{
		//because tileId starts from 1, and texture position starts at 0
		return ((tileId - 1) % getTileCountX()) * tileWidth;
	}

	public int getTilePosY(int tileId)
	{
		//because tileId starts from 1, and texture position starts at 0
		return ((tileId - 1) / getTileCountX()) * tileHeight;
	}

}
