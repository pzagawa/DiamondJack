package pl.pzagawa.diamond.jack.activities;

import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.portal.UserProfile;
import pl.pzagawa.diamond.jack.ui.CommonTab;
import pl.pzagawa.diamond.jack.ui.CommonTabActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileActivityTabAccount
	extends CommonTab
{	
	private TextView textNickname;
	private TextView textEmail;
	private TextView textAccountType;
	private TextView textAccountTypeDescription;
	
	private TextView textBannedComment;
	
	private LinearLayout accountDetailsView;
	private LinearLayout accountBannedStatus;
	private LinearLayout accountOfflineStatus;
	private LinearLayout accountUpgradeHints;
	
	public ProfileActivityTabAccount(CommonTabActivity parent)
	{
		super(parent);
		     
		//account details
    	this.textNickname = (TextView)parent.findViewById(R.id.textNickname);
    	this.textEmail = (TextView)parent.findViewById(R.id.textEmail);
    	this.textAccountType = (TextView)parent.findViewById(R.id.textAccountType);
    	this.textAccountTypeDescription = (TextView)parent.findViewById(R.id.textAccountTypeDescription);
    	
    	this.textBannedComment = (TextView)parent.findViewById(R.id.textBannedComment);
    	
    	this.accountDetailsView = (LinearLayout)parent.findViewById(R.id.accountDetailsView);
    	this.accountBannedStatus = (LinearLayout)parent.findViewById(R.id.accountBannedStatus);
    	this.accountOfflineStatus = (LinearLayout)parent.findViewById(R.id.accountOfflineStatus);
    	this.accountUpgradeHints = (LinearLayout)parent.findViewById(R.id.accountUpgradeHints);
	}
		
	@Override
	protected int getTabIndex()
	{
		return 1;
	}
	
	@Override
	protected int getTabTextResId()
	{
		return R.string.tab_account;
	}

	@Override
	protected int getTabIconResId()
	{
		return R.drawable.tab_account;
	}

	@Override
	protected int getTabContentResId()
	{
		return R.id.contentTab0;
	}	
	
	@Override
	public void onLoadData()
	{
		final UserProfile userProfile = MainApplication.getUserProfile();
		
		accountDetailsView.setVisibility(View.GONE);
		accountBannedStatus.setVisibility(View.GONE);
		accountOfflineStatus.setVisibility(View.GONE);
		
		//show account hints
		accountUpgradeHints.setVisibility(View.VISIBLE);		
		if (MainApplication.isUserProfileOnline())
			if (userProfile.accessRights.isPremiumAccount())		
				accountUpgradeHints.setVisibility(View.GONE);
		
		//show account details if online
		if (MainApplication.isUserProfileOnline())
		{
			if (userProfile.account.isBanned)
			{
				textBannedComment.setText(userProfile.account.bannedComment);
				
				accountBannedStatus.setVisibility(View.VISIBLE);
				return;
			}
			else
			{
				//show account details			
				textNickname.setText(userProfile.account.nickName);
				textEmail.setText(userProfile.account.email);			
		    	textAccountType.setText(userProfile.accessRights.getName());
		    	textAccountTypeDescription.setText(userProfile.accessRights.getDescription());
	
				accountDetailsView.setVisibility(View.VISIBLE);				
		    	return;
			}
		}

		//show OFFLINE data
		if (MainApplication.isUserProfileOffline())
		{			
			accountOfflineStatus.setVisibility(View.VISIBLE);
			return;
		}		
	}
	
}
