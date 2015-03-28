package pl.pzagawa.game.engine.map;


public class LevelSetupItem
{
	private final String name;
	private final String value;
	
	public LevelSetupItem(String data)
	{
		final int pos = data.indexOf(":");
		
		this.name = data.substring(0, pos).trim();
		this.value = data.substring(pos + 1).trim();
	}
	
	public String getKey()
	{
		return name;
	}

	public String getString()
	{
		return value;
	}
	
	public float getFloat()
	{
		return Float.valueOf(value);
	}

	public int getInteger()
	{
		if (value == null)
			return -1;
		
		return Integer.valueOf(value);
	}

	public long getLong()
	{
		if (value == null)
			return -1;
		
		return Long.valueOf(value);
	}
	
	public boolean getBool()
	{
		return (getInteger() == 1);
	}

	@Override
	public String toString()
	{
		return name + "=" + value;
	}
	
}
