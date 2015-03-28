package pl.pzagawa.game.engine.object.anim;

import pl.pzagawa.game.engine.EngineException;
import pl.pzagawa.game.engine.Utils;
import pl.pzagawa.game.engine.objects.BoundingBox;
import pl.pzagawa.game.engine.objects.BoundingBoxList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;

//loads frame data
public class FrameSetData
{
	//line index
	private final static int INDEX_TILE_WIDTH = 0;
	private final static int INDEX_TILE_HEIGHT = 1;
	private final static int INDEX_ANIMATION_SPEED_FPS = 2;	
	private final static int INDEX_COLLISION_MARGINS = 3;
	private final static int INDEX_ANIMATION_STATES = 4;
	private final static int INDEX_FRAME_DATA = 5;
	
	private String fileName;
	private int tileWidth = 0;
	private int tileHeight = 0;
	private int speedFPS = 60;
	
	private BoundingBox[] boundingBoxes;	
	private FrameSetStates frameSetStates;
	private BoundingBox collisionMargins;	
	
	public FrameSetData(String fileName)
	{
		this.fileName = fileName;		
	}

	public void load(int leftMargin, int rightMargin, int topMargin, int bottomMargin)
	{
		try
		{
			FileHandle fileHandle = Gdx.files.getFileHandle(fileName, FileType.Internal);
			
			String str = Utils.ReadTextFile(fileHandle.read());
			
			String[] lines = str.split("\n");
						
			tileWidth = Integer.parseInt(lines[INDEX_TILE_WIDTH]);
			tileHeight = Integer.parseInt(lines[INDEX_TILE_HEIGHT]);

			speedFPS = Integer.parseInt(lines[INDEX_ANIMATION_SPEED_FPS]);

			collisionMargins = BoundingBox.createFromString(lines[INDEX_COLLISION_MARGINS], 0, 0, 0, 0);

			frameSetStates = new FrameSetStates(lines[INDEX_ANIMATION_STATES]);

			final BoundingBoxList boxes = new BoundingBoxList(); 

			for (int index = INDEX_FRAME_DATA; index < lines.length; index++)
			{
				String boxData = lines[index];
								
				BoundingBox box = BoundingBox.createFromString(boxData, leftMargin, rightMargin, topMargin, bottomMargin);
				
				//correct bounding box position in tile
				float y = tileHeight - box.getHeight();
				
				box.setPosition(box.left(), y);
				
				boxes.addNew(box);
			}

			boundingBoxes = boxes.getArray();

		} catch (Exception e)
		{
			throw new EngineException("Error loading frameset data: " + fileName + ", " + e.getMessage());
		}
	}
		
	public int getTileWidth()
	{
		return tileWidth;
	}

	public int getTileHeight()
	{
		return tileHeight;
	}
	
	public int getSpeedFPS()
	{
		return speedFPS;
	}
	
	public BoundingBox getCollisionMargins()
	{
		return collisionMargins;
	}

	public BoundingBox[] getBoundingBoxes()
	{
		return boundingBoxes;
	}

	public FrameSetStates getFrameSetStates()
	{
		return frameSetStates;
	}
	
}
