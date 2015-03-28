package pl.pzagawa.diamond.jack;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class Utils
{
	public static String getAppVersionName(Context context)
    {
	    //get version from manifest
	    final PackageManager packageManager = context.getPackageManager();
	    if (packageManager != null)
	    {
            try
            {                               
            	PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                if (packageInfo != null)
                	return packageInfo.versionName;
                
            } catch (NameNotFoundException e)
            {
            }               
	    }
	    return "";
    }       

	public static int getAppVersionCode(Context context)
    {
	    //get version from manifest
	    final PackageManager packageManager = context.getPackageManager();
	    if (packageManager != null)
	    {
            try
            {                               
            	PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                if (packageInfo != null)
                	return packageInfo.versionCode;
                
            } catch (NameNotFoundException e)
            {
            }               
	    }
	    return 0;
    }       
	
	public static void openWebsite(Activity parent, String url)
	{
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		parent.startActivity(i);										
	}

	public static String formatTimeAsText(int seconds)
	{
		if (seconds < 0)
			return "";
		
		final int hours = (int)((float)seconds / (float)3600.0f);
		final int minutes = (int)(((float)seconds / (float)60.0f) % 60);

		if (hours == 0 && minutes == 0)
			return String.format("%1$ds", seconds);

		if (hours == 0)
			return String.format("%1$dm %2$ds", minutes, seconds % 60);
	
		return String.format("%1$dh %2$dm %3$ds", hours, minutes, seconds % 60);
	}

	public static String getMD5(String value)
		throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		 
		byte[] messageDigest = md.digest(value.getBytes());
		 
		BigInteger number = new BigInteger(1, messageDigest);
		 
		return number.toString(16);
	}
	
}
