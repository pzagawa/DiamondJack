package pl.pzagawa.events;

import android.content.Intent;

public class Event
{
	public enum Type
	{
		ERROR,
		OK,
	}
	
	private final EventId id;
	private final String description;
	private final Intent intent;	
	
	private boolean isFinish = false;
	private boolean isSuccessResult = false;
	
	public Event(EventId id)
	{
		this.id = id;
		this.description = "";
		this.intent = null;
	}

	public Event(EventId id, String description)
	{
		this.id = id;
		this.description = description;
		this.intent = null;
	}

	public Event(EventId id, Intent intent)
	{
		this.id = id;
		this.description = "";
		this.intent = intent;
	}
	
	public EventId getId()
	{
		return 	id;	
	}

	public String getDescription()
	{
		return description;
	}
	
	public Type getType()
	{
		return 	id.getType();	
	}
	
	public Intent getIntent()
	{
		return intent;
	}

	public boolean isTypeError()
	{
		return (getType() == Type.ERROR);
	}

	public boolean isTypeSuccess()
	{
		return (getType() == Type.OK);
	}
	
	public void finish()
	{
		this.isFinish = true;		
	}

	public boolean isFinish()
	{
		return this.isFinish;		
	}

	public boolean isStart()
	{
		return !this.isFinish;		
	}

	public boolean isSuccessResult()
	{
		return isSuccessResult;		
	}

	public void setSuccessResult()
	{
		isSuccessResult = true;		
	}
	
	@Override
	public String toString()
	{
		return id + ": " + id.getText();		
	}
	
}
