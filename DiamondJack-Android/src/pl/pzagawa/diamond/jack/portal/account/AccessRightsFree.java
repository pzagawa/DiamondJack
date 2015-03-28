package pl.pzagawa.diamond.jack.portal.account;

import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.R;

public class AccessRightsFree
	extends AccessRights
{
	public AccessRightsFree()
	{
		this.canModifyLevel = false;
		this.canDownloadPublicLevels = false;
		this.canDownloadPrivateLevels = false;
		this.canPublishLevel = false;
		this.canSyncStats = false;
	}

	@Override
	public int getType()
	{
		return AccountType.FREE;
	}
	
	@Override
	public String getName()
	{
		return "free";
	}

	@Override
	public String getDescription()
	{
		return MainApplication.getContext().getString(R.string.accessRightsDescAccountFree);
	}	

}
