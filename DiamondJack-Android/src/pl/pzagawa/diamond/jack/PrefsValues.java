package pl.pzagawa.diamond.jack;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefsValues
{
	private final static String PREFS_NAME = "PrefsValues";

	private static PrefsValues values;
	
	private final SharedPreferences prefs;
	
	private PrefsValues(Context context)
	{
		this.prefs = context.getSharedPreferences(PREFS_NAME, Activity.MODE_WORLD_READABLE);		
	}
	
	public static void initialize(Context parent)
	{
		if (values == null)
			values = new PrefsValues(parent);
	}

	public static void set(String name, long value)
	{
		Editor edit = values.prefs.edit();		
		edit.putLong(name, value);		
		edit.commit();		
	}

	public static void set(String name, int value)
	{
		Editor edit = values.prefs.edit();		
		edit.putInt(name, value);
		edit.commit();		
	}
	
	public static void set(String name, String value)
	{
		Editor edit = values.prefs.edit();
		edit.putString(name, value);
		edit.commit();		
	}

	public static void set(String name, boolean value)
	{
		Editor edit = values.prefs.edit();
		edit.putBoolean(name, value);
		edit.commit();
	}
	
	public static long getLong(String name, long defValue)
	{
		return values.prefs.getLong(name, defValue);
	}

	public static int getInt(String name, int defValue)
	{
		return values.prefs.getInt(name, defValue);
	}

	public static String getString(String name, String defValue)
	{
		return values.prefs.getString(name, defValue);
	}

	public static boolean getBool(String name, boolean defValue)
	{
		return values.prefs.getBoolean(name, defValue);
	}
	
}
