package pl.pzagawa.diamond.jack.portal;

import java.util.Observable;
import java.util.Observer;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.data.DatabaseUpdater;
import pl.pzagawa.events.Event;
import pl.pzagawa.events.EventId;
import pl.pzagawa.gae.client.RequestGetMobileProfile;
import pl.pzagawa.gae.client.RequestGetOneLevel;
import pl.pzagawa.gae.client.RequestSyncStats;
import android.app.Activity;

public class PortalAccess
	extends Observable
	implements Observer
{
	private UserProfile profile = null;	
	private Activity parent = null;

	private RequestGetMobileProfile reqGetMobileProfile = null;
	private RequestGetOneLevel reqGetOneLevel = null;
	private RequestSyncStats reqSyncStats = null;
	
	public PortalAccess()
	{
		this.profile = new UserProfile();
	}
	
	public void startOperationUpdateDatabase(Activity parent)
	{
		this.parent = parent;

		final DatabaseUpdater databaseUpdater = new DatabaseUpdater(parent);
		
		databaseUpdater.addObserver(this);		
		
		if (MainApplication.isAppVersionUpdate())
		{
			databaseUpdater.process();
		} else {
			databaseUpdater.finish();
		}
	}
	
	public void startRequestGetMobileProfile(Activity parent)
	{
		this.profile = new UserProfile();
		
		this.parent = parent;
		
		this.reqGetMobileProfile = new RequestGetMobileProfile(this, parent);
		this.reqGetMobileProfile.execute();
	}

	public void startRequestGetOneLevel(Activity parent, UserLevelItem levelItem)
	{
		this.parent = parent;
		
		this.reqGetOneLevel = new RequestGetOneLevel(this, parent, levelItem);
		this.reqGetOneLevel.execute();
	}

	public void startRequestSyncStats(Activity parent)
	{
		this.parent = parent;
			
		this.reqSyncStats = new RequestSyncStats(this, parent);
		this.reqSyncStats.execute();
	}

	public boolean isOffline()
	{
		if (profile == null)
			return true;
		
		if (profile.account == null)
			return true;
		
		return profile.account.isOffline;
	}

	public boolean isOnline()
	{
		return !isOffline();
	}
	
	public UserProfile getProfile()
	{
		return profile;
	}

	private void logMessage(String text)
	{		
		this.setChanged();
		this.notifyObservers(new Event(EventId.GAME_UPDATE_LOG_MESSAGE, text));		
	}
	
	private void logReadyToPlay()
	{		
		logMessage("ready to play!");
		notifyEvent(EventId.GAME_UPDATE_FINISH);
	}

	private void logError(String text)
	{		
		logMessage(text);
		notifyEvent(EventId.GAME_UPDATE_FINISH_WITH_ERROR);
	}
	
	private void notifyEvent(EventId eventId)
	{		
		this.setChanged();
		this.notifyObservers(new Event(eventId));
	}
	
	@Override
	public void update(Observable src, Object data)
	{
		Event event = (Event)data;

		//process update database
		if (event.getId() == EventId.DATABASE_UPDATE)
		{
			processUpdateDatabase(event);
		}
		
		//process request event
		if (event.getId() == EventId.REQ_GET_MOBILE_PROFILE)
		{
			processRequestGetMobileProfile(event);
		}

		//process request event
		if (event.getId() == EventId.REQ_SYNC_STATS)
		{
			processRequestSyncStats(event);
		}
		
		//process request event
		if (event.getId() == EventId.REQ_GET_ONE_LEVEL)
		{
			processRequestGetOneLevel(event);
		}
		
		//process other events
		if (event.getId() == EventId.AUTHORIZATION)
			if (event.isStart())
				logMessage("accessing Google Accounts");

		if (event.getId() == EventId.AUTHORIZATION_READY)
			logMessage("authorization done!");
		
		if (event.getId() == EventId.REQUIRED_USER_INPUT)
			logMessage("required user input");

		if (event.getId() == EventId.SELECT_ACCOUNT)
			logMessage("select account for authorization");

		if (event.getId() == EventId.AUTHORIZATION_ERROR)
			logMessage("authorization error");

		if (event.getId() == EventId.CONNECTION_ERROR)
			logMessage("connection error");

		if (event.getId() == EventId.NO_GOOGLE_ACCOUNTS_AVAILABLE)
			logMessage("no google accounts available");

		if (event.getId() == EventId.REQUEST_FAILURE)
			logMessage("request error " + event.getDescription());
		
		if (event.isTypeError())
		{
			notifyEvent(EventId.GAME_UPDATE_FINISH_WITH_ERROR);
			return;
		}
		
		//dispatch locally observed events up
		this.setChanged();
		this.notifyObservers(event);
	}
	
	private void processUpdateDatabase(Event event)
	{
		//request started
		if (event.isStart())
		{
			logMessage("updating database");
		}
		//request finished
		if (event.isFinish())
		{
			if (event.isSuccessResult())
			{
				startRequestGetMobileProfile(parent);
			}
		}
	}
	
	private void processRequestGetMobileProfile(Event event)
	{
		//request started
		if (event.isStart())
		{
			logMessage("connecting to cloud");
		}		
		//request finished
		if (event.isFinish())
		{
			if (event.isSuccessResult())
			{
				//request success
				profile = reqGetMobileProfile.getProfile();
				
				if (reqGetMobileProfile.areLevelsToDownload())
		        {
					runGetOneLevel();
		        }
				else
				{
					runSyncStats();
				}
			}
			else
			{
				logError("profile sync error");
	        }
		}
	}
	
	private void processRequestSyncStats(Event event)
	{
		//request started
		if (event.isStart())
		{
			logMessage("syncing gameplay stats");
		}		
		//request finished
		if (event.isFinish())
		{
			if (event.isSuccessResult())
			{
				logReadyToPlay();
			}
			else
			{
				logError("stats sync error");
	        }
		}
	}
	
	private void processRequestGetOneLevel(Event event)
	{
		//request started
		if (event.isStart())
		{
			final String levelName = "\"" + reqGetOneLevel.levelItem.getName() + "\"";
			logMessage("downloading level:\n" + levelName);
		}
		//request finished
		if (event.isFinish())
		{
			if (event.isSuccessResult())
			{
				runGetOneLevel();
			}
			else
			{
				logError("downloading level error");
	        }			
		}
	}

	private boolean runGetOneLevel()
	{
		final UserLevelItem levelId = reqGetMobileProfile.getNextLevelItem();

		if (levelId == null)
		{	
			runSyncStats();
			return false;
		}
		
		startRequestGetOneLevel(parent, levelId);
		
		return true;
	}
	
	private void runSyncStats()
	{
		if (profile.accessRights.canSyncStats())
		{		
			startRequestSyncStats(parent);
		}
		else
		{
			logReadyToPlay();
		}
	}
	
}
