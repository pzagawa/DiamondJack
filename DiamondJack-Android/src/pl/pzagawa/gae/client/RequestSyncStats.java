package pl.pzagawa.gae.client;

import java.sql.SQLException;
import java.util.List;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.PrefsValues;
import pl.pzagawa.diamond.jack.database.collections.LevelStatsItem;
import pl.pzagawa.diamond.jack.portal.PortalAccess;
import pl.pzagawa.events.EventId;
import android.app.Activity;

public class RequestSyncStats
	extends RequestCommand
{
	private final static String MOBILE_UPDATE_KEY = "mobileUpdate";
	private final static String PREFS_FULL_SYNC = "isFullSync";
	
	public RequestSyncStats(PortalAccess portalAccess, Activity parent)
	{
		super(portalAccess, parent);
	}
	
	@Override
	public String getName()
	{
		return SYNC_STATS;
	}
	
	@Override
	public EventId getEventId()
	{
		return EventId.REQ_SYNC_STATS;
	}
	
	@Override
	public String getRequestData()
		throws JSONException
	{
		//create request data
		final JSONObject jRequest = new JSONObject();

		//create items array
		final JSONArray jSyncItems = new JSONArray();
		
		try
		{
			//get changed sync items
			final List<LevelStatsItem> itemsToSync = MainApplication.getData().levelStats.getItemsToSync();
			
			//fill sync items array
			for (LevelStatsItem item : itemsToSync)
				jSyncItems.put(item.toJSON());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		//put array to request data
		jRequest.put("syncItems", jSyncItems);

		//control full sync mode
		jRequest.put(PREFS_FULL_SYNC, PrefsValues.getBool(PREFS_FULL_SYNC, true));

		return jRequest.toString();
	}

	@Override
	public void processResponse(int statusCode, String result)
		throws JSONException, java.sql.SQLException
	{
		if (statusCode == HttpStatus.SC_OK)
		{
			//create stats item as parsing utility
			final LevelStatsItem tempStatsItem = new LevelStatsItem();					
			
			//get server result
			final JSONObject jResult = new JSONObject(result);

			//get diff items array
			final JSONArray jSyncItems = jResult.getJSONArray("syncItems");
						
			//process server stats items array
			for (int index = 0; index < jSyncItems.length(); index++)
			{
				//get server stats item
				final JSONObject jStatsItem = jSyncItems.getJSONObject(index);
				
				processServerStatsItem(jStatsItem, tempStatsItem);
			}
			
			//get full items array, from previous installation, not already existing
			final JSONArray jAddSyncItems = jResult.getJSONArray("fullSyncItems");
			
			//process server stats items array
			for (int index = 0; index < jAddSyncItems.length(); index++)
			{
				//get server stats item
				final JSONObject jStatsItem = jAddSyncItems.getJSONObject(index);

				addServerStatsItem(jStatsItem);
			}

			//disable full sync after success
			PrefsValues.set(PREFS_FULL_SYNC, false);
		}
	}
	
	private void processServerStatsItem(final JSONObject jStatsItem, final LevelStatsItem tempStatsItem)
		throws JSONException, SQLException
	{
		//get server stats key values
		final boolean mobileUpdate = jStatsItem.getBoolean(MOBILE_UPDATE_KEY);				
		final long levelId = jStatsItem.getLong("levelId");
		final int version = jStatsItem.optInt("version", 0);

		//get mobile stats item to process
		final LevelStatsItem statsItem = MainApplication.getData().levelStats.getItemById(levelId);

		//check server mobile update flag
		if (mobileUpdate)
		{
			//parse server entity
			tempStatsItem.resetValues();
			tempStatsItem.parse(jStatsItem);

			//update mobile version from server
			statsItem.setVersion(version + 1);
			
			//update mobile item with server values
			statsItem.aggregate(tempStatsItem);					
		}

		//reset changed flag
		statsItem.clearChanged();
		
		//update level stats item
		MainApplication.getData().levelStats.updateItem(statsItem);
	}

	private void addServerStatsItem(final JSONObject jStatsItem)
		throws JSONException, SQLException
	{
		//get server stats key values
		final long levelId = jStatsItem.getLong("levelId");
		
		//skip, if stats item for given level already exists 
		if (MainApplication.getData().levelStats.getItemById(levelId) != null)
			return;

		//add old user stats item from server
		final LevelStatsItem item = new LevelStatsItem();
		
		item.setLevelId(levelId);
		
		item.parse(jStatsItem);
		
		MainApplication.getData().levelStats.addItem(item);
	}
	
}
