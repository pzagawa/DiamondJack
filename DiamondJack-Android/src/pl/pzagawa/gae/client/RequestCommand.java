package pl.pzagawa.gae.client;

import org.json.JSONException;
import pl.pzagawa.diamond.jack.portal.PortalAccess;
import pl.pzagawa.events.EventId;
import android.app.Activity;
import android.os.Handler;

public abstract class RequestCommand
{
	public final static String PARAM_LEVELS_RETURN = "LEVELS_RETURN";	
	
	public final static String GET_MOBILE_PROFILE = "get-mobile-profile";
	public final static String GET_ONE_LEVEL = "get-one-level";
	public final static String SYNC_STATS = "sync-stats";

	protected final Handler handler = new Handler();
	
	protected final PortalAccess portalAccess;
	protected final Activity parent;
	protected final RequestProcessor rp; 	
	
	public RequestCommand(PortalAccess portalAccess, Activity parent)
	{
		this.portalAccess = portalAccess;
		this.parent = parent;
		this.rp = new RequestProcessor(parent);
		this.rp.addObserver(portalAccess);
	}
	
	public Activity getParent()
	{
		return parent;
	}
	
	public void execute()
	{
		handler.removeCallbacks(runnableRequest);
		handler.post(runnableRequest);		
	}
	
	private Runnable runnableRequest = new Runnable()
	{
		@Override
		public void run()
		{
			RequestCommand.this.rp.process(RequestCommand.this);
		}
	};
	
	public abstract String getName();
	
	public abstract EventId getEventId();
	
	public abstract String getRequestData()
		throws JSONException, java.sql.SQLException;
	
	public abstract void processResponse(int statusCode, String result)
		throws JSONException, java.sql.SQLException;
	
}
