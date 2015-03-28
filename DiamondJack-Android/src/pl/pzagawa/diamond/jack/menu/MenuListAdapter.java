package pl.pzagawa.diamond.jack.menu;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class MenuListAdapter
	implements ListAdapter
{
	private final LayoutInflater inflater;
	private final ArrayList<MenuListItem> list;
	private final int itemViewResId;
	
	public MenuListAdapter(Context context, ArrayList<MenuListItem> list, int itemViewResId)
	{
		this.inflater = LayoutInflater.from(context);		
		this.list = list;
		this.itemViewResId = itemViewResId;
	}	
	
	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return list.get(position).getId();
	}

	@Override
	public int getItemViewType(int position)
	{
		//view type 0 (only one type)
		return 0;
	}
	
	@Override
	public int getViewTypeCount()
	{
		//list shows one view type
		return 1;
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}

	@Override
	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer)
	{
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer)
	{
	}

	@Override
	public boolean areAllItemsEnabled()
	{
		return true;
	}

	@Override
	public boolean isEnabled(int position)
	{
		return true;
	}
	
	//views item cache
	public static abstract class ItemViewHolder
	{
		private View convertView;

		public ItemViewHolder(LayoutInflater inflater, int itemResourceId)
		{
			this.convertView = inflater.inflate(itemResourceId, null);
			initializeViews(convertView);
		}
		
		public View getConvertView()
		{
			return convertView;
		}
				
		public abstract void initializeViews(View convertView);
		public abstract void mapItemToViews(MenuListItem item);
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		//ViewHolder keeps references to children views to avoid unneccessary
		//calls to findViewById() on each row
		ItemViewHolder holder;

		MenuListItem item;
		
        //when convertView is not null, we can reuse it directly, there is no need
		//to reinflate it only inflate a new View when the convertView supplied by
		//ListView is null              
        if (convertView == null)
        {
        	item = list.get(position);
        	
       		holder = new MenuListItemViewHolder(inflater, itemViewResId);
        	
        	convertView = holder.getConvertView();
        	
            convertView.setTag(holder);            
        } else {
            //get the ViewHolder back to get fast access to the TextView
            holder = (ItemViewHolder)convertView.getTag();            
        }

        item = list.get(position);

        holder.mapItemToViews(item);
        
        return convertView;
	}
	
}
