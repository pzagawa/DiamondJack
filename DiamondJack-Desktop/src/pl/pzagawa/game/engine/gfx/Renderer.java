package pl.pzagawa.game.engine.gfx;

import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.map.LevelData;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.map.tiles.TileLayer;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.GameObjectsManager;
import pl.pzagawa.game.engine.objects.ViewObject;
import pl.pzagawa.game.engine.objects.set.DynamicGameObject;
import pl.pzagawa.game.engine.state.State;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public abstract class Renderer
	implements LevelData.Events,
		GameInstance.GameplayEvents
{
	public boolean isAppletStartScreen = false;
	
	protected final GameInstance game;
	protected final GameObjectsManager objects;
	protected final ViewObject viewObject;
	protected final boolean enableBlending;

	protected TileLayer layer = null;
	protected int tileWidth = 0;
	protected int tileHeight = 0;
	protected Texture texture = null;
	protected TileItem[] tileData = null;
	private Color tintColor;
		
	public Renderer(GameInstance game, boolean enableBlending)
  {
  	this.game = game;
		this.objects = game.getObjects();
		this.viewObject = objects.getViewObject();
  	this.enableBlending = enableBlending;
  }

	public void setTileLayer(TileLayer layer)
	{
  	this.layer = layer;
		
  	this.tileWidth = layer.getTileWidth();
  	this.tileHeight = layer.getTileHeight();
		
  	this.texture = layer.getTexture();
  	this.tileData = layer.getTileData();  	
	}
	
	public void setTintColor(Color tintColor)
	{
		this.tintColor = tintColor;
	}
		
  public void dispose()
  {
  }

	public void resize(int width, int height)
	{		
	}
  
	protected void update()
	{		
	}
		
	//draws layer tiles
	public void render(Screen screen, float speed, float shake)
	{
		if (layer != null)
		{
			float viewPosX = viewObject.left() * speed;
			float viewPosY = viewObject.top() * speed;
			int viewWidth = viewObject.getWidth();
			int viewHeight = viewObject.getHeight();
			
			float screenY = screen.getHeight() + viewPosY;
			
			float posX = 0;
			float posY = 0;
			
			screen.batch.begin();
			
			final Color defaultColor = screen.batch.getColor();
			
			tintColor.a = screen.levelFadeOut.getAlphaValue();
			
			screen.batch.setColor(tintColor);
			
			if (enableBlending)
				screen.batch.enableBlending();
			else
				screen.batch.disableBlending();

			for (TileItem tile : tileData)
			{
				if (tile != null)
				{
					if (tile.isInView(viewPosX, viewPosY, viewWidth, viewHeight, tileWidth, tileHeight))
					{
						posX = (tile.posX - viewPosX);		
						posY = (screenY - tile.posY - tileHeight);
						
						screen.batch.draw(texture, posX + shake, posY + shake, tile.srcX, tile.srcY, tileWidth, tileHeight);
					}
				}
			}

			screen.batch.setColor(defaultColor);

			screen.batch.end();
		}
	}

	//draws layer tiles with map wrapping
	public void renderWrap(Screen screen, float speed)
	{
		if (layer != null)
		{
			float viewPosX = viewObject.left() * speed;
			float viewPosY = viewObject.top() * speed;
			int viewWidth = viewObject.getWidth();
			int viewHeight = viewObject.getHeight();
			
			float screenY = screen.getHeight() + viewPosY;
			
			float mapPixelWidth = layer.getMapPixelWidth();
			float mapPixelHeight = layer.getMapPixelHeight();
				
			float posX = 0;
			float posY = 0;

			screen.batch.begin();

			final Color defaultColor = screen.batch.getColor();
						
			if (enableBlending)
				screen.batch.enableBlending();
			else
				screen.batch.disableBlending();
			
			//must be enabled for proper alpha channel work
			if (screen.levelFadeOut.isEnabled() || screen.levelFadeOut.isTransparent())
				screen.batch.enableBlending();

			tintColor.a = screen.levelFadeOut.getAlphaValue();
			
			screen.batch.setColor(tintColor);
			
			for (TileItem tile : tileData)
			{
				if (tile != null)
				{
					float tilePosX = (tile.posX - viewPosX) % mapPixelWidth;
					float tilePosY = (screenY - tile.posY) % mapPixelHeight;
					
					//scroll horizontally
					if (tilePosX + tileWidth < 0)
					{
						//scrolling left
						posX = tilePosX + viewWidth + tileWidth;
					} else {
						//scrolling right
						posX = tilePosX - tileWidth;
					}

					//scroll vertically
					if (tilePosY + tileHeight < 0)
					{
						//scrolling up
						posY = tilePosY + viewHeight + tileHeight;
					} else {
						//scrolling down
						posY = tilePosY - tileHeight;
					}
					
					screen.batch.draw(texture, posX, posY, tile.srcX, tile.srcY, tileWidth, tileHeight);
				}
			}

			screen.batch.setColor(defaultColor);
			
			screen.batch.end();
		}
	}
	
	//draws test box with (0,0) coords upper left
	public void drawBox(Screen screen, BoundingBox box)
	{
		float x = box.left();
		float y = box.top();
		int w = box.getWidth();
		int h = box.getHeight();
		
		drawBox(screen, x, y, w, h);
	}
	
	public void drawBox(Screen screen, float x, float y, int width, int height)
	{
		Texture drawingTexture = CommonData.get().drawingTexture;

		int lineWidth = 1;

		float viewPosX = viewObject.left();
		float viewPosY = viewObject.top();
		
		float posX = x - viewPosX;
		float posY = screen.getHeight() - y - height + viewPosY;
		
		screen.batch.begin();
		screen.batch.disableBlending();
				
		screen.batch.draw(drawingTexture, posX, posY, 0, 0, width, lineWidth);
		
		screen.batch.draw(drawingTexture, posX, posY + height - lineWidth, 0, 0, width, lineWidth);
		
		screen.batch.draw(drawingTexture, posX, posY, 0, 0, lineWidth, height);
		
		screen.batch.draw(drawingTexture, posX + width - lineWidth, posY, 0, 0, lineWidth, height);
		
		screen.batch.end();
	}

	public void drawCenterLines(Screen screen)
	{
		Texture drawingTexture = CommonData.get().drawingTexture;

		int lineWidth = 1;
		
		int posX = screen.getWidth() >> 1;
		int posY = screen.getHeight() >> 1;
		
		screen.batch.begin();
		screen.batch.disableBlending();
		
		screen.batch.draw(drawingTexture, posX, 0, 0, 0, lineWidth, screen.getHeight());
		screen.batch.draw(drawingTexture, 0, posY, 0, 0, screen.getWidth(), lineWidth);
		
		screen.batch.end();
	}
	
	public void drawObject(Screen screen, DynamicGameObject object)
	{	
		Color color = screen.batch.getColor();
		
		if (object.state.getState() == State.IDLE)
			screen.batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
					
		if (object.state.getState() == State.ATTACK)			
			screen.batch.setColor(1.0f, 0, 0, 1.0f);
		
		if (object.state.getState() == State.RETREAT)			
			screen.batch.setColor(1.0f, 0.7f, 0, 1.0f);
		
		drawBox(screen, object);
		
		if (object.events.isCollisionTop())
			drawBox(screen, object.x, object.y - 8, object.width, 4);
		
		if (object.events.isCollisionBottom())
			drawBox(screen, object.x, object.y + object.height + 8, object.width, 4);
		
		screen.batch.setColor(color);
	}	

	public void drawGroundShape(Screen screen, BoundingBox box)
	{	
		Color color = screen.batch.getColor();
		
		screen.batch.setColor(0.2f, 0.6f, 1.0f, 1.0f);
		drawBox(screen, box);
		
		screen.batch.setColor(0.6f, 0.8f, 1.0f, 1.0f);
		drawBox(screen, box.x + 1, box.y + 1, box.width - 2, box.height - 2);
		
		screen.batch.setColor(color);
	}	
	
}
