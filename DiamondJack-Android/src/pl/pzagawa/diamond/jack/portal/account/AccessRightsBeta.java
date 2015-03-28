package pl.pzagawa.diamond.jack.portal.account;

import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.R;

public class AccessRightsBeta
	extends AccessRights
{
	public AccessRightsBeta()
	{
		this.canModifyLevel = true;
		this.canDownloadPublicLevels = true;
		this.canDownloadPrivateLevels = true;
		this.canPublishLevel = false;		
		this.canSyncStats = true;
	}

	@Override
	public int getType()
	{
		return AccountType.BETA;
	}
	
	@Override
	public String getName()
	{
		return "beta";
	}

	@Override
	public String getDescription()
	{
		return MainApplication.getContext().getString(R.string.accessRightsDescAccountBeta);
	}

}
