package pl.pzagawa.gae.client;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.database.collections.LevelDataItem;
import pl.pzagawa.diamond.jack.portal.PortalAccess;
import pl.pzagawa.diamond.jack.portal.UserLevelItem;
import pl.pzagawa.diamond.jack.portal.UserProfile;
import pl.pzagawa.events.EventId;

public class RequestGetMobileProfile
	extends RequestCommand
{
	public final static int LEVELS_RETURN_ALL = 0;
	public final static int LEVELS_RETURN_PUBLIC = 1;
	public final static int LEVELS_RETURN_PRIVATE = 2;
	public final static int LEVELS_RETURN_NONE = 3;
	
	private UserProfile profile = null;	
	
	private List<UserLevelItem> publicLevelsToDownload = new ArrayList<UserLevelItem>();
	private List<UserLevelItem> privateLevelsToDownload = new ArrayList<UserLevelItem>();
	
	private final int paramLevelsReturn;
	
	public RequestGetMobileProfile(PortalAccess portalAccess, Activity parent)
	{
		super(portalAccess, parent);
		
		this.paramLevelsReturn = parent.getIntent().getIntExtra(PARAM_LEVELS_RETURN, RequestGetMobileProfile.LEVELS_RETURN_ALL);
	}
	
	@Override
	public String getName()
	{
		return GET_MOBILE_PROFILE;
	}
	
	@Override
	public EventId getEventId()
	{
		return EventId.REQ_GET_MOBILE_PROFILE;
	}
	
	@Override
	public String getRequestData()
		throws JSONException, SQLException
	{
		//create level properties json array
		JSONArray jArray = getExistingLevelItemsArray();
		
		//create response data
		JSONObject jRequest = new JSONObject();

		//type of levels to return
		jRequest.put("levelsReturn", paramLevelsReturn);		
		
		//array of existing levels to exclude from return 
		jRequest.put("levels", jArray);		
		
		return jRequest.toString();
	}

	@Override
	public void processResponse(int statusCode, String result)
		throws JSONException
	{
		if (statusCode == HttpStatus.SC_OK)
		{
			JSONObject jProfile = new JSONObject(result);
			
			profile = new UserProfile(jProfile);
			
			updateLevelsToDownload();
		}	
	}

	public UserProfile getProfile()
	{
		return profile;
	}
	
	private void updateLevelsToDownload()
	{		
		publicLevelsToDownload = profile.publicLevels.cloneLevels();
		
		if (profile.accessRights.canDownloadPrivateLevels())
			privateLevelsToDownload = profile.privateLevels.cloneLevels();
	}

	public boolean areLevelsToDownload()
	{
		if (!publicLevelsToDownload.isEmpty())
			return true;
		if (!privateLevelsToDownload.isEmpty())
			return true;		
		return false;
	}
	
	private UserLevelItem getNextLevelItem(List<UserLevelItem> list)
	{
		if (list.size() > 0)
		{
			UserLevelItem item = list.get(0);
			
	    	list.remove(item);
	    	
			return item;
		}
		
		return null;
	}

	public UserLevelItem getNextLevelItem()
	{
		UserLevelItem item = null;
		
		item = getNextLevelItem(publicLevelsToDownload);		
		if (item != null)
			return item;

		item = getNextLevelItem(privateLevelsToDownload);
		if (item != null)
			return item;
		
		return null;
	}

	private JSONArray getExistingLevelItemsArray()
		throws JSONException, SQLException
	{
		JSONArray jArray = new JSONArray();
				
		final List<LevelDataItem> list = MainApplication.getData().levelData.getLevels();			

		for (LevelDataItem item : list)
		{
			JSONObject jItem = new JSONObject();

			jItem.put("levelId", item.getLevelId());
			
			//include uid to compare, if local item is not published 
			if (item.isPrivate() && !item.isPublic())
			{					
				jItem.put("uid", item.getUID());
			}			
						
			//add item to array
			if (jItem.has("levelId"))				
				jArray.put(jItem);			
		}
		
		return jArray;
	}
	
}
