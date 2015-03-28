package pl.pzagawa.events;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Observable;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import android.os.Handler;

public abstract class GameUpdateOperation
	extends Observable
{
	private final Event runEvent;
	private final Handler uiHandler = new Handler();

	public GameUpdateOperation(EventId eventId)
	{
		this.runEvent = new Event(eventId);
	}

	public GameUpdateOperation(Event event)
	{
		this.runEvent = event;
	}
	
	public void run()
	{
		this.setChanged();
		this.notifyObservers(runEvent);		
		
		//do worker thread
		Thread th = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					runWorkerThread();
					
				} catch (ClientProtocolException e)
				{
					e.printStackTrace();					
				}
				catch (JSONException e)
				{
					e.printStackTrace();					
				}
				catch (IOException e)
				{
					e.printStackTrace();					
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				finally
				{
					//call event on UI thread when finish
					uiHandler.post(new Runnable()
					{
						@Override
						public void run()
						{
							runEvent.finish();
							
							try
							{
								final boolean isSuccess = runFinishUiThread();

								if (isSuccess)
									runEvent.setSuccessResult();
							}
							catch (JSONException e)
							{
								e.printStackTrace();
							}
							catch (SQLException e)
							{
								e.printStackTrace();
							}

							GameUpdateOperation.this.setChanged();
							GameUpdateOperation.this.notifyObservers(runEvent);
						}
					});					
				}				
			}
		});
		
		th.start();		
	}
	
	protected abstract void runWorkerThread()
		throws ClientProtocolException, JSONException, IOException, java.sql.SQLException;
	
	protected abstract boolean runFinishUiThread()
		throws JSONException, java.sql.SQLException;
	
}
