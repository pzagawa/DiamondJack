package pl.pzagawa.diamond.jack.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MenuList
{
	public interface Events
	{
		void onMenuItem(MenuListItem menuItem);
	}

	private final Activity parent;
	private final int menuItemResId;
	
	private ArrayList<MenuListItem> items = new ArrayList<MenuListItem>();
	private Map<Integer, MenuListItem> itemsMap = new HashMap<Integer, MenuListItem>();
	
	private ListView list;
	private MenuListAdapter listAdapter;
	
	private Events events;

	public static MenuList create(Activity parent, int menuListResId, int menuItemResId)
	{
		ListView list = (ListView)parent.findViewById(menuListResId);
		
		if (list != null)
			return new MenuList(parent, menuListResId, menuItemResId);
		
		return null;
	}
	
	private MenuList(final Activity parent, int menuListResId, int menuItemResId)
	{
		this.parent = parent;
		this.menuItemResId = menuItemResId;

	    list = (ListView)parent.findViewById(menuListResId);
	    list.setOnItemClickListener(onClickListener);
	    
	    update();
	}
	
	private ListView.OnItemClickListener onClickListener = new ListView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			if (events != null)
			{
				MenuListItem menuItem = itemsMap.get((int)id);
				events.onMenuItem(menuItem);
			}
		}
	};
	
	public void update()
	{
	    listAdapter = new MenuListAdapter(parent, items, menuItemResId);
	    list.setAdapter(listAdapter);
	}
	
	public void clear()
	{
		items.clear();
		itemsMap.clear();
	}
	
	public void addItem(MenuListItem menuItem)
	{
		items.add(menuItem);
		itemsMap.put(menuItem.getId(), menuItem);
	}
	
	public void setOnClickListener(Events events)
	{
		this.events = events;
	}
	
	public MenuListItem getMenuItem(int id)
	{
		if (itemsMap.containsKey(id))
			return itemsMap.get(id);
		
		return null;
	}
	
}
