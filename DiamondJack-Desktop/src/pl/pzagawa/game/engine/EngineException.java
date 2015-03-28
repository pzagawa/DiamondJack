package pl.pzagawa.game.engine;

public class EngineException
	extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public EngineException (String message)
  {
  	super(message);
  }

	public EngineException (Throwable t)
	{
		super(t);
	}

	public EngineException (String message, Throwable t)
	{
		super(message, t);
	}
	
}
