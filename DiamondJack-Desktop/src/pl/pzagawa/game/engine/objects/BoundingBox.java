package pl.pzagawa.game.engine.objects;

import java.util.Map;

import pl.pzagawa.game.engine.EngineException;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.shape.TileShapes;

public class BoundingBox
{
	public float x = 0;
	public float y = 0;
	public int width = 0;
	public int height = 0;
	
	public final int tileShape;

	public BoundingBox()
	{	
		tileShape = TileShapes.NO_SHAPE;
	}

	public BoundingBox(BoundingBox box)
	{
		this.x = box.x;
		this.y = box.y;
		this.width = box.width;
		this.height = box.height;
		this.tileShape = box.tileShape;
	}
	
	public BoundingBox(float x, float y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tileShape = TileShapes.NO_SHAPE;
	}

	public BoundingBox(TileItem tile, int width, int height)
	{
		this.x = tile.posX;
		this.y = tile.posY;
		this.width = width;
		this.height = height;
		this.tileShape = tile.id;
	}

	public static BoundingBox createFromString(String text, int leftMargin, int rightMargin, int topMargin, int bottomMargin)
	{
		String[] data = text.split(",");
		
		if (data.length != 4)
			throw new EngineException("BoundingBox creation error. String data invalid");
				
		int x = Integer.parseInt(data[0]) + leftMargin;
		int y = Integer.parseInt(data[1]) + topMargin;
		int width = Integer.parseInt(data[2]) - rightMargin;
		int height = Integer.parseInt(data[3]) - bottomMargin;
		
		return new BoundingBox(x, y, width, height);
	}
	
	public void setParams(Map<String, String> map)
	{
		if (map.containsKey("x"))
			x = Integer.parseInt(map.get("x"));

		if (map.containsKey("y"))
			y = Integer.parseInt(map.get("y"));
		
		if (map.containsKey("width"))
			width = Integer.parseInt(map.get("width"));
		
		if (map.containsKey("height"))
			height = Integer.parseInt(map.get("height"));
	}
	
	@Override
	public String toString()
	{
		return String.format("%d,%d-%d,%d", (int)x, (int)y, (int)width, (int)height);
	}
	
	public void setPosition(float x, float y)
	{
		this.x = x;
		this.y = y;		
	}

  public void setSize(int width, int height)
  {
  	this.width = width;
  	this.height = height;
  }

  public void setSize(BoundingBox box)
  {
  	this.width = box.width;
  	this.height = box.height;
  }

  public void set(BoundingBox box)
  {
  	this.x = box.x;
  	this.y = box.y;
  	this.width = box.width;
  	this.height = box.height;
  }

  public void set(BoundingBox box, float marginLeft, float marginTop, float marginRight, float marginBottom)
  {
  	this.x = box.x + marginLeft;
  	this.y = box.y + marginTop;
  	this.width = box.width - (int)(marginRight + marginLeft);
  	this.height = box.height - (int)(marginBottom + marginTop);
  }
  
	public void addWidth(int width)
	{
		this.width += width;
	}	

	public void addHeight(int height)
	{
		this.height += height;
	}	
	
	public float left()
	{
		return x;
	}

	public float top()
	{
		return y;
	}
	
	public float right()
	{
		return x + width;
	}

	public float bottom()
	{
		return y + height;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public float getCenterX()
	{
		return x + (width >> 1);
	}	

	public float getCenterY()
	{
		return y + (height >> 1);
	}	
	
	public boolean isEmpty()
	{
		return (width <= 0) || (height <= 0);
	}
	
	public boolean isCollision(BoundingBox box)
	{
		int tw = this.width;
		int th = this.height;
		int rw = box.width;
		int rh = box.height;
		
		if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0)
			return false;
		
		float tx = this.x;
		float ty = this.y;
		float rx = box.x;
		float ry = box.y;
		
		rw += rx;
		rh += ry;
		tw += tx;
		th += ty;

		// overflow || intersect
		return ((rw < rx || rw > tx) &&
			(rh < ry || rh > ty) &&
			(tw < tx || tw > rx) &&
			(th < ty || th > ry));
	}
	
	public boolean isInside(BoundingBox box)
	{
		final float boxRight = box.x + box.width;
		final float boxBottom = box.y + box.height;
		
		if (box.x >= x && boxRight <= right())
			if (box.y <= y && boxBottom >= bottom())
				return true;
		
		return false;
	}
	
	public boolean isInside(int left, int top)
	{
		if ((left > x) && (left < (x + width)))
			if ((top > y) && (top < (y + height)))
				return true;

		return false;
	}
	
	public void expand(BoundingBox box)
	{
		if (width == 0)
			x = box.x;

		if (height == 0)
			y = box.y;
		
		x = Math.min(x, box.x);
		y = Math.min(y, box.y);
				
		float maxRight = Math.max(x + width, box.x + box.width);
		float maxBottom = Math.max(y + height, box.y + box.height);

		width = (int)(maxRight - x);
		height = (int)(maxBottom - y);
	}

	public int distance(BoundingBox box)
	{
		float x1 = this.getCenterX();
		float y1 = this.getCenterY();

		float x2 = box.getCenterX();
		float y2 = box.getCenterY();
		
		return (int)Math.sqrt((x2-x1) * (x2-x1) + (y2-y1) * (y2-y1));
	}
	
}
