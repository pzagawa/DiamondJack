package pl.pzagawa.diamond.jack.gfx;

import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.NumberToDigitsConverter;
import pl.pzagawa.game.engine.gfx.Renderer;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.LevelData;
import pl.pzagawa.game.engine.map.tiles.TileTexture;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.player.Inventory;
import com.badlogic.gdx.graphics.Texture;

public class RendererOSD
	extends Renderer
{
	private final int MARGIN = 8;
	private final int DIGITS_SPACE = 4;
	private final int NUMBER_DIGITS = 7;

	private Inventory inventory;
	
	private TileTexture osdTileSet;
	private Texture osdTexture;	
	private final int osdTileWidth;
	private final int osdTileHeight;
	
	private OSDTextureMap textureMap;	
	private NumberToDigitsConverter numberToDigits;
		
	public RendererOSD(GameInstance game)
	{
		super(game, true);
		
		this.inventory = game.getObjects().getInventory();
				
		this.osdTileSet = CommonData.get().osdTiles;
		this.osdTexture = osdTileSet.getTexture();
		
		this.osdTileWidth = osdTileSet.getTileWidth();
		this.osdTileHeight = osdTileSet.getTileHeight();
		
		this.textureMap = new OSDTextureMap(osdTileWidth, osdTileHeight);
		this.numberToDigits = new NumberToDigitsConverter(NUMBER_DIGITS);
	}
	
	@Override
	public void onLevelLoaded(LevelData level)
	{
	}	
	
	@Override
	public void onGameStateChange(int gameState)
	{		
	}
	
	@Override
	public void render(Screen screen, float speed, float shake)
	{
		final int score = inventory.getScore();
		
		final int key1Count = inventory.getKey1Count();
		final int key2Count = inventory.getKey2Count();
		
		float x = MARGIN;
		float y = (screen.getHeight() - osdTileHeight) - MARGIN;
		
		float line1 = y;
		float line2 = y - osdTileHeight - 10;
		
		screen.batch.begin();
		screen.batch.enableBlending();		
		
		//draw score text
		float scoreX = x;
		
		BoundingBox boxScore = textureMap.boxScore;
		screen.batch.draw(osdTexture, scoreX, line1, (int)boxScore.left(), (int)boxScore.top(), boxScore.getWidth(), boxScore.getHeight());

		//draw score number
		float digitsX = scoreX + boxScore.getWidth() + DIGITS_SPACE;

		drawNumber(screen, digitsX, line1, score);
		
		//draw inventory icons
		float iconsLeft = x;
		
		//draw key 1
		if (key1Count > 0)
			iconsLeft = drawInventoryIcon(screen, iconsLeft, line2, textureMap.boxKey1, key1Count);

		//draw key 2
		if (key2Count > 0)
			iconsLeft = drawInventoryIcon(screen, iconsLeft, line2, textureMap.boxKey2, key2Count);
		
		
		screen.batch.end();		
	}

	private int drawNumber(Screen screen, float left, float top, long value)
	{	
		numberToDigits.set(value);
	
		final int start = numberToDigits.getLeadingZeroesCount();
		
		for (int digitIndex = start; digitIndex < NUMBER_DIGITS; digitIndex++)
		{
			final int digitValue = numberToDigits.getDigitValue(digitIndex);
			
			BoundingBox boxDigit = textureMap.getDigitPos(digitValue);
			
			screen.batch.draw(osdTexture, left, top, (int)boxDigit.left(), (int)boxDigit.top(), osdTileWidth, osdTileHeight);
			
			left += osdTileWidth + DIGITS_SPACE;
		}
		
		return numberToDigits.getDigitsCount();
	}	
	
	private float drawInventoryIcon(Screen screen, float left, float top, BoundingBox boxIcon, int value)
	{	
		screen.batch.draw(osdTexture, left, top, (int)boxIcon.left(), (int)boxIcon.top(), boxIcon.getWidth(), boxIcon.getHeight());
				
		left += osdTileWidth + MARGIN + DIGITS_SPACE;
		
		final int digitsCount = drawNumber(screen, left, top, value);
		
		final int valueWidth = (osdTileWidth + DIGITS_SPACE) * (digitsCount - 1);

		return valueWidth + left + MARGIN + MARGIN;
	}
	
}
