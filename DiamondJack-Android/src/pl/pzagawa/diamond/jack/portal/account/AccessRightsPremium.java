package pl.pzagawa.diamond.jack.portal.account;

import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.R;

public class AccessRightsPremium
	extends AccessRights
{
	public AccessRightsPremium()
	{
		this.canModifyLevel = true;
		this.canDownloadPublicLevels = true;
		this.canDownloadPrivateLevels = true;
		this.canPublishLevel = true;
		this.canSyncStats = true;
	}

	@Override
	public int getType()
	{
		return AccountType.PREMIUM;
	}
	
	@Override
	public String getName()
	{
		return "premium";
	}
	
	@Override
	public String getDescription()
	{
		return MainApplication.getContext().getString(R.string.accessRightsDescAccountPremium);
	}
	
}
