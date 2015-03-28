package pl.pzagawa.game.engine.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.map.LevelData;
import pl.pzagawa.game.engine.map.tiles.TileTexture;
import pl.pzagawa.game.engine.objects.BoundingBox;

public abstract class RendererCenterText
	extends Renderer
{	
	protected String text = null;

	private BoundingBox boxText = new BoundingBox(0,0,0,0);
	private TextBounds textBounds = new TextBounds();
	
	private BitmapFont font;
	private TileTexture titleTileSet;
	private Texture titleTexture;	
	private final int titleTileWidth;
	private final int titleTileHeight;
		
	protected float alphaValue = 1.0f;
	
	public RendererCenterText(GameInstance game)
	{
		super(game, true);
		
		font = CommonData.get().fontMain;
		
		this.titleTileSet = CommonData.get().levelTitleTiles;
		this.titleTexture = titleTileSet.getTexture();

		this.titleTileWidth = titleTileSet.getTileWidth();
		this.titleTileHeight = titleTileSet.getTileHeight();		
	}

	@Override
	public void onLevelLoaded(LevelData level)
	{						
	}	

	public void setText(String text)
	{
		this.text = text;

		if (text != null)
			this.textBounds.set(font.getBounds(text));
	}
	
	@Override
	public void render(Screen screen, float speed, float shake)
	{
		if (text == null)
			return;

		final int screenWidth = screen.getWidth();
		final int screenHeight = screen.getHeight();
		
		final int screenCenterX = (screenWidth >> 1);
		final int screenCenterY = (screenHeight >> 1);
		
		final int boxHeight = titleTileSet.getTileCountY() * titleTileHeight;
		final int boxWidth = titleTileWidth * 13;
		
		boxText.setPosition(screenCenterX - (boxWidth >> 1), screenCenterY - boxHeight);
		boxText.setSize(boxWidth, boxHeight);
		
		float y = (screenHeight - titleTileHeight);
		
		float posX = boxText.left();
		float posY = y - boxText.top();
		
		screen.batch.begin();
		screen.batch.enableBlending();		
		
		Color oldColor = screen.batch.getColor();
		
		screen.batch.setColor(1, 1, 1, alphaValue);
		
		//get first tile
		int srcX = 0;
		int srcY = 0;
		
		//draw left-top corner
		screen.batch.draw(titleTexture, posX, posY, srcX, srcY, titleTileWidth, titleTileHeight);

		//draw mid-top
		srcX += titleTileWidth;
		posX += titleTileWidth;
		
		final int tileCountX = (boxText.getWidth() / titleTileWidth) - 2;
				
		for (int index = 0; index < tileCountX; index++)
		{
			screen.batch.draw(titleTexture, posX, posY, srcX, srcY, titleTileWidth, titleTileHeight);
			posX += titleTileWidth;
		}
		
		//draw right-top corner
		srcX += titleTileWidth;
		screen.batch.draw(titleTexture, posX, posY, srcX, srcY, titleTileWidth, titleTileHeight);
		
		//draw left-bottom corner
		srcX = 0;
		srcY = titleTileHeight;
		posX = boxText.left();
		posY -= titleTileHeight;
		screen.batch.draw(titleTexture, posX, posY, srcX, srcY, titleTileWidth, titleTileHeight);

		//draw mid-bottom
		srcX += titleTileWidth;
		posX += titleTileWidth;
		
		for (int index = 0; index < tileCountX; index++)
		{
			screen.batch.draw(titleTexture, posX, posY, srcX, srcY, titleTileWidth, titleTileHeight);
			posX += titleTileWidth;
		}

		//draw right-bottom corner
		srcX += titleTileWidth;
		screen.batch.draw(titleTexture, posX, posY, srcX, srcY, titleTileWidth, titleTileHeight);

		//draw title text
		final float textCenterX = (textBounds.width / 2f);
		final float textCenterY = (textBounds.height / 2f);

		final float textPosX = boxText.left() + (boxText.getWidth() >> 1) - textCenterX;
		final float textPosY = screenHeight - boxText.top() - (boxText.getHeight() >> 1) + textCenterY;
		
		font.setColor(1, 1, 1, alphaValue);
		font.draw(screen.batch, text, textPosX, textPosY);
		
		screen.batch.setColor(oldColor);
		
		screen.batch.end();
	}
	
}
