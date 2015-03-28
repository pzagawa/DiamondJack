package pl.pzagawa.game.engine.gfx;

import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.map.LevelData;
import pl.pzagawa.game.engine.map.tiles.TileTexture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class RendererCoverScreen
	extends Renderer
{	
	private TileTexture titleTileSet;
	private Texture titleTexture;	
	private final int titleTileWidth;
	private final int titleTileHeight;
		
	public RendererCoverScreen(GameInstance game)
	{
		super(game, true);

		this.titleTileSet = CommonData.get().coverScreenTileSet;
		this.titleTexture = titleTileSet.getTexture();
		
		this.titleTileWidth = titleTileSet.getTileWidth();
		this.titleTileHeight = titleTileSet.getTileHeight();
	}
		
	@Override
	public void onLevelLoaded(LevelData level)
	{
	}	
	
	@Override
	public void render(Screen screen, float speed, float shake)
	{
		final float y = (screen.getHeight() - titleTileHeight);
				
		screen.batch.begin();
		screen.batch.enableBlending();		
		
		Color oldColor = screen.batch.getColor();
		
		screen.batch.setColor(1.0f, 1.0f, 1.0f, 0.7f);

		//get tile
		int srcX = 0;
		int srcY = 0;		
		
		//cover size
		final int tileCountX = 8;
		final int tileCountY = 5;
		
		//set pos
		float posX = 0;
		float posY = y;

		//draw cover
		for (int row = 0; row < tileCountY; row++)
		{		
			for (int col = 0; col < tileCountX; col++)
			{
				screen.batch.draw(titleTexture, posX, posY, srcX, srcY, titleTileWidth, titleTileHeight);
				
				posX += titleTileWidth;
			}
			
			posX = 0;
			posY -= titleTileHeight;			
		}
		
		screen.batch.setColor(oldColor);
		
		screen.batch.end();
	}

	@Override
	public void onGameStateChange(int gameState)
	{
	}
	
}
