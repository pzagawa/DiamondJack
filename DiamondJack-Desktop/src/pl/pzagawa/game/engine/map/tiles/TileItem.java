package pl.pzagawa.game.engine.map.tiles;

public class TileItem
{
	//tiles mutual orientation
	public final static int ORIENTATION_UNDEFINED = 0;
	public final static int ORIENTATION_HORZ = 1;
	public final static int ORIENTATION_VERT = 2;
	
	//tile guid from Tiled
	public int id = 0;
	
	//tile pixel position in map
	public int posX = 0;
	public int posY = 0;

	//texel position
	public int srcX = 0;
	public int srcY = 0;
	
	@Override
	public String toString()
	{
		return Integer.toString(id) + ": " + Integer.toString(posX) + "," + Integer.toString(posY); 
	}

	public boolean isInView(float viewPosX, float viewPosY, int viewWidth, int viewHeight, int tileWidth, int tileHeight)
	{
		//test x
		if (posX > (viewPosX - tileWidth))
		{
			if (posX < (viewPosX + viewWidth + tileWidth))
			{
				//test y
				if (posY > (viewPosY - tileHeight))
				{				
					if (posY < (viewPosY + viewHeight + tileHeight))
					{
						return true;
					}
				}				
			}
		}
		
		return false;
	}

	public int getReferenceOrientation(TileItem tileItem)
	{
		if (tileItem != null)
		{		
			if (tileItem.posX == this.posX)
				if (tileItem.posY != this.posY)
					return ORIENTATION_VERT;
			
			if (tileItem.posY == this.posY)
				if (tileItem.posX != this.posX)
					return ORIENTATION_HORZ;
		}
		
		return ORIENTATION_UNDEFINED;
	}
	
}
