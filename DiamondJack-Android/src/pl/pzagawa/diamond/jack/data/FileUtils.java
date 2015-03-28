package pl.pzagawa.diamond.jack.data;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import pl.pzagawa.game.engine.EngineException;
import android.content.Context;

public class FileUtils
{
	private final static String LEVELS = "data/levels";
	
	public static String[] getAssetLevelsDirectory(Context context)
	{
		try
		{
			return context.getAssets().list(LEVELS);
		} catch (IOException e)
		{
			return new String[0];
		}
	}

	public static String getFileToString(Context context, String fileName)
		throws IOException
	{
		InputStream is = context.getAssets().open(fileName);
		DataInputStream dis = new DataInputStream(is);
		BufferedReader br = new BufferedReader(new InputStreamReader(dis), 16 * 1024);				
		StringBuilder str = new StringBuilder();  
		String line;
  
		while ((line = br.readLine()) != null)
		{
			str.append(line);
			str.append("\n");
		}
	  
		dis.close();
	  
		return str.toString(); 
	}
	
	public static String getAssetData(Context context, String fileName)
	{
		try
		{
			return getFileToString(context, fileName);
		} catch (Exception e)
		{
			throw new EngineException("FileUtils. Error loading asset: " + fileName + ", " + e.getMessage());
		}		
	}

	public static String getLevelData(Context context, long levelId, String name)
	{
		String fileName = LEVELS + "/" + Long.toString(levelId) + "/" + name + ".txt"; 
		
		return getAssetData(context, fileName);
	}
		
	public static String getMapSetup(Context context, long mapId)
	{
		String fileName = LEVELS + "/map-" + Long.toString(mapId) + ".txt"; 		
		
		return getAssetData(context, fileName);
	}
	
	public static String getLevelSetup(Context context, long levelId)
	{
		return getLevelData(context, levelId, "setup");
	}
	
}
