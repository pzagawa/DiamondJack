package pl.pzagawa.diamond.jack.database.collections;

import com.j256.ormlite.field.DatabaseField;

public class DataItem
{
	@DatabaseField
	private boolean isChanged = false;
	
	//***
	public void setChanged()
	{
		this.isChanged = true;
	}
	
	public void clearChanged()
	{
		this.isChanged = false;
	}
	
	public boolean isChanged()
	{
		return isChanged;
	}
	
}
