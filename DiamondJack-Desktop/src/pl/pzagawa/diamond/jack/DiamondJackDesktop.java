package pl.pzagawa.diamond.jack;

import pl.pzagawa.diamond.jack.game.DiamondJack;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.GameStartupParams;
import pl.pzagawa.game.engine.map.LevelDataLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DiamondJackDesktop
{
	public static void main(String[] args)
	{		
		GameInstance game = createGame(); 

		new LwjglApplication(game, game.getName(), game.roomWidth(), game.roomHeight(), false);
	}

	private static GameInstance createGame()
	{		
		LevelDataLoader loader = new DesktopLevelDataLoader();
		
		GameStartupParams params = new GameStartupParams();
		
		params.levelId = 30010;

		return new DiamondJack(params, loader, gameAppEvents);
	}	
	
	private static GameInstance.ApplicationEvents gameAppEvents = new GameInstance.ApplicationEvents()
	{		
		@Override
		public void onGameExit(int resultCode)
		{
			Gdx.app.exit();
		}
	};
	
}
