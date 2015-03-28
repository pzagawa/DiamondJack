package pl.pzagawa.diamond.jack.ui;

import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.Settings;
import pl.pzagawa.diamond.jack.R;
import android.app.TabActivity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public abstract class CommonTabActivity
	extends TabActivity
{
	private LinearLayout llBackground;
	private ImageView imageLogo;
	private TextView labelHeader;

	protected ActivityStarter starter;	
	protected Settings settings;
		
	private boolean isActivityResumed = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.starter = new ActivityStarter(this);
		
		settings = MainApplication.getSettings();
		settings.load();		
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
		//get subviews AFTER inflating main view in onCreate
		llBackground = (LinearLayout)this.findViewById(R.id.llBackground);
		imageLogo = (ImageView)this.findViewById(R.id.imageLogo);
		labelHeader = (TextView)this.findViewById(R.id.labelHeader);		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		updateLayout();
		
		updateHeaderLayout();
		
		isActivityResumed = true;
	
		onLoadData(getCurrentTab());		
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		isActivityResumed = false;		
	}	
	
	protected boolean isLandScape()
	{
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		
		int width = display.getWidth();
		int height = display.getHeight();
		
		return (width > height);		
	}
	
	private void updateHeaderLayout()
	{
		if (isLandScape())
		{
			labelHeader.setVisibility(View.GONE);
		} else {
			labelHeader.setVisibility(View.VISIBLE);
		}
	}
	
	private void updateLayout()
	{
		final boolean landscape = isLandScape();
		
		llBackground.setGravity(landscape ? Gravity.CENTER : Gravity.TOP);
		
		if (imageLogo != null)
			imageLogo.setVisibility(landscape ? View.GONE : View.VISIBLE); 
	}

	public TabSpec addTab(String text, String tag, int iconResId, int tabContentResId)
	{
        TabHost tabHost = getTabHost();
        
        tabHost.setOnTabChangedListener(onTabChangeEvent);
        
        Drawable tabIcon = getResources().getDrawable(iconResId);
        
        TabSpec tab =  tabHost.newTabSpec(tag);
        tab.setIndicator(text, tabIcon);
        tab.setContent(tabContentResId);        

        tabHost.addTab(tab);
                
        return tab;
	}

	public void setCurrentTab(int index)
	{
        TabHost tabHost = getTabHost();        
        tabHost.setCurrentTab(index);        
	}

	public int getCurrentTab()
	{
        TabHost tabHost = getTabHost();
		return tabHost.getCurrentTab();
	}
		
	private TabHost.OnTabChangeListener onTabChangeEvent = new TabHost.OnTabChangeListener()
	{
		@Override
		public void onTabChanged(String tabId)
		{
			if (!isActivityResumed)
				return;
			
			onTabChangedEvent(tabId);
		}		
	};

	protected void onTabChangedEvent(String tabId)
	{
		onLoadData(Integer.parseInt(tabId));
	}
	
	protected abstract void onLoadData(final int tabId);
	
}
