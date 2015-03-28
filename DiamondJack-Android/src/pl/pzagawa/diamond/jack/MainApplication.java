package pl.pzagawa.diamond.jack;

import java.sql.SQLException;
import java.util.Calendar;
import pl.pzagawa.diamond.jack.database.DataSource;
import pl.pzagawa.diamond.jack.portal.PortalAccess;
import pl.pzagawa.diamond.jack.portal.UserProfile;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.support.ConnectionSource;

public class MainApplication
	extends Application
{
	private static Context context;	
    private static DataSource data;
	private static Settings settings;    
    private static ConnectionSource connection;

	private static PortalAccess portalAccess = new PortalAccess();

	private static int appVersionCode = 0;
    
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		
		context = this.getApplicationContext();
        data = (DataSource)OpenHelperManager.getHelper(context, DataSource.class);
        
        PrefsValues.initialize(this);
		
        settings = new Settings(context);		
        settings.load();

        connection = new AndroidConnectionSource(data);
        
    	appVersionCode = Utils.getAppVersionCode(this);
	}

	public static void openDatabase(Activity parent)
	{
        try
		{
       		connection.getReadWriteConnection();
			
		} catch (SQLException e)
		{
			e.printStackTrace();
		}		
	}
	
	@Override
	public void onTerminate()
	{
        try
		{
			connection.close();
			
		} catch (SQLException e)
		{
		}
		
		OpenHelperManager.releaseHelper();
		
		super.onTerminate();
	}
	
	@Override
	public void onLowMemory()
	{
		super.onLowMemory();
	}

	public static Context getContext()
	{
		return context;
	}
	
	public static DataSource getData()
	{
		return data;
	}
	
	public static Settings getSettings()
	{
		return settings;
	}
	
	public static boolean isUserProfileOnline()
	{	
		return portalAccess.isOnline();
	}

	public static boolean isUserProfileOffline()
	{	
		return portalAccess.isOffline();
	}
	
	public static UserProfile getUserProfile()
	{	
		return portalAccess.getProfile();
	}
	
	public static PortalAccess getPortalAccess()
	{	
		return portalAccess;
	}
	
	public static int getAppVersionCode()
	{
		return appVersionCode;
	}
	
	public static boolean isAppVersionUpdate()
	{
		boolean isUpdate = false;
		
		final int currentAppVersion = getAppVersionCode();

		if (currentAppVersion > settings.getLastAppVersion())
		{
			isUpdate = true;
			
			settings.setLastAppVersion(currentAppVersion);				
		}
		
		return isUpdate;
	}
	
	public static boolean isUpdaterRequiredToRun()
	{
		final String LAST_UPDATE_TIME_MS = "LAST_UPDATE_TIME_MS";

		final long nowTimeMs = Calendar.getInstance().getTimeInMillis();
		
		if (isUserProfileOffline())
		{
			PrefsValues.set(LAST_UPDATE_TIME_MS, nowTimeMs);
			return true;
		}

		final long lastUpdateTimeMs = PrefsValues.getLong(LAST_UPDATE_TIME_MS, 0);
				
		final long MINUTE_AS_MS = 1000 * 60;
		
		//3 hours update period
		final long MIN_UPDATE_PERIOD = (MINUTE_AS_MS * 60) * 3;

		final long gameUpdateTimeMsTreshold = lastUpdateTimeMs + MIN_UPDATE_PERIOD;		

		if (nowTimeMs > gameUpdateTimeMsTreshold)
		{
			PrefsValues.set(LAST_UPDATE_TIME_MS, nowTimeMs);
			return true;
		} else {
			return false;
		}
	}

}
