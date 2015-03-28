package pl.pzagawa.diamond.jack.activities;

import java.util.Observable;
import java.util.Observer;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.events.Event;
import pl.pzagawa.events.EventId;
import pl.pzagawa.gae.client.RequestCommand;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class UpdaterActivity
	extends Activity
	implements Observer	
{	
	private UpdaterListItem lastItem;	
	
	private LinearLayout llayContent;
	private ScrollView scroller;
	private TextView labelTitle;
	private Button closeButton;
	private Button tryConnectAgainButton;
	
	private Animation shakeAnim;
		
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_updater);

        llayContent = (LinearLayout)this.findViewById(R.id.llayContent);
        scroller = (ScrollView)this.findViewById(R.id.scroller);
        labelTitle = (TextView)this.findViewById(R.id.labelTitle);
        
        tryConnectAgainButton = (Button)this.findViewById(R.id.tryConnectAgainButton);
        tryConnectAgainButton.setOnClickListener(onTryConnectAgainClick);
        
        closeButton = (Button)this.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(onCloseClick);
        
        shakeAnim = AnimationUtils.loadAnimation(this, R.anim.title_shake);        
    }

    private void setHeader(String title)
    {
    	labelTitle.setText(title);
    }
    
	@Override
	protected void onStart()
	{
		super.onStart();

		//register PortalAccess events
        MainApplication.getPortalAccess().addObserver(this);
				
        startDatabaseUpdateOperation();
	}

	@Override
	protected void onStop()
	{
		//register PortalAccess events
        MainApplication.getPortalAccess().deleteObserver(this);		
        
		super.onStop();
	}

	private void startDatabaseUpdateOperation()
	{
		llayContent.removeAllViews();
		
        setHeader(getString(R.string.updaterTitleStart));
                
        tryConnectAgainButton.setVisibility(View.GONE);        
        closeButton.setEnabled(false);

        MainApplication.getPortalAccess().startOperationUpdateDatabase(UpdaterActivity.this);
	}
	
	private void startGameUpdateOperation()
	{
		llayContent.removeAllViews();
		
        setHeader(getString(R.string.updaterTitleStart));
        
        tryConnectAgainButton.setVisibility(View.GONE);
        closeButton.setEnabled(false);

        MainApplication.getPortalAccess().startRequestGetMobileProfile(UpdaterActivity.this);		
	}

	private void updateUiForRepeatingRequest()
	{
        setHeader(getString(R.string.updaterTitleStart));
        
        tryConnectAgainButton.setVisibility(View.GONE);
        closeButton.setEnabled(false);
	}

	private void finishOperationWithSuccess()
	{   
        closeLastLogItem();
		
        setHeader(getString(R.string.updaterTitleFinish));
        
        closeButton.setEnabled(true);

        //run delayed activity close
		Handler h = new Handler();
		h.postDelayed(runnableDelayedClose, 1000 * 2);		
	}
	
	private void finishOperationWithError()
	{   
        closeLastLogItem();
		
        setHeader(getString(R.string.updaterTitleFinishWithErrors));
        
        closeButton.setEnabled(true);
		
        tryConnectAgainButton.setVisibility(View.VISIBLE);
	}

	private Runnable runnableDelayedClose = new Runnable()
	{
		@Override
		public void run()
		{
			if (closeButton.isEnabled())
				finish();
		}		
	};

	private UpdaterListItem addLogItem(String text)
	{
		closeLastLogItem();
		
		lastItem = new UpdaterListItem(this.getLayoutInflater(), text);
		llayContent.addView(lastItem.getView());
		
		//scroll this f*** list down
		scroller.post(new Runnable()
		{ 
		    public void run()
		    { 
		    	scroller.fullScroll(ScrollView.FOCUS_DOWN); 
		    } 
		}); 
		
		return lastItem;
	}
	
	private void closeLastLogItem()
	{				
		if (lastItem != null)
			lastItem.done();
	}
	
	@Override
	public void update(Observable src, Object data)
	{
		Event event = (Event)data;
		
		//process event
		if (event.getId() == EventId.GAME_UPDATE_FINISH)
		{
			finishOperationWithSuccess();
		}

		//process event
		if (event.getId() == EventId.GAME_UPDATE_FINISH_WITH_ERROR)
		{
			finishOperationWithError();
		}
		
		//process event
		if (event.getId() == EventId.GAME_UPDATE_LOG_MESSAGE)
		{			
			addLogItem(event.getDescription());
		}
		
		//process request starts
		if (event.getId() == EventId.REQ_GET_ONE_LEVEL)
			if (event.isStart())
				updateUiForRepeatingRequest();

		if (event.getId() == EventId.REQ_GET_MOBILE_PROFILE)
			if (event.isStart())
				updateUiForRepeatingRequest();
		
		if (event.getId() == EventId.AUTHORIZATION)
			if (event.isStart())
				updateUiForRepeatingRequest();
	}

	private View.OnClickListener onTryConnectAgainClick = new View.OnClickListener()
	{
		@Override
		public void onClick(View arg0)
		{
			startGameUpdateOperation();			
		}		
	};
	
	private View.OnClickListener onCloseClick = new View.OnClickListener()
	{
		@Override
		public void onClick(View arg0)
		{
			UpdaterActivity.this.finish();			
		}
	};

	@Override
	public void onBackPressed()
	{
        if (closeButton.isEnabled())
        {
        	super.onBackPressed();
        } else {
            labelTitle.startAnimation(shakeAnim);        	
        }
	}
	
	public static void open(Activity parent, int levelsReturn)
	{
		Intent intent = new Intent(parent, UpdaterActivity.class);

		intent.putExtra(RequestCommand.PARAM_LEVELS_RETURN, levelsReturn);
	
		parent.startActivity(intent);
	}

}
