package pl.pzagawa.gae.auth;

public enum AuthManagerError
{
	//generic authorization error
	ERROR("authorization error"),
	
	//no google account available on system
	NO_GOOGLE_ACCOUNTS_AVAILABLE("no google accounts available"),
	
	//more than one account; select default
	SELECT_ACCOUNT("select account for authorization");
	
	private final String msg;
	
	AuthManagerError(String msg)
	{
		this.msg = msg;		
	}
	
	public String getMessage()
	{
		return msg;
	}

}
