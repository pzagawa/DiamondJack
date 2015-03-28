package pl.pzagawa.game.engine.objects.enemies;

import pl.pzagawa.game.engine.DelayTimer;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.Utils;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.objects.EnemyGameObject;
import pl.pzagawa.game.engine.state.State;

public class SkeletonObject
	extends EnemyGameObject
{
	private final static int MIN_ALERT_DISTANCE = 80; 
	private final static int MIN_ATTACK_DISTANCE = 48; 
	private final static float ATTACK_DELAY = 0.35f;
	
	public SkeletonObject(GameInstance game, TileItem tile, int tileWidth, int tileHeight, boolean flipDrawingX)
	{
		super(game, new SkeletonStateManager(), tile, tileWidth, tileHeight, "skeleton.png", "skeleton-frameset.txt");

		animation.getFrameSet().setFlipDrawingX(flipDrawingX);
	}

	private DelayTimer stateAttackDelay = new DelayTimer(ATTACK_DELAY)
	{
		@Override
		public void onFinish(Object object)
		{
			state.setState(State.ATTACK);
		}		
	};
	
	@Override
	public void load()
	{
		super.load();
		
		String enemyParams = "power:0.5;friction:0.6;gravity:0.0";

		this.setParams(Utils.propertiesFromString(enemyParams));

		state.setState(State.IDLE);
	}
	
	@Override
	public void update()
	{
		super.update();

		if (playerObject.isMinimumDistance(this, MIN_ALERT_DISTANCE))
		{
			if (playerObject.isMinimumDistance(this, MIN_ATTACK_DISTANCE))
			{
				if (!stateAttackDelay.isEnabled())
					stateAttackDelay.start();
			} else {
				if (state.getState() != State.ATTACK)		
					state.setState(State.ALERT);
			}
		}		
		
		stateAttackDelay.update();
	}

	@Override
	public boolean isCollisionTestEnabled()
	{
		if (state.getState() == State.ATTACK)		
			return true;
		
		return false;
	}
	
}
