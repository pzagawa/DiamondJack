package pl.pzagawa.game.engine;

import pl.pzagawa.game.engine.controls.UserControls;
import pl.pzagawa.game.engine.map.tiles.TileTexture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class CommonData
{
	public final static String GFX = "data/gfx/";
	public final static String EFFECTS = "data/gfx/effects/";
	public final static String ENEMIES = "data/gfx/enemies/";
	public final static String ENV = "data/gfx/env/";
	public final static String FONTS = "data/fonts/";
	
	private static CommonData data = new CommonData();

	public BitmapFont debugFont;
	public BitmapFont fontMain;
	
	public TileTexture controlsTiles;
	public Texture controlsTexture;
	
	public TileTexture drawingTiles;
	public Texture drawingTexture;
	
	public TileTexture osdTiles;
	public TileTexture levelTitleTiles;
	public TileTexture menuItemTiles;
	
	public TileTexture coverScreenTileSet;
	
	private CommonData()
	{
	}

	public static CommonData get()
	{
		return data;
	}

	public void load()
	{	
		//set debug font
		debugFont = new BitmapFont(Gdx.files.internal(FONTS + "font16.fnt"), Gdx.files.internal(FONTS + "font16.png"), false);		
		debugFont.setColor(Color.WHITE);
		
		//set fontMain
		fontMain = new BitmapFont(Gdx.files.internal(FONTS + "fontMain.fnt"), Gdx.files.internal(FONTS + "fontMain.png"), false);		
		fontMain.setColor(Color.WHITE);

		//set controls tile set data
		controlsTiles = new TileTexture();		
		controlsTiles.load(GFX + "controls.png", UserControls.BUTTON_WIDTH, UserControls.BUTTON_HEIGHT);
		controlsTexture = controlsTiles.getTexture();
		
		//set drawing tile set data for Renderer.drawBox, etc..
		drawingTiles = new TileTexture();
		drawingTiles.load(GFX + "drawing.png", 2, 2);
		drawingTexture = drawingTiles.getTexture();

		//set OSD tile layer
		osdTiles = new TileTexture();
		osdTiles.load(GFX + "osd-alpha.png", 16, 24);
		
		//set level title tile layer
		levelTitleTiles = new TileTexture();
		levelTitleTiles.load(GFX + "level-title.png", 32, 32);
		
		//set menu item tile layer
		menuItemTiles = new TileTexture();
		menuItemTiles.load(GFX + "menu/menu-item.png", 32, 32);

		//cover screen
		coverScreenTileSet = new TileTexture();
		coverScreenTileSet.load(GFX + "cover-screen.png", 64, 64);
	}

	public void dispose()
	{
		menuItemTiles.dispose();
		levelTitleTiles.dispose();
		debugFont.dispose();
		fontMain.dispose();
		controlsTiles.dispose();
		drawingTiles.dispose();
		osdTiles.dispose();
	}
	
}
