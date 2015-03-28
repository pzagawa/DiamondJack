package pl.pzagawa.diamond.jack.database;

import java.sql.SQLException;
import java.util.List;
import pl.pzagawa.diamond.jack.database.collections.LevelDataItem;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class LevelDataUtils
{
	private final DataSource dbHelper;
	
	public LevelDataUtils(DataSource dbHelper)
	{
		this.dbHelper = dbHelper;		
	}
	
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
		throws SQLException
	{
		TableUtils.createTable(connectionSource, LevelDataItem.class);
	}

	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
		throws SQLException
	{
		TableUtils.dropTable(connectionSource, LevelDataItem.class, true);
	}

	protected Dao<LevelDataItem, Long> getDao()
		throws SQLException
	{		
		return dbHelper.getDao(LevelDataItem.class);
	}
		
	private QueryBuilder<LevelDataItem, Long> getListQuery()
		throws SQLException
	{
		return getDao().queryBuilder()
			.selectColumns("levelId", "uid", "name", "description", "author", "isPublic", "isPrivate", "isFree")		
			.orderBy("name", true);
	}

	public List<LevelDataItem> getPublicLevels()
		throws SQLException
	{	
		return getDao().query(getListQuery()
			.where()
			.eq("isPublic", true)
			.prepare());
	}

	public List<LevelDataItem> getPrivateLevels()
		throws SQLException
	{	
		return getDao().query(getListQuery()
			.where()
			.eq("isPrivate", true)
			.and()
			.eq("isPublic", false)
			.prepare());			
	}

	public List<LevelDataItem> getLevels()
		throws SQLException
	{	
		return getDao().query(getListQuery()
			.prepare());			
	}
	
	public LevelDataItem getItemById(long levelId)
		throws SQLException
	{	
		return getDao().queryForId(levelId);
	}

}
