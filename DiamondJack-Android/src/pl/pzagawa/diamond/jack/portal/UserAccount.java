package pl.pzagawa.diamond.jack.portal;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import pl.pzagawa.diamond.jack.portal.account.AccountType;

public class UserAccount
{
	public final boolean isOffline;
	
	public final String email;
	public final Date createDate;
	public final String nickName;
	public final String city;
	public final String country;
	public final int accountType;
	public final Date unlockDate;
	public final boolean isBanned;
	public final String bannedComment;
	public final String userId;

	public UserAccount()
	{
		this.isOffline = true;
		
		this.email = null;
		this.createDate = null;		
		this.nickName = null;
		this.city = null;
		this.country = null;
		this.accountType = AccountType.FREE;		
		this.unlockDate = null;
		this.isBanned = false;
		this.bannedComment = "";
		this.userId = "";
	}
	
	public UserAccount(JSONObject jUser)
		throws JSONException
	{
		this.isOffline = false;
		
		this.email = jUser.getString("email");		
		this.createDate = new Date(jUser.getLong("createDate"));		
		this.nickName = jUser.getString("nickName");
		this.city = jUser.optString("city", "");
		this.country = jUser.optString("country", "");
		this.accountType = jUser.getInt("accountType");
		
		final long unlockDateMs = jUser.optLong("unlockDate", 0);
		
		if (unlockDateMs == 0)
			this.unlockDate = null;
		else
			this.unlockDate = new Date(unlockDateMs);
		
		this.isBanned = jUser.optBoolean("isBanned", false);
		this.bannedComment = jUser.optString("bannedComment", "");		

		this.userId = jUser.getString("userId");		
	}
	
	public boolean isTypeFree()
	{
		return (accountType == AccountType.FREE);
	}

	public boolean isTypeBeta()
	{
		return (accountType == AccountType.BETA);
	}
	
	public boolean isTypePremium()
	{
		return (accountType == AccountType.PREMIUM);
	}	

	@Override
	public String toString()
	{
		String account = "free";
		
		if (isTypePremium())
			account = "premium";
		
		if (isTypeBeta())
			account = "beta";
		
		if (isOffline)
			return "offline account: " + account;

		return nickName + "/" + email + ", account: " + account;
	}
	
}
