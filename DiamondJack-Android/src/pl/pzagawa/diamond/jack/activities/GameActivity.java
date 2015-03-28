package pl.pzagawa.diamond.jack.activities;

import pl.pzagawa.diamond.jack.AndroidLevelDataLoader;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.diamond.jack.Settings;
import pl.pzagawa.diamond.jack.game.DiamondJack;
import pl.pzagawa.diamond.jack.stats.GameStatsManager;
import pl.pzagawa.diamond.jack.ui.ActivityStarter;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.GameStartupParams;
import pl.pzagawa.game.engine.GameplayFeedback;
import pl.pzagawa.game.engine.map.LevelDataLoader;
import android.os.Bundle;
import android.os.Vibrator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdx.backends.android.surfaceview.FixedResolutionStrategy;
import com.badlogic.gdx.backends.android.surfaceview.RatioResolutionStrategy;

public class GameActivity
	extends AndroidApplication
{
	private GameInstance game;	
	private Settings settings;
	private ActivityStarter starter;
	
	private Vibrator vibrator;
	private long[] vibratorEffectDrop = { 100, 20 };
	private long[] vibratorEffectOpenDoors = { 0, 100, 50, 100, 50, 100, 50, 100, 50, 100, 50, 100  };
	private long[] vibratorEffectFall = { 0, 40 };

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.vibrator = (Vibrator)this.getSystemService(VIBRATOR_SERVICE);
		
		GameInstance.gameplayFeedback = this.gameplayFeedback;		
		GameInstance.gameStatsEvents = GameStatsManager.getGameStatsEvents();
				
		this.starter = new ActivityStarter(this);
	
		//get preferences
		settings = MainApplication.getSettings();
		settings.load();
		
		//setup game
		LevelDataLoader loader = new AndroidLevelDataLoader(this);

		GameStartupParams startupParams = new GameStartupParams();
		
		startupParams.levelId = starter.getLevelId();
		
		//initialize
		game = new DiamondJack(startupParams, loader, gameAppEvents);
		
		this.initialize(game, getConfiguration(settings));	
	}
	
	private AndroidApplicationConfiguration getConfiguration(Settings settings)
	{
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		config.useGL20 = false;
		config.useAccelerometer = false;
		config.useCompass = false;
		config.touchSleepTime = 0;
		config.useWakelock = true;
		
		//set default video mode
		config.resolutionStrategy = new FillResolutionStrategy();
		
		if (settings.getVideoMode() == Settings.VIDEOMODE_FIXED)
			config.resolutionStrategy = new FixedResolutionStrategy(DiamondJack.SCREEN_WIDTH, DiamondJack.SCREEN_HEIGHT);			
		
		if (settings.getVideoMode() == Settings.VIDEOMODE_RATIO)
			config.resolutionStrategy = new RatioResolutionStrategy(DiamondJack.SCREEN_WIDTH, DiamondJack.SCREEN_HEIGHT);
		
		return config;
	}
	
	private GameInstance.ApplicationEvents gameAppEvents = new GameInstance.ApplicationEvents()
	{		
		@Override
		public void onGameExit(int resultCode)
		{
			if (resultCode == GameInstance.RESULT_COMPLETED)
				GameActivity.this.setResult(RESULT_OK);
			
			Gdx.app.exit();
		}
	};
	
	private GameplayFeedback gameplayFeedback = new GameplayFeedback()
	{
		@Override
		public void playEffect(int effectType)
		{
			if (vibrator != null)
			{
				if (settings.getVibratorState() == Settings.OPTION_DISABLED)
					return;
				
				vibrator.cancel();
				
				if (effectType == GameplayFeedback.EFFECT_PLAYER_DROP)
					vibrator.vibrate(vibratorEffectDrop, -1);
				
				if (effectType == GameplayFeedback.EFFECT_OPEN_DOORS)
					vibrator.vibrate(vibratorEffectOpenDoors, -1);
				
				if (effectType == GameplayFeedback.EFFECT_PLAYER_FALL)
					vibrator.vibrate(vibratorEffectFall, -1);					
			}			
		}
	};
	
}
