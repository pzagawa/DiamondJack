package pl.pzagawa.diamond.jack.menu;

import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.menu.MenuListAdapter.ItemViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MenuListItemViewHolder
	extends ItemViewHolder
{
	private TextView label;
	private TextView description;
	
	public MenuListItemViewHolder(LayoutInflater inflater, int itemResourceId)
	{
		super(inflater, itemResourceId);
	}

	@Override
	public void initializeViews(View convertView)
	{
		label = (TextView)convertView.findViewById(R.id.label);
		description = (TextView)convertView.findViewById(R.id.description);
	}

	@Override
	public void mapItemToViews(MenuListItem item)
	{
		label.setText(item.getText());
		
		String descriptionText = item.getDescription();
		
		if (descriptionText == null)
		{
			description.setVisibility(View.GONE);
		} else {
			description.setVisibility(View.VISIBLE);
			description.setText(descriptionText);			
		}
	}

}
