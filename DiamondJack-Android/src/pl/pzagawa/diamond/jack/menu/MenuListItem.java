package pl.pzagawa.diamond.jack.menu;

public class MenuListItem
{
	private final int id;
	private final String text;
	private String description;
	
	public MenuListItem(int id, String text)
	{
		this.id = id;
		this.text = text;
	}	

	public MenuListItem(int id, String text, String description)
	{
		this.id = id;
		this.text = text;
		this.description = description;
	}	
	
	public int getId()
	{
		return id;
	}
	
	public String getText()
	{
		return text;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	@Override
	public String toString()
	{
		return Integer.toString(id) + ". " + text;
	}
	
}
