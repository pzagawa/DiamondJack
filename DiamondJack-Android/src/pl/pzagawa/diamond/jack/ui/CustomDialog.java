package pl.pzagawa.diamond.jack.ui;

import pl.pzagawa.diamond.jack.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CustomDialog
{
	public static class Events
	{
		public void onPositiveButtonClick(DialogInterface dialog)
		{
		}
		public void onNegativeButtonClick(DialogInterface dialog)
		{
		}		
		public void onListItemClick(DialogInterface dialog, int id)
		{
		}
	};
	
	protected final Context context;
	private final AlertDialog.Builder builder;
	
	public Events events;
	
	public CustomDialog(Context context)
	{
		this.context = context;
		this.builder = new AlertDialog.Builder(context);

		enableButtons();
	}
	
	public CustomDialog(Context context, String title)
	{
		this.context = context;
		this.builder = new AlertDialog.Builder(context);
		this.builder.setTitle(title);

		enableButtons();
	}

	public CustomDialog(Context context, int stringResId, CharSequence[] items, int checkedItem)
	{
		this.context = context;
		this.builder = new AlertDialog.Builder(context);
		this.builder.setTitle(context.getString(stringResId));

		setSingleChoiceItems(items, checkedItem);
	}
	
	public void setMessage(String text)
	{
		builder.setMessage(text);
	}

	public void setTitle(String text)
	{
		builder.setTitle(text);
	}

	public void disableCancelation()
	{
		builder.setCancelable(false);
	}

	public void create()
	{
		builder.create();
	}
	
	public void show()
	{
		builder.show();
	}

	public void enableButtons()
	{		
		builder.setPositiveButton(context.getString(R.string.dialog_button_ok), onButtonClick);
		builder.setNegativeButton(context.getString(R.string.dialog_button_cancel), onButtonClick);
	}
	
	public void setSingleChoiceItems(CharSequence[] items, int checkedItem)
	{
		builder.setSingleChoiceItems(items, checkedItem, onButtonClick);		
	}
	
	private DialogInterface.OnClickListener onButtonClick = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int id)
		{
			if (events != null)
			{
				if (id == DialogInterface.BUTTON_POSITIVE)
				{
					events.onPositiveButtonClick(dialog);
					return;
				}
				
				if (id == DialogInterface.BUTTON_NEGATIVE)
				{
					events.onNegativeButtonClick(dialog);
					return;
				}
				
				events.onListItemClick(dialog, id);
			}
        }
	};
	
}
