package pl.pzagawa.gae.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.events.Event;
import pl.pzagawa.events.EventId;
import pl.pzagawa.events.GameUpdateOperation;
import pl.pzagawa.gae.auth.AuthManager;
import pl.pzagawa.gae.auth.Consts;
import android.app.Activity;
import android.os.Handler;

public class RequestProcessor
	extends Observable
	implements Observer
{
	public final int MAX_RETRY_COUNT = 3;
	
	private final Handler handler = new Handler();
	private final Activity parent;	
	private final AuthManager authManager;
	private URI uri;
	
	private RequestCommand requestCommand = null;
	private int retryCounter = 1;

	public RequestProcessor(Activity parent)
	{
		this.parent = parent;
        this.authManager = new AuthManager(parent);
        this.authManager.addObserver(this);

		try
		{
	        this.uri = new URI(Consts.APP_URL + "/" + Consts.RESOURCE);
	        
		} catch (URISyntaxException e)
		{
			e.printStackTrace();
		}        
	}
	
	public void process(RequestCommand requestCommand)
	{
		this.requestCommand = requestCommand;
		this.retryCounter = 1;

		handler.removeCallbacks(runnableAuthorization);
		handler.post(runnableAuthorization);
	}
	
	private Runnable runnableAuthorization = new Runnable()
	{
		@Override
		public void run()
		{
			if (authManager.getAuthCookie() == null)
			{
				authManager.process();
			}
			else
			{
				postRequest();
			}
		}
	};

	private void notify(Event event)
	{
		setChanged();
		notifyObservers(event);
	}
	
	private void postRequest()
	{
		final EventId eventId = requestCommand.getEventId();
				
		final ClientRequest request = new ClientRequest(uri, authManager.getAuthCookie());
		
		GameUpdateOperation owp = new GameUpdateOperation(eventId)
		{
			@Override
			protected void runWorkerThread()
				throws ClientProtocolException, JSONException, IOException, SQLException
			{
				request.post(requestCommand);
			}

			@Override
			protected boolean runFinishUiThread()
				throws JSONException, java.sql.SQLException
			{
				final int statusCode = request.getStatusCode();
				final String result = request.getResultString();
				
				//token is no longer valid
				if (statusCode == HttpStatus.SC_UNAUTHORIZED)
				{
					authManager.invalidateToken();

					if (retryCounter > MAX_RETRY_COUNT)
					{
						//too many retires; report authorization error
						RequestProcessor.this.notify(new Event(EventId.AUTHORIZATION_ERROR, parent.getString(R.string.httpAuthError)));
						return false;
					}

					retryCounter++;

					//start request again
					handler.removeCallbacks(runnableAuthorization);					
					handler.post(runnableAuthorization);
					return false;
				}

				requestCommand.processResponse(statusCode, result);					
				
				//SC_OK
				if (statusCode == HttpStatus.SC_OK)
				{
					//request success
					RequestProcessor.this.notify(new Event(EventId.REQUEST_SUCCESS));
					return true;
				}

				//request failure
				if (statusCode == 0)
				{
					//connection error
					String message = parent.getString(R.string.httpConnectionError);
					
					RequestProcessor.this.notify(new Event(EventId.CONNECTION_ERROR, message));					
				}
				else
				{
					//request error
					String message = Integer.toString(statusCode) + ": " + request.getStatusReason() + ".";
					
					if (request.getServerMessage().length() > 0)
						message += "\n" + request.getServerMessage();

					RequestProcessor.this.notify(new Event(EventId.REQUEST_FAILURE, message));					
				}
				
				return false;
			}
		};

		owp.addObserver(this);		
		owp.run();
	}

	@Override
	public void update(Observable src, Object data)
	{
		Event event = (Event)data;
		
		//dispatch locally observed events up
		RequestProcessor.this.notify(event);
		
		if (event.getId() == EventId.AUTHORIZATION_READY)
		{
			postRequest();
		}
		
		if (event.getId() == EventId.REQUIRED_USER_INPUT)
		{
			parent.startActivity(event.getIntent());
		}
	}
	
}
