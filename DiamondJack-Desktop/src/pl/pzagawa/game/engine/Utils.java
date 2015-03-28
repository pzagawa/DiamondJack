package pl.pzagawa.game.engine;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Utils
{
	public static String ReadTextFile(InputStream is)
		throws IOException
	{
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

	public static Map<String, String> propertiesFromString(String values)
	{
		Map<String, String> map = new HashMap<String, String>();
		
		String[] pairs = values.split(";");

		for (String pair : pairs)
		{
			String[] item = pair.trim().split(":");
			
			String key = item[0];
			String value = "";
			
			if (item.length == 2)
				value = item[1];
			
			map.put(key, value);
		}

		return map;
	}	

	public static float getDirectionAngle(float x, float y)
	{
		float radians = (float)Math.atan2(y, x);

		//convert to degrees
		return (float)((180.0f / Math.PI) * radians);
	}

	public static float getDistance(float x, float y)
	{
		return (float)Math.sqrt(x * x + y * y);
	}
	
}
