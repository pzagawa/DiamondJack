package pl.pzagawa.diamond.jack.data;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.database.collections.LevelDataItem;
import pl.pzagawa.events.Event;
import pl.pzagawa.events.EventId;
import pl.pzagawa.events.GameUpdateOperation;
import pl.pzagawa.game.engine.map.tiles.TileLayer;
import android.app.Activity;

public class DatabaseUpdater
	extends Observable
	implements Observer
{
	private final Activity parent;
	private final Event event;

	public DatabaseUpdater(Activity parent)
	{
		this.parent = parent;
		this.event = new Event(EventId.DATABASE_UPDATE);
	}
	
	private boolean isMapFile(String fileName)
	{
		String[] parts = fileName.split("-");
		
		if (parts.length > 0)
			if (parts[0].compareTo("map") == 0)
				return true;
				
		return false;
	}
	
	private void updateFromAssets()
	{
		final String[] files = FileUtils.getAssetLevelsDirectory(parent);
		
		for (String fileName : files)
		{
			if (!isMapFile(fileName))
			{
				try
				{
					long levelId = Long.parseLong(fileName);
					processLevel(levelId);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private void processLevel(long levelId)
	{
		String dataSetup = FileUtils.getLevelSetup(parent, levelId);
		String dataBackground = FileUtils.getLevelData(parent, levelId, TileLayer.BACKGROUND);
		String dataGround = FileUtils.getLevelData(parent, levelId, TileLayer.GROUND);
		String dataShape = FileUtils.getLevelData(parent, levelId, TileLayer.SHAPE);
		String dataObjects = FileUtils.getLevelData(parent, levelId, TileLayer.OBJECTS);
		String dataEnemies = FileUtils.getLevelData(parent, levelId, TileLayer.ENEMIES);

		LevelDataItem item = new LevelDataItem();

		item.setPublic(true);
		item.setFree(true);

		item.decodeDataSetup(dataSetup);
		
		item.setDataBackground(dataBackground);
		item.setDataGround(dataGround);
		item.setDataShape(dataShape);
		item.setDataObjects(dataObjects);
		item.setDataEnemies(dataEnemies);

		try
		{
			MainApplication.getData().createLevelItem(item);			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
		
	public void process()
	{
		GameUpdateOperation owp = new GameUpdateOperation(event)
		{
			@Override
			protected void runWorkerThread()
				throws ClientProtocolException, JSONException, IOException
			{
				updateFromAssets();				
			}

			@Override
			protected boolean runFinishUiThread() throws JSONException
			{
				return true;
			}
			
		};
		
		owp.addObserver(this);
		owp.run();
	}
	
	public void finish()
	{
		setChanged();
		
		event.finish();
		event.setSuccessResult();
		
		notifyObservers(event);		
	}

	@Override
	public void update(Observable src, Object data)
	{
		Event event = (Event)data;
		
		//dispatch locally observed events up
		this.setChanged();
		this.notifyObservers(event);
	}
	
}
