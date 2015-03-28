package pl.pzagawa.game.engine.map;

import java.util.ArrayList;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.map.tiles.TileLayer;
import pl.pzagawa.game.engine.objects.enemies.EnemyGameObjectList;
import pl.pzagawa.game.engine.objects.set.StaticGameObjectList;
import pl.pzagawa.game.engine.shape.GroundShape;

public abstract class LevelData
{
	public interface Events
	{	
		void onLevelLoaded(LevelData level);
	};

	public final long levelId;
	public final GameInstance game;
	
	private ArrayList<TileLayer> layers = new ArrayList<TileLayer>(); 

	private TileLayer backgroundLayer; 
	private TileLayer groundLayer; 
	private TileLayer shapeLayer;
	private TileLayer objectsLayer;
	private TileLayer enemiesLayer;
	
	private GroundShape groundShape;	
	private StaticGameObjectList staticObjects;
	private EnemyGameObjectList enemyObjects;

	private LevelSetup levelSetup;
	
	public LevelData(long levelId)
	{
		this.levelId = levelId;		
		this.game = GameInstance.getGame();
		
	  	this.backgroundLayer = createBackgroundLayer();
	  	this.groundLayer = createGroundLayer();
	  	this.shapeLayer = createShapeLayer();
	  	this.objectsLayer = createObjectsLayer();
	  	this.enemiesLayer = createEnemiesLayer();
	  	
	  	this.layers.add(backgroundLayer);
	  	this.layers.add(groundLayer);
	  	this.layers.add(shapeLayer);
	  	this.layers.add(objectsLayer);
	  	this.layers.add(enemiesLayer);
	  	
	  	this.groundShape = new GroundShape(shapeLayer);
	  	this.staticObjects = new StaticGameObjectList(game, objectsLayer);
	  	this.enemyObjects = new EnemyGameObjectList(game, enemiesLayer);
	  	
	  	this.levelSetup = new LevelSetup();
	}

	protected abstract TileLayer createBackgroundLayer();
	protected abstract TileLayer createGroundLayer();
	protected abstract TileLayer createShapeLayer();
	protected abstract TileLayer createObjectsLayer();
	protected abstract TileLayer createEnemiesLayer();
		
	public TileLayer getBackgroundLayer()
	{
		return backgroundLayer;
	}

	public TileLayer getGroundLayer()
	{
		return groundLayer;
	}

	public TileLayer getShapeLayer()
	{
		return shapeLayer;
	}

	public TileLayer getObjectsLayer()
	{
		return objectsLayer;
	}

	public TileLayer getEnemiesLayer()
	{
		return enemiesLayer;
	}
	
	public GroundShape getGroundShape()
	{
		return groundShape;
	}

	public StaticGameObjectList getStaticObjects()
	{
		return staticObjects;
	}

	public EnemyGameObjectList getEnemyObjects()
	{
		return enemyObjects;
	}
	
	public void load()
	{
		final LevelDataLoader loader = game.getLoader();
		
		final String levelSetupData = loader.getLevelSetup(levelId);

		this.levelSetup.parse(levelSetupData);
		
		for (TileLayer layer : layers)
		{
			final String tileMapSize = this.levelSetup.getTileMapSize(layer.getTileMapName());			
			final String tileMapdata = loader.getLevelData(levelId, layer.getTileMapName());
			
			layer.load(tileMapSize, tileMapdata, levelId);
		}
		
		this.groundShape.build();
		
		this.staticObjects.build();
		this.staticObjects.load();
		
		this.enemyObjects.build();		
		this.enemyObjects.load();
	}

	public void resize(int width, int height)
	{
	}
	
	public void dispose()
	{
		for (TileLayer layer : layers)
			layer.dispose();

		this.enemyObjects.dispose();
	}
	
	public int getMapPixelWidth()
	{
		return groundLayer.getMapPixelWidth();
	}

	public int getMapPixelHeight()
	{
		return groundLayer.getMapPixelHeight();
	}
	
	public LevelSetup getLevelSetup()
	{
		return levelSetup;
	}
		
}
