package pl.pzagawa.game.engine.objects;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.GameState;
import pl.pzagawa.game.engine.GameplayFeedback;
import pl.pzagawa.game.engine.audio.Sounds;
import pl.pzagawa.game.engine.objects.set.DynamicGameObject;
import pl.pzagawa.game.engine.state.State;
import pl.pzagawa.game.engine.state.StateManager;

public class PlayerStateManager
	extends StateManager
{
	private boolean playerFallDown = false;
	
	public PlayerStateManager()
	{
		super();

		addState(new State(this, "IDLE", State.IDLE));
		addState(new State(this, "WALK", State.WALK));
		addState(new State(this, "JUMP", State.JUMP));
		addState(new State(this, "DEAD", State.DEAD));
	}

	@Override
	public void update(DynamicGameObject playerObject)
	{		
		if (playerObject.state.getState() == State.DEAD)
		{
			if (playerObject.events.isCollisionBottom())
			{
				if (!playerFallDown)
				{
					playerFallDown = true;
					
					Sounds.playerFall();

					if (GameInstance.gameplayFeedback != null)
						GameInstance.gameplayFeedback.playEffect(GameplayFeedback.EFFECT_PLAYER_FALL);					
				}
			} else {				
				if (!playerFallDown)
				{
					if (playerObject.isOutsideMap())
					{
						//skip checking if player fall down into GATE
						if (playerObject.game.getState() == GameState.LOADING_LEVEL)
							return;

						playerFallDown = true;

						playerObject.events.setDead();
						playerObject.events.setCollisionGround();
						playerObject.events.setCollisionBottom();
					}
				}
			}

			return;
		}

		//watch for player falling down
		if (playerObject.isOutsideMap())
		{
			//skip checking if player fall down into GATE
			if (playerObject.game.getState() == GameState.LOADING_LEVEL)				
				return;

			playerFallDown = true;
			
			playerObject.events.setDead();
			playerObject.events.setCollisionGround();
			playerObject.events.setCollisionBottom();
		}
		
		if (playerObject.events.isDead())
		{
			playerObject.state.setState(State.DEAD);
			return;
		}
		
		if (!playerObject.events.isCollisionBottom())
		{
			setState(State.JUMP);
			return;
		}
		
		if (playerObject.jump.isJumping())
			return;
		
		if (playerObject.events.isMoveLeft() || playerObject.events.isMoveRight())
		{
			setState(State.WALK);
		} else {
			setState(State.IDLE);
		}
		
		if (playerObject.events.isAction())
		{
			playerObject.events.clearAction();
			
			if (playerObject.events.isCollisionBottom())
			{
				playerObject.state.setState(State.JUMP);			
				playerObject.jump.start();				
			}
		}

	}

	@Override
	public void onExit(State state)
	{
		if (state.getIndex() == State.JUMP)
		{			
			Sounds.playerDrop();

			if (GameInstance.gameplayFeedback != null)
				GameInstance.gameplayFeedback.playEffect(GameplayFeedback.EFFECT_PLAYER_DROP);
		}		
	}

	@Override
	public void onEnter(State state)
	{
		state.resetFrameIndex();
		
		if (state.getIndex() == State.IDLE)
		{
			playerFallDown = false;
		}
		
		if (state.getIndex() == State.DEAD)
		{
			Sounds.playerScream();			
		}		
	}

	@Override
	public void onFrameChange(int stateIndex, int frameIndex)
	{
		if (stateIndex == State.WALK)
		{
			if (frameIndex % 4 == 0)
			{
				Sounds.playerWalk();
			}			
		}
	}
	
}
