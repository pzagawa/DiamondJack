package pl.pzagawa.game.engine.gfx;

import pl.pzagawa.game.engine.GameInstance;

public class GameScreen
	extends Screen
{
	private static boolean[] enabledButtons =
	{
		//button indexes: LEFT, RIGHT, UP, DOWN, ACTION1, ACTION2
		true, true, false, false, true, false,
	};
		
	public GameScreen(GameInstance game)
	{
		super(game, enabledButtons);
	}

	@Override
	public void dispose()
	{
		super.dispose();
	}
  
	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);		
	}
	
	@Override
	public void render()
	{
		super.render();
	}
	
	@Override
	public boolean isFinished()
	{
		return false;
	}
	
}
