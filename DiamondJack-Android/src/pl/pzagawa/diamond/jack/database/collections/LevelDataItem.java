package pl.pzagawa.diamond.jack.database.collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.game.engine.map.LevelSetup;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LevelData")
public class LevelDataItem
	extends DataItem
{
	@DatabaseField(id = true)
	private long levelId = -1;
	
	@DatabaseField
	private String uid;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String description;
	
	@DatabaseField
	private String author;
		
	@DatabaseField
	private boolean isPublic = false;
	
	@DatabaseField
	private boolean isPrivate = false;

	@DatabaseField
	private boolean isFree = false;
	
	@DatabaseField
	private int mapWidth;

	@DatabaseField
	private int mapHeight;
	
	@DatabaseField
	private String dataBackground;

	@DatabaseField
	private String dataGround;

	@DatabaseField
	private String dataShape;

	@DatabaseField
	private String dataObjects;

	@DatabaseField
	private String dataEnemies;
	
	//empty ctor
	public LevelDataItem()
	{
	}
	
	public long getLevelId()
	{
		return levelId;
	}
	
	public String getUID()
	{
		return uid;
	}	
	
	@Override
	public String toString()
	{
		return Long.toString(levelId) + ". " + name;
	}
	
	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		if ((description != null) && (description.length() > 200))
			return description.substring(0, 200) + "...";
		
		return description;
	}

	public boolean hasDescription()
	{		
		return ((description != null) && (description.trim().length() > 0));
	}
	
	public String getAuthor()
	{
		return author;
	}
		
	public void setPublic(boolean value)
	{
		this.isPublic = value;
	}

	public void setFree(boolean value)
	{
		this.isFree = value;
	}
	
	public boolean isPublic()
	{
		return isPublic;
	}

	public boolean isPrivate()
	{
		return isPrivate;
	}
	
	public boolean isFree()
	{
		return isFree;
	}

	public String getDataSetup()
	{
		StringBuilder sb = new StringBuilder(); 
		
		sb.append("id:" + Long.toString(levelId));
		sb.append("\n");
		
		sb.append("name:" + name);
		sb.append("\n");
		
		sb.append("description:" + description);
		sb.append("\n");
		
		sb.append("author:" + author);
		sb.append("\n");
				
		sb.append("size-tilemap-bkg-wrap:" + DataStaticBackground.getMapSize());
		sb.append("\n");
		
		final String mapSize = Integer.toString(mapWidth) + "*" + Integer.toString(mapHeight);
		
		sb.append("size-tilemap-ground:" + mapSize);
		sb.append("\n");
		
		sb.append("size-tilemap-enemies:" + mapSize);
		sb.append("\n");
		
		sb.append("size-tilemap-shape:" + mapSize);
		sb.append("\n");
		
		sb.append("size-tilemap-objects:" + mapSize);
		sb.append("\n");

		sb.append("version:1");
		sb.append("\n");
		
		return sb.toString();
	}

	public String getDataBackground()
	{
		return dataBackground;
	}

	public String getDataGround()
	{
		return dataGround;
	}

	public String getDataShape()
	{
		return dataShape;
	}

	public String getDataObjects()
	{
		return dataObjects;
	}

	public String getDataEnemies()
	{
		return dataEnemies;
	}
	
	public void decodeDataSetup(String dataSetup)
	{
		LevelSetup levelSetup = new LevelSetup();
		levelSetup.parse(dataSetup);
		
		this.levelId = levelSetup.getLevelId();
		this.name = levelSetup.getLevelName();
		this.description = levelSetup.getLevelDescription();
		this.author = levelSetup.getAuthor();

		final String mapSize = levelSetup.getTileMapSize("tilemap-ground");
		
		final String[] mapSizeVec = mapSize.split("\\*");
		
		this.mapWidth = Integer.parseInt(mapSizeVec[0]);
		this.mapHeight = Integer.parseInt(mapSizeVec[1]);
	}

	public void setDataBackground(String dataBackground)
	{
		if (dataBackground == null)
		{
			this.dataBackground = DataStaticBackground.getData();
			return;
		}
		
		this.dataBackground = dataBackground;
	}

	public void setDataGround(String dataGround)
	{
		this.dataGround = dataGround;
	}

	public void setDataShape(String dataShape)
	{
		this.dataShape = dataShape;
	}

	public void setDataObjects(String dataObjects)
	{
		this.dataObjects = dataObjects;
	}

	public void setDataEnemies(String dataEnemies)
	{
		this.dataEnemies = dataEnemies;
	}
	
	public boolean isLevelSelected()
	{
		return (MainApplication.getSettings().getSelectedLevelId() == levelId); 		
	}

	//parse object properties from its JSON representation
	public void parse(JSONObject jData)
		throws JSONException
	{
		this.levelId = jData.getLong("levelId");
		this.uid = jData.getString("uid");
		
		this.name = jData.optString("name", "").replace("\n", "");
		this.description = jData.optString("description", "").replace("\n", "");
		this.author = jData.optString("author", "").replace("\n", "");			
	
		this.isPublic = jData.getBoolean("isPublic");
		this.isPrivate = jData.getBoolean("isPrivate");
		this.isFree = jData.getBoolean("isFree");
	
		this.mapWidth = jData.getInt("mapWidth");
		this.mapHeight = jData.getInt("mapHeight");
	
		final JSONArray dataGround = jData.getJSONArray("dataGround");
		final JSONArray dataShape = jData.getJSONArray("dataShape");
		final JSONArray dataObjects = jData.getJSONArray("dataObjects");
		final JSONArray dataEnemies = jData.getJSONArray("dataEnemies");
	
		this.dataBackground = DataStaticBackground.getData();
		
		this.dataGround = dataGround.join(",");
		this.dataShape = dataShape.join(",");
		this.dataObjects = dataObjects.join(",");
		this.dataEnemies = dataEnemies.join(",");		
	}
	
}
