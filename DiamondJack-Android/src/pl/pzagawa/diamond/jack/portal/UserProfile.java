package pl.pzagawa.diamond.jack.portal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.pzagawa.diamond.jack.portal.account.AccessRights;

public class UserProfile
{
	public final UserAccount account;
	public final AccessRights accessRights; 

	public final UserLevels publicLevels;
	public final UserLevels privateLevels;	

	public UserProfile()
	{
		//create empty objects for offline mode
		this.account = new UserAccount();
		this.accessRights = AccessRights.createByAccountType(this.account.accountType);
		
		this.publicLevels = new UserLevels();
		this.privateLevels = new UserLevels();
	}
	
	public UserProfile(JSONObject jProfile)
		throws JSONException
	{
		//get json objects
		final JSONObject jUser = jProfile.getJSONObject("user");
		final JSONArray jPublicLevels = jProfile.getJSONArray("publicLevels");
		final JSONArray jPrivateLevels = jProfile.getJSONArray("privateLevels");
		
		//parse objects
		this.account = new UserAccount(jUser);
		this.accessRights = AccessRights.createByAccountType(this.account.accountType);

		this.publicLevels = new UserLevels(jPublicLevels);
		this.privateLevels = new UserLevels(jPrivateLevels);
	}

}
