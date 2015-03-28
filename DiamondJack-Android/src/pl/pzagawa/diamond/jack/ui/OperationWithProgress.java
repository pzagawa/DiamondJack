package pl.pzagawa.diamond.jack.ui;

import java.sql.SQLException;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public abstract class OperationWithProgress
{
	private final String title;
	private final String text;
	private final Handler uiHandler = new Handler();

	public OperationWithProgress(String title, String text)
	{
		this.title = title;
		this.text = text;
	}
	
	public void run(Context context)
	{
		final ProgressDialog progress = ProgressDialog.show(context, title, text, true);
		
		//do worker thread
		Thread th = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					runWorkerThread();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
				
				//call event on UI thread when finish
				uiHandler.post(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							progress.dismiss();
						}
						catch(Exception e)
						{
						}
						
						runFinishUiThread();
					}		
				});
			}
		});
		
		th.start();		
	}
	
	protected abstract void runWorkerThread()
		throws SQLException;
		
	protected abstract void runFinishUiThread();
	
}
