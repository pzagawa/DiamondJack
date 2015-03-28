package pl.pzagawa.diamond.jack.game;

import pl.pzagawa.diamond.jack.gfx.RendererBackground;
import pl.pzagawa.diamond.jack.gfx.RendererEffects;
import pl.pzagawa.diamond.jack.gfx.RendererEnemies;
import pl.pzagawa.diamond.jack.gfx.RendererGround;
import pl.pzagawa.diamond.jack.gfx.RendererGroundShape;
import pl.pzagawa.diamond.jack.gfx.RendererLevelTitle;
import pl.pzagawa.diamond.jack.gfx.RendererOSD;
import pl.pzagawa.diamond.jack.gfx.RendererObjects;
import pl.pzagawa.diamond.jack.gfx.RendererPlayer;
import pl.pzagawa.diamond.jack.gfx.RendererScreenMessage;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.GameStartupParams;
import pl.pzagawa.game.engine.controls.RendererControls;
import pl.pzagawa.game.engine.gfx.GameScreen;
import pl.pzagawa.game.engine.gfx.RendererDebugText;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.LevelDataLoader;

public class DiamondJack
	extends GameInstance
{	
	public static final int SCREEN_WIDTH = 480; 
	public static final int SCREEN_HEIGHT = 320; 
	
	public DiamondJack(GameStartupParams params, LevelDataLoader loader, ApplicationEvents events)
	{
		super(params, loader, events);
	}
	
	@Override
	public void create()
	{
		super.create();
		
		//set screen
		Screen screen = new GameScreen(this);
		setScreen(screen);
		
		//add renderers
		screen.addRenderer(new RendererBackground(this));
		screen.addRenderer(new RendererGround(this));
		screen.addRenderer(new RendererGroundShape(this));
		screen.addRenderer(new RendererObjects(this));
		screen.addRenderer(new RendererEnemies(this));
		screen.addRenderer(new RendererPlayer(this));
		screen.addRenderer(new RendererEffects(this));
		screen.addRenderer(new RendererControls(this));	
		screen.addRenderer(new RendererOSD(this));
		screen.addRenderer(new RendererLevelTitle(this));
		screen.addRenderer(new RendererScreenMessage(this));
		
		//screen.addRenderer(new RendererCoverScreen(this));
		
		if (this.TEST_MODE > -1)
		{
			screen.addRenderer(new RendererDebugText(this));
		}
		
		processStartupParams();
	}
	
	@Override
	public void resume()
	{
		super.resume();
	}
	
	@Override
	public void pause()
	{
		super.pause();
	}

	@Override
	public int roomWidth()
	{
		return SCREEN_WIDTH;
	}
	
	@Override
	public int roomHeight()
	{
		return SCREEN_HEIGHT;
	}

	@Override
	public String getName()
	{
		return "Diamond Jack";
	}
	
}
