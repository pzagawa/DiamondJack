package pl.pzagawa.diamond.jack.ui;

import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.Settings;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.Utils;
import pl.pzagawa.diamond.jack.menu.MenuList;
import pl.pzagawa.diamond.jack.menu.MenuListItem;
import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class CommonActivity
	extends Activity
{
	private ImageView imageLogo;
	private MenuList menuList;
	private TextView labelGameLink;
	private TextView labelGameLinkHeader;

	protected ActivityStarter starter;
	protected Settings settings;
	
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
						
		setupUI();
		
		MainApplication.openDatabase(this);		
	}
	
	private void setupUI()
	{
		//get subviews AFTER inflating main view in onCreate
		imageLogo = (ImageView)this.findViewById(R.id.imageLogo);

		//initialize menu list
		menuList = MenuList.create(this, R.id.menuList, R.layout.menu_list_item);
		if (menuList != null)
			menuList.setOnClickListener(menuEvents);
		
		//get game web link labels
		labelGameLink = (TextView)this.findViewById(R.id.labelGameLink);
		labelGameLinkHeader = (TextView)this.findViewById(R.id.labelGameLinkHeader);
				
		//setup link event
		if (labelGameLink != null)
		{
			labelGameLink.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Utils.openWebsite(CommonActivity.this, getString(R.string.app_website));
				}				
			});			
		}		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
				
		updateLayout();		
	}

	protected boolean isLandScape()
	{
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		
		int width = display.getWidth();
		int height = display.getHeight();
		
		return (width > height);		
	}
	
	private void updateLayout()
	{
		final boolean landscape = isLandScape();
				
		if (imageLogo != null)
			imageLogo.setVisibility(landscape ? View.GONE : View.VISIBLE); 
		
		if (labelGameLink != null)
			labelGameLink.setVisibility(View.VISIBLE);
		
		if (labelGameLinkHeader != null)
			labelGameLinkHeader.setVisibility(landscape ? View.GONE : View.VISIBLE);
	}
	
	protected void addMenuItem(int id, int stringResId)
	{
		if (menuList == null)
			return;

		final String text = getString(stringResId);
		
		menuList.addItem(new MenuListItem(id, text));	
	}
	
	protected void setMenuItemDescription(int id, String text)
	{		
		if (menuList == null)
			return;
		
		MenuListItem item = menuList.getMenuItem(id);
		
		if (item == null)
			return;
		
		item.setDescription(text);		
	}
	
	protected void menuUpdate()
	{
		if (menuList == null)
			return;
		
		menuList.update();
	}
    
	private MenuList.Events menuEvents = new MenuList.Events()
	{
		@Override
		public void onMenuItem(MenuListItem menuItem)
		{
			CommonActivity.this.onMenuItem(menuItem);
		}
	};
    
	public void onMenuItem(MenuListItem menuItem)
	{
	}
	
}
