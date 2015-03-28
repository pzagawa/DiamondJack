package pl.pzagawa.diamond.jack.stats;

import java.sql.SQLException;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.database.collections.LevelStatsItem;
import pl.pzagawa.game.engine.GameStatsEvents;
import com.j256.ormlite.dao.Dao;

public class GameStatsManager
{
	private static LevelStatsItem statsItem;
	private static Dao<LevelStatsItem, Long> dao;
	
	public static GameStatsEvents getGameStatsEvents()
	{
		return gameStatsEvents;
	}

	private static void getData(long levelId)
		throws SQLException
	{
		dao = MainApplication.getData().levelStats.getDao();		
		statsItem = MainApplication.getData().levelStats.getItemById(levelId);
	}
	
	private static GameStatsEvents gameStatsEvents = new GameStatsEvents()
	{
		@Override
		public void onPlayStart(long levelId)
		{
			try	
			{
				getData(levelId);
				
				statsItem.playStart();
				
				dao.update(statsItem);
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}


		@Override
		public void onPlayFinish(long levelId, int score, int totalGates, String completedGate)
		{
			try
			{
				getData(levelId);
			
				statsItem.addScore(score);
				
				//skip counting A entry gate
				statsItem.setTotalExits(totalGates - 1);
				
				statsItem.addCompletedExit(completedGate);				
				statsItem.playFinishSuccess();
			
				dao.update(statsItem);
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}

		@Override
		public void onPlayerDead(long levelId)
		{
			try
			{
				getData(levelId);

				statsItem.playFinishFailure();
			
				dao.update(statsItem);
			}			
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}

		@Override
		public int getTotalScore(long levelId)
		{
			try
			{
				getData(levelId);
				
				return statsItem.getScore();				
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();				
			}

			return 0;
		}
	};

}
