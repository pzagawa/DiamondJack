package pl.pzagawa.diamond.jack.data;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import android.content.Context;

public class FileReader
{
	protected final Context context;

	public FileReader(Context context)
	{
		this.context = context;
	}
	
	public String readFileToString(File file)
		throws
		IOException
	{		
		FileInputStream fis = new FileInputStream(file);
				
		BufferedInputStream bis = new BufferedInputStream(fis); 
		DataInputStream dis = new DataInputStream(bis);
		
		StringBuilder data = new StringBuilder();
		
		while (dis.available() != 0)
		{
			data.append(dis.readLine());
			data.append("\n");
		}
	
		dis.close();
		
		return data.toString();
	}
	
	public String readFileToString(File parentDirectory, String fileName)
		throws
		IOException
	{		
		File file = new File(parentDirectory, fileName);
		
		return readFileToString(file);
	}

	public String readFileToString(String fileName)
		throws
		IOException
	{		
		File file = new File(fileName);
		
		return readFileToString(file);
	}
	
}
