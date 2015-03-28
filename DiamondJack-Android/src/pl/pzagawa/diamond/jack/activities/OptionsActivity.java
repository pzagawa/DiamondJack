package pl.pzagawa.diamond.jack.activities;

import pl.pzagawa.diamond.jack.MenuItemId;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.menu.MenuListItem;
import pl.pzagawa.diamond.jack.ui.CommonActivity;
import pl.pzagawa.diamond.jack.ui.CustomDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class OptionsActivity
	extends CommonActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }

	@Override
	protected void onStart()
	{
		super.onStart();
		
	  	//addMenuItem(MenuItemId.AUDIO, R.string.menu_item_audio);
	  	addMenuItem(MenuItemId.VIDEO, R.string.menu_item_video);
	  	addMenuItem(MenuItemId.VIBRATOR, R.string.menu_item_vibrator);
	  	//addMenuItem(MenuItemId.CONTROLS, R.string.menu_item_controls);
	  	
	  	//setMenuItemDescription(MenuItemId.AUDIO, prefs.getAudioStateText());
	  	setMenuItemDescription(MenuItemId.VIDEO, settings.getVideoModeText());
	  	setMenuItemDescription(MenuItemId.VIBRATOR, settings.getVibratorStateText());
	  	//setMenuItemDescription(MenuItemId.CONTROLS, prefs.getMultiTouchText());

	  	menuUpdate();
	}
	
	@Override
	public void onMenuItem(MenuListItem menuItem)
	{
		if (menuItem.getId() == MenuItemId.AUDIO)
			showOptionAudioDlg();

		if (menuItem.getId() == MenuItemId.VIDEO)
			showOptionVideoDlg();

		if (menuItem.getId() == MenuItemId.VIBRATOR)
			showOptionVibratorDlg();
		
		if (menuItem.getId() == MenuItemId.CONTROLS)
			showOptionControlsDlg();
	}

	private void showOptionAudioDlg()
	{
        CustomDialog dlg = new CustomDialog(this, R.string.menu_item_audio, settings.option_items, settings.getAudioState());
        
        dlg.events = new CustomDialog.Events()
        {
			@Override
			public void onListItemClick(DialogInterface dialog, int id)
			{
				settings.setAudioState(id);
			  	setMenuItemDescription(MenuItemId.AUDIO, settings.getAudioStateText());
			  	menuUpdate();			  	
				dialog.dismiss();
			}
        };

        dlg.show();
	}

	private void showOptionVideoDlg()
	{        
        CustomDialog dlg = new CustomDialog(this, R.string.menu_item_video, settings.video_items, settings.getVideoMode());
        
        dlg.events = new CustomDialog.Events()
        {
			@Override
			public void onListItemClick(DialogInterface dialog, int id)
			{
				settings.setVideoMode(id);
			  	setMenuItemDescription(MenuItemId.VIDEO, settings.getVideoModeText());
			  	menuUpdate();			  	
				dialog.dismiss();
			}
        };

        dlg.show();
	}

	private void showOptionVibratorDlg()
	{
        CustomDialog dlg = new CustomDialog(this, R.string.menu_item_vibrator, settings.option_items, settings.getVibratorState());
        
        dlg.events = new CustomDialog.Events()
        {
			@Override
			public void onListItemClick(DialogInterface dialog, int id)
			{
				settings.setVibratorState(id);
			  	setMenuItemDescription(MenuItemId.VIBRATOR, settings.getVibratorStateText());
			  	menuUpdate();
				dialog.dismiss();
			}
        };

        dlg.show();
	}
	
	private void showOptionControlsDlg()
	{
        CustomDialog dlg = new CustomDialog(this, R.string.menu_item_controls, settings.controls_items, settings.getMultiTouchState());
        
        dlg.events = new CustomDialog.Events()
        {
			@Override
			public void onListItemClick(DialogInterface dialog, int id)
			{
				settings.setMultiTouchState(id);
			  	setMenuItemDescription(MenuItemId.CONTROLS, settings.getMultiTouchText());
			  	menuUpdate();			  	
				dialog.dismiss();
			}
        };

        dlg.show();
	}
	
}
