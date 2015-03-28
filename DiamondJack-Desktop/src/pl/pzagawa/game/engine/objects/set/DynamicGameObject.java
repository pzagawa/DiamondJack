package pl.pzagawa.game.engine.objects.set;

import java.util.Map;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.JumpController;
import pl.pzagawa.game.engine.MoveController;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.objects.GameObject;
import pl.pzagawa.game.engine.objects.GameObjectEvents;
import pl.pzagawa.game.engine.objects.ViewObject;
import pl.pzagawa.game.engine.state.StateManager;

public abstract class DynamicGameObject
	extends GameObject
{
	public GameObjectEvents events;
	public StateManager state;
	public MoveController position;
	public JumpController jump;
	
	protected int mapPixelWidth = 0;
	protected int mapPixelHeight = 0;		
	
	public DynamicGameObject(GameInstance game, StateManager stateManager)
	{
		super(game);
		
		this.events = new GameObjectEvents();
		this.state = stateManager;
		this.position = new MoveController();
		this.jump = new JumpController();
	}
	
	@Override
	public void load()
	{
		super.load();
		
		mapPixelWidth = game.getLevel().getMapPixelWidth();
		mapPixelHeight = game.getLevel().getMapPixelHeight();
	}
	
	@Override
	public void setParams(Map<String, String> map)
	{
		super.setParams(map);
		
		position.setParams(map);
	}
	
	@Override
	public void reset()
	{
		jump.reset();
		events.clearAll();
	}

	public void clearEvents()
	{
		events.clearAll();
	}
		
	public abstract void render(Screen screen, ViewObject viewObject);
		
	public boolean isOutsideMap()
	{
		return (top() > (mapPixelHeight - height));
	}
	
}
