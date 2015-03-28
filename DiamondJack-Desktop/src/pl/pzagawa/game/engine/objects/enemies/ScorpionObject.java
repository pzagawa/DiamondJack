package pl.pzagawa.game.engine.objects.enemies;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.Utils;
import pl.pzagawa.game.engine.map.tiles.TileItem;
import pl.pzagawa.game.engine.objects.EnemyGameObject;
import pl.pzagawa.game.engine.state.State;

public class ScorpionObject
	extends EnemyGameObject
{
	private final static int MIN_ALERT_DISTANCE = 64; 
	
	public ScorpionObject(GameInstance game, TileItem tile, int tileWidth, int tileHeight)
	{
		super(game, new ScorpionStateManager(), tile, tileWidth, tileHeight, "scorpion.png", "scorpion-frameset.txt");
	}

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
			state.setState(State.ALERT);
		} else {
			state.setState(State.WALK);			
		}
	}
	
}
