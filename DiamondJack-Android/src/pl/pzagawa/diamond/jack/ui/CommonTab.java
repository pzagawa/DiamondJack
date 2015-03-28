package pl.pzagawa.diamond.jack.ui;

import android.view.View;
import android.widget.ListView;

public abstract class CommonTab
{
	protected final CommonTabActivity parent;
	private final String textTab;
	
	public CommonTab(CommonTabActivity parent)
	{
		this.parent = parent;
		this.textTab = parent.getString(getTabTextResId());		
		this.parent.addTab(textTab, Integer.toString(getTabIndex()), getTabIconResId(), getTabContentResId());
	}

	public void updateEmptyView(ListView list, View emptyView)
	{
		emptyView.setVisibility(View.VISIBLE);
		list.setVisibility(View.GONE);

		if (isListEmpty(list))
			return;

		emptyView.setVisibility(View.GONE);
		list.setVisibility(View.VISIBLE);
	}
	
	public boolean isListEmpty(ListView list)
	{
		if (list.getAdapter() == null)
			return true;
		
		if (list.getAdapter() != null && list.getAdapter().isEmpty())
			return true;
		
		return false;		
	}
	
	public boolean isTabEqual(String tabId)
	{
		return (tabId.compareTo(Integer.toString(getTabIndex())) == 0);
	}
	
	public boolean isTabEqual(int tabId)
	{
		return (getTabIndex() == tabId);
	}
	
	public void setCurrentTab()
	{
		parent.setCurrentTab(getTabIndex());
	}
	
	protected abstract int getTabIndex();
	protected abstract int getTabTextResId();
	protected abstract int getTabIconResId();
	protected abstract int getTabContentResId();
	
	protected abstract void onLoadData();	

}
