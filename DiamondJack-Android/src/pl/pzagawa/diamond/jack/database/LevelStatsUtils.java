package pl.pzagawa.diamond.jack.database;

import java.sql.SQLException;
import java.util.List;
import pl.pzagawa.diamond.jack.database.collections.LevelStatsItem;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class LevelStatsUtils
{
	private final DataSource dbHelper;
	
	public LevelStatsUtils(DataSource dbHelper)
	{
		this.dbHelper = dbHelper;		
	}

	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
		throws SQLException
	{
		TableUtils.createTable(connectionSource, LevelStatsItem.class);
	}

	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
		throws SQLException
	{
		TableUtils.dropTable(connectionSource, LevelStatsItem.class, true);
	}

	public Dao<LevelStatsItem, Long> getDao()
		throws SQLException
	{
		return dbHelper.getDao(LevelStatsItem.class);
	}

	public void addItem(final long levelId)
		throws SQLException
	{
		LevelStatsItem item = new LevelStatsItem();
		
		item.setLevelId(levelId);

		item.updateChecksum();
		
		getDao().create(item);
	}

	public void addItem(LevelStatsItem item)
		throws SQLException
	{
		item.updateChecksum();
		
		getDao().create(item);
	}
	
	protected void deleteItemById(final long levelId)
		throws SQLException
	{
		DeleteBuilder<LevelStatsItem, Long> builder = getDao().deleteBuilder();
		
		builder.where().eq("levelId", levelId);
		
		getDao().delete(builder.prepare());
	}

	public LevelStatsItem getItemById(final long levelId)
		throws SQLException
	{
		return getDao().queryForId(levelId);
	}

	public void resetItemById(final long levelId)
		throws SQLException
	{
		//delete existing stats
		deleteItemById(levelId);
		
		//and set new level stats
		addItem(levelId);
	}

	public List<LevelStatsItem> getList()
		throws SQLException
	{
		PreparedQuery<LevelStatsItem> query = getDao().queryBuilder()
			.prepare();
	
		return getDao().query(query);
	}
	
	public List<LevelStatsItem> getItemsToSync()
		throws SQLException
	{
		PreparedQuery<LevelStatsItem> query = getDao().queryBuilder().where()
			.eq("isChanged", true)
			.or()
			.eq("version", 0)
			.prepare();
	
		return getDao().query(query);
	}

	public void updateItem(final LevelStatsItem item)
		throws SQLException
	{
		item.updateChecksum();

		getDao().update(item);
	}
	
}
