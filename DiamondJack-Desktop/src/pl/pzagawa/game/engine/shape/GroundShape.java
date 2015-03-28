package pl.pzagawa.game.engine.shape;

import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.map.tiles.TileLayer;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.BoundingBoxList;

//creates ground bounding boxes shape from tile map
public class GroundShape
{
	private TileLayer layer;
	private BoundingBox[] boundingBoxes;  
	
	public GroundShape(TileLayer layer)
	{
		this.layer = layer;		
	}
	
	//builds bounding box placement map with (0,0) coords in upper left	
	public void build()
	{
		final BoundingBoxList boxes = new BoundingBoxList(); 
		
		final TileItem[] tiles = layer.getTileData();
		
		final int tileWidth = layer.getTileWidth();
		final int tileHeight = layer.getTileHeight();		
		
		TileItem prevTile = null;
		
		for (TileItem tile : tiles)
		{			
			if (tile == null)
			{
				prevTile = null;
				continue;
			}

			boolean shapeChanged = (prevTile != null && prevTile.id != tile.id);
			
			int tileOrientation = tile.getReferenceOrientation(prevTile);
			
			if (tileOrientation == TileItem.ORIENTATION_UNDEFINED || shapeChanged)
			{
				boxes.addNew(new BoundingBox(tile, tileWidth, tileHeight));
				
			} else {
				BoundingBox box = boxes.getLast();

				if (tileOrientation == TileItem.ORIENTATION_HORZ)
					box.addWidth(tileWidth);

				if (tileOrientation == TileItem.ORIENTATION_VERT)
					box.addHeight(tileHeight);
			}
						
			prevTile = tile;
		}
		
		boundingBoxes = boxes.getArray();
	}
	
	public BoundingBox[] getBoundingBoxes()
	{
		return boundingBoxes;
	}
	
}
