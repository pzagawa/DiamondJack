package pl.pzagawa.gae.auth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public abstract class SelectorDialog
{
	private int selectedItem = -1;
	private AlertDialog.Builder builder;
	
	public SelectorDialog(Activity activity, String title, String[] items)
	{	
		builder = initBuilder(activity, title);
				
	    builder.setItems(items, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				selectedItem = which;
				onClickButtonOK(selectedItem);
			}
		});
	}

	public SelectorDialog(Activity activity, String title, String[] items, int defaultSelectedItem)
	{	
		builder = initBuilder(activity, title);
				
	    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
	    {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				onClickButtonOK(selectedItem);
			}	    	
	    });
	    
	    builder.setSingleChoiceItems(items, defaultSelectedItem, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				selectedItem = which;
			}
		});
	}
	
	private AlertDialog.Builder initBuilder(Activity activity, String title)
	{
	    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    
	    builder.setTitle(title);
	    
	    builder.setOnCancelListener(new DialogInterface.OnCancelListener()
		{			
			@Override
			public void onCancel(DialogInterface dialog)
			{
				onClickButtonOK(-1);
			}
		});
	
	    return builder;
	}

	public void show()
	{
	    AlertDialog alert = builder.create();
	    alert.show();		
	}
	
	public abstract void onClickButtonOK(int selectedItem);

}
