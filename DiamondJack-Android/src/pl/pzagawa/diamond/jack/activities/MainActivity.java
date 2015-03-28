package pl.pzagawa.diamond.jack.activities;

import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.MenuItemId;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.menu.MenuListItem;
import pl.pzagawa.diamond.jack.ui.CommonActivity;
import pl.pzagawa.gae.client.RequestGetMobileProfile;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity
	extends CommonActivity
{
	private ImageView titleIconLeft;
	private ImageView titleIconRight;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleIconLeft = (ImageView)this.findViewById(R.id.titleIconLeft);
        titleIconRight = (ImageView)this.findViewById(R.id.titleIconRight);
    }

	@Override
	protected void onStart()
	{
		super.onStart();
		
    	addMenuItem(MenuItemId.NEW_GAME, R.string.menu_item_new_game);
    	addMenuItem(MenuItemId.PROFILE, R.string.menu_item_profile);
    	addMenuItem(MenuItemId.OPTIONS, R.string.menu_item_options);
    	addMenuItem(MenuItemId.ABOUT, R.string.menu_item_about);

    	menuUpdate();
    	
    	openUpdaterIfRequired();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		final boolean landscape = isLandScape();
		
		titleIconLeft.setVisibility(landscape ? View.VISIBLE : View.GONE); 
		titleIconRight.setVisibility(landscape ? View.VISIBLE : View.GONE);		
	}
	
	@Override
	public void onMenuItem(MenuListItem menuItem)
	{
		switch (menuItem.getId())
		{
			case MenuItemId.NEW_GAME:
				starter.open(LevelsActivity.class);
				return;
			case MenuItemId.PROFILE:
				starter.open(ProfileActivity.class);
				return;
			case MenuItemId.OPTIONS:
				starter.open(OptionsActivity.class);
				return;
			case MenuItemId.ABOUT:
				starter.open(AboutActivity.class);
				return;
		}
	}

	private void openUpdaterIfRequired()
	{
		if (MainApplication.isUpdaterRequiredToRun())
		{
	    	UpdaterActivity.open(MainActivity.this, RequestGetMobileProfile.LEVELS_RETURN_ALL);
		}
	}

}
