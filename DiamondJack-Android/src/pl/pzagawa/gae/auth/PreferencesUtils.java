package pl.pzagawa.gae.auth;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesUtils
{
	private final static String PREFS_NAME = "AuthManagerPreferences";

	private final static String KEY_AUTH_TOKEN = "authToken";
	private final static String KEY_AUTH_COOKIE = "authCookie";
	private final static String KEY_DEFAULT_ACCOUNT_NAME = "accountName";

	private static SharedPreferences getPrefs(Activity activity)
	{
		return activity.getSharedPreferences(PREFS_NAME, Activity.MODE_WORLD_READABLE);
	}

	public static String getCookie(Activity activity)
	{
		SharedPreferences prefs = getPrefs(activity);

		return prefs.getString(KEY_AUTH_COOKIE, null);
	}

	public static String getToken(Activity activity)
	{
		SharedPreferences prefs = getPrefs(activity);

		return prefs.getString(KEY_AUTH_TOKEN, null);
	}
	
	public static String getSelectedAccountName(Activity activity)
	{
		SharedPreferences prefs = getPrefs(activity);

		return prefs.getString(KEY_DEFAULT_ACCOUNT_NAME, null);
	}
	
	public static void setCookie(Activity activity, String authCookie)
	{
		SharedPreferences prefs = getPrefs(activity);
		
		Editor edit = prefs.edit();
		
		edit.putString(KEY_AUTH_COOKIE, authCookie);
		edit.commit();
	}

	public static void setToken(Activity activity, String authToken)
	{
		SharedPreferences prefs = getPrefs(activity);
		
		Editor edit = prefs.edit();
		
		edit.putString(KEY_AUTH_TOKEN, authToken);
		edit.commit();
	}

	public static void clearDefaultAccountName(Activity activity)
	{
		setDefaultAccountName(activity, null);
	}
	
	public static void setDefaultAccountName(Activity activity, String accountName)
	{
		SharedPreferences prefs = getPrefs(activity);
		
		Editor edit = prefs.edit();
		
		edit.putString(KEY_DEFAULT_ACCOUNT_NAME, accountName);
		edit.commit();
	}
	
}
