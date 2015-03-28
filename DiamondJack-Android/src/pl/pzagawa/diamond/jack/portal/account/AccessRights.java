package pl.pzagawa.diamond.jack.portal.account;

public abstract class AccessRights
{
	protected boolean canModifyLevel = false;
	protected boolean canDownloadPublicLevels = false;
	protected boolean canDownloadPrivateLevels = false;
	protected boolean canPublishLevel = false;	
	protected boolean canSyncStats = false;

	public boolean isPremiumAccount()
	{
		return (getType() == AccountType.PREMIUM);
	}
	
	public boolean canModifyLevel()
	{
		return canModifyLevel;
	}
	
	public boolean canPublishLevel()
	{
		return canPublishLevel;
	}
	
	public boolean canSyncStats()
	{
		return canSyncStats;
	}

	public boolean canDownloadPublicLevels()
	{
		return canDownloadPublicLevels;
	}

	public boolean canDownloadPrivateLevels()
	{
		return canDownloadPrivateLevels;
	}
	
	@Override
	public String toString()
	{
		return getName() + ": " + 
			(canModifyLevel() ? "modify level, " : "") +
			(canPublishLevel() ? "publish level, " : "") +
			(canSyncStats() ? "store stats, " : "") +
			(canDownloadPublicLevels() ? "download public levels, " : "") +
			(canDownloadPrivateLevels() ? "download private levels" : "");
	}
	
	public abstract int getType();
	public abstract String getName();
	public abstract String getDescription();

	public static AccessRights createByAccountType(final int accountType)
	{
		if (accountType == AccountType.FREE)
			return new AccessRightsFree();

		if (accountType == AccountType.BETA)
			return new AccessRightsBeta();

		if (accountType == AccountType.PREMIUM)
			return new AccessRightsPremium();

		return null;
	}

}
