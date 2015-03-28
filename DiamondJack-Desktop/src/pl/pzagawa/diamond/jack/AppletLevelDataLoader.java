package pl.pzagawa.diamond.jack;

import pl.pzagawa.game.engine.map.LevelDataLoader;

public class AppletLevelDataLoader
	implements LevelDataLoader
{
	public static DiamondJackApplet applet;

	private String getParam(String paramName)
	{
		return applet.getParameter(paramName);
	}

	@Override
	public String getMapSetup(long mapId)
	{
		return "";
	}

	@Override
	public String getLevelSetup(long levelId)
	{
		StringBuilder sb = new StringBuilder(); 
		
		sb.append("id:" + getParam("level-id"));
		sb.append("\n");
		
		sb.append("author:" + getParam("level-author"));
		sb.append("\n");

		sb.append("name:" + getParam("level-name"));
		sb.append("\n");

		sb.append("description:" + getParam("level-description"));
		sb.append("\n");

		sb.append("size-tilemap-bkg-wrap:" + getParam("level-size-bkg"));
		sb.append("\n");

		sb.append("size-tilemap-ground:" + getParam("level-size-ground"));
		sb.append("\n");

		sb.append("size-tilemap-enemies:" + getParam("level-size-enemies"));
		sb.append("\n");

		sb.append("size-tilemap-shape:" + getParam("level-size-shape"));
		sb.append("\n");

		sb.append("size-tilemap-objects:" + getParam("level-size-objects"));
		sb.append("\n");

		return sb.toString();
	}
	
	@Override
	public String getLevelData(long levelId, String name)
	{
		return getParam("level-data-" + name);
	}

}
