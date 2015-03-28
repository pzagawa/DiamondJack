package pl.pzagawa.game.engine.object.anim;

import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.map.tiles.TileTexture;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.GameObjectEvents;
import pl.pzagawa.game.engine.objects.ViewObject;

import com.badlogic.gdx.graphics.Texture;

//handles frameset texture image and frameset map data
public class FrameSet
{	
	private final TileTexture tileSet;
	private final String textureFileName;
	
	private Texture texture;
	
	private BoundingBox[] boundingBoxes;
	private BoundingBox currentBox;
	private int currentFrame = 0;
	private int textureSrcX;
	private int textureSrcY;
	private int currentWidth;
	private int currentHeight;	

	private boolean flipDrawingX = false;	
	
	public FrameSet(String textureFileName)
	{
		this.tileSet = new TileTexture();
		this.textureFileName = textureFileName;
	}

	public void load(int tileWidth, int tileHeight)
	{
		tileSet.load(textureFileName, tileWidth, tileHeight);
		texture = tileSet.getTexture();
	}
	
	public void setBoundingBoxes(BoundingBox[] boundingBoxes)
	{
		this.boundingBoxes = boundingBoxes;		
	}
	
	public void dispose()
	{
		tileSet.dispose();
	}
	
	public void setFrame(int index)
	{
		if (index == -1)
			return;
		
		this.currentFrame = index;
		this.currentBox = boundingBoxes[currentFrame];		
		this.textureSrcX = (int)currentBox.left();
		this.textureSrcY = (int)currentBox.top();
		this.currentWidth = currentBox.getWidth();
		this.currentHeight = currentBox.getHeight();
	}
	
	public void update(GameObjectEvents events)
	{
		if (events.isMoveLeft())
			flipDrawingX = true;
			
		if (events.isMoveRight())
			flipDrawingX = false;	
	}
	
	public void setFlipDrawingX(boolean flip)
	{
		this.flipDrawingX = flip;
	}
	
	public boolean isFlipDrawingX()
	{
		return flipDrawingX;
	}
	
	public BoundingBox getBoundingBox()
	{
		return currentBox;
	}
	
	public void render(Screen screen, ViewObject viewObject, float x, float y)
	{
		float viewPosX = viewObject.left();
		float viewPosY = viewObject.top();
		
		float width = currentWidth;
		float height = currentHeight;

		int textureSrcWidth = currentWidth;
		int textureSrcHeight = currentHeight;
		
		float posX = x - viewPosX;
		float posY = screen.getHeight() - y - height + viewPosY;
		
		screen.batch.draw(texture, posX, posY, width, height, textureSrcX, textureSrcY,
				textureSrcWidth, textureSrcHeight, flipDrawingX, false);
	}
	
	public Texture getTexture()
	{
		return texture;
	}
	
}
