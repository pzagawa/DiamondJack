package pl.pzagawa.diamond.jack.database;

import java.sql.SQLException;
import java.util.concurrent.Callable;
import org.json.JSONException;
import org.json.JSONObject;
import pl.pzagawa.diamond.jack.database.collections.LevelDataItem;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

public class DataSource
	extends OrmLiteSqliteOpenHelper
{
	private static final String DATABASE_NAME = "data.db";
	private static final int DATABASE_VERSION = 1;
	
	public final LevelDataUtils levelData;
	public final LevelStatsUtils levelStats;

	public DataSource(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		this.levelData = new LevelDataUtils(this);
		this.levelStats = new LevelStatsUtils(this);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{
			levelData.onCreate(db, connectionSource);
			levelStats.onCreate(db, connectionSource);
						
		} catch (java.sql.SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		try
		{
			levelData.onUpgrade(db, connectionSource, oldVersion, newVersion);
			levelStats.onUpgrade(db, connectionSource, oldVersion, newVersion);
			
			onCreate(db, connectionSource);
		} catch (java.sql.SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void close()
	{
		super.close();
	}
	
	public void createLevelItem(final LevelDataItem item)
		throws SQLException
	{
		TransactionManager.callInTransaction(getConnectionSource(),
			new Callable<Void>()
			{
				public Void call()
					throws SQLException
				{
					//create level item
					levelData.getDao().create(item);
		
					//level updated, so reset existing stats
					levelStats.resetItemById(item.getLevelId());

					return null;
				}
			});
	}
	
	public void updateLevelItem(final long levelId, final JSONObject jData)
		throws SQLException, JSONException
	{
		TransactionManager.callInTransaction(getConnectionSource(),
			new Callable<Void>()
			{
				public Void call()
					throws SQLException
				{		
					try
					{
						//get level dataitem
						LevelDataItem levelItem = levelData.getItemById(levelId);
						
						//check if level item exists
						if (levelItem == null)
						{
							//create new if not found
							levelItem = new LevelDataItem();
							levelItem.parse(jData);
							levelData.getDao().create(levelItem);
						} else {
							//update existing
							levelItem.parse(jData);					
							levelData.getDao().update(levelItem);
						}

						//level updated, so reset existing stats
						levelStats.resetItemById(levelId);
					}
					catch (JSONException e)
					{
						throw new SQLException("Rollback level update; JSON parse error: " + e.getMessage());
					}
					
					return null;
				}
			});		
	}

}
