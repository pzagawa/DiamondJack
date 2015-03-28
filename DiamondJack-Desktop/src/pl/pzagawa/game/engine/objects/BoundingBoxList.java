package pl.pzagawa.game.engine.objects;

import java.util.ArrayList;

public class BoundingBoxList
{
	private ArrayList<BoundingBox> items = new ArrayList<BoundingBox>();

	public BoundingBoxList()
	{
	}

	public BoundingBox[] getArray()
	{
		return items.toArray(new BoundingBox[items.size()]);
	}
	
	public void addNew(BoundingBox box)
	{
		items.add(box);
	}

	public BoundingBox getLast()
	{
		if (items.isEmpty())
			return null;
		
		return items.get(items.size() - 1);
	}
	
	public boolean isEmpty()
	{
		return items.isEmpty();
	}
	
}
