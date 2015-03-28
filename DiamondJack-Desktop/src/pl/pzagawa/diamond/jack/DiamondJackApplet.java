package pl.pzagawa.diamond.jack;

import pl.pzagawa.diamond.jack.game.DiamondJack;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.GameStartupParams;
import pl.pzagawa.game.engine.map.LevelDataLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplet;

public class DiamondJackApplet
	extends LwjglApplet
{
	private static final long serialVersionUID = 1L;
    
	public DiamondJackApplet()
	{
        super(createGame(), false);
        
        AppletLevelDataLoader.applet = this;
	}
	
	@Override
	public void init()
	{
		super.init();
		
	}

	@Override
	public void start()
	{
		super.start();
		
	}
	
	private static GameInstance createGame()
	{
		GameInstance.IS_APPLET_VERSION = true;
		
		LevelDataLoader loader = new AppletLevelDataLoader();
		
		GameStartupParams params = new GameStartupParams();

		params.levelId = 1;

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

	@Override
	public String getAppletInfo()
	{
		return "Diamond Jack. Copyright © 2011 by Piotr Zagawa";
	}

}
