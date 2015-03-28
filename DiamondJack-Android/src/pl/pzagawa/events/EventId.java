package pl.pzagawa.events;

import pl.pzagawa.events.Event.Type;

public enum EventId
{
	DATABASE_UPDATE(Type.OK),
	
	GAME_UPDATE_FINISH(Type.OK),
	GAME_UPDATE_FINISH_WITH_ERROR(Type.ERROR),
	
	GAME_UPDATE_LOG_MESSAGE(Type.OK),
	
	REQ_GET_MOBILE_PROFILE(Type.OK),
	REQ_GET_LEVELS(Type.OK),
	REQ_GET_ONE_LEVEL(Type.OK),
	REQ_SYNC_STATS(Type.OK),
	
	AUTHORIZATION(Type.OK),
	REQUEST_SUCCESS(Type.OK),
	REQUIRED_USER_INPUT(Type.OK, "required user input"),
	AUTHORIZATION_READY(Type.OK, "authorization finished"),
	SELECT_ACCOUNT(Type.OK, "select account for authorization"),
	REQUEST_FAILURE(Type.ERROR),
	AUTHORIZATION_ERROR(Type.ERROR, "authorization error"),
	CONNECTION_ERROR(Type.ERROR, "connection error"),
	NO_GOOGLE_ACCOUNTS_AVAILABLE(Type.ERROR, "no google accounts available");
	
	private final String text;
	private final Type type;

	EventId(Type type)
	{
		this.text = "";
		this.type = type;
	}
	
	EventId(Type type, String text)
	{
		this.type = type;
		this.text = text;		
	}
	
	public String getText()
	{
		return text;
	}
	
	public Type getType()
	{
		return type;
	}
	
}
