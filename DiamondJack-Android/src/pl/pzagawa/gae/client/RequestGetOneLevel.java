package pl.pzagawa.gae.client;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.portal.PortalAccess;
import pl.pzagawa.diamond.jack.portal.UserLevelItem;
import pl.pzagawa.events.EventId;

public class RequestGetOneLevel
	extends RequestCommand
{
	public final UserLevelItem levelItem;
	
	public RequestGetOneLevel(PortalAccess portalAccess, Activity parent, UserLevelItem levelItem)
	{
		super(portalAccess, parent);
		
		this.levelItem = levelItem;
	}
	
	@Override
	public String getName()
	{
		return GET_ONE_LEVEL;
	}
	
	@Override
	public EventId getEventId()
	{
		return EventId.REQ_GET_ONE_LEVEL;
	}
	
	@Override
	public String getRequestData()
		throws JSONException
	{
		JSONObject jRequest = new JSONObject();
		
		jRequest.put("id", levelItem.levelId);
		
		return jRequest.toString();
	}

	@Override
	public void processResponse(int statusCode, String result)
		throws JSONException, java.sql.SQLException
	{
		if (statusCode == HttpStatus.SC_OK)
		{
			final JSONObject jData = new JSONObject(result);

			final long levelId = jData.getLong("levelId");

			//add or update level item if exists
			MainApplication.getData().updateLevelItem(levelId, jData);
		}	
	}

}
