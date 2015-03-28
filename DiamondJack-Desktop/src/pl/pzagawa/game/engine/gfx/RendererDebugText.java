package pl.pzagawa.game.engine.gfx;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import pl.pzagawa.game.engine.CommonData;
import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.map.LevelData;

public class RendererDebugText
	extends Renderer
{
	private BitmapFont font;
		
	private static ArrayList<String> lines = new ArrayList<String>(); 
	
	private static long startTime = 0;
	private static int[] bufferTime = new int[20];
	private static int measureIndex = 0;
	private static String textTime = "";	
	private Color textColor = new Color(0.5f, 1.0f, 0.0f, 0.5f);
		
	public RendererDebugText(GameInstance game)
	{
		super(game, false);

		font = CommonData.get().debugFont;
	}
	
	@Override
	public void onLevelLoaded(LevelData level)
	{
	}
	
	@Override
	public void onGameStateChange(int gameState)
	{		
	}
	
	@Override
	public void dispose()
	{
	}

	@Override
	public void resize(int width, int height)
	{
	}
	
	@Override
	public void render(Screen screen, float speed, float shake)
	{
		int margin = 8;
		int lineHeight = 16;

		int linePosY = screen.getHeight();
		linePosY -= 64;

		final Color defaultColor = screen.batch.getColor();		
		
		screen.batch.begin();
		screen.batch.enableBlending();		
		
		font.setColor(textColor);
		
		String textFPS = "FPS: " + Integer.toString(Gdx.graphics.getFramesPerSecond());		
		
		font.draw(screen.batch, textFPS, margin, linePosY);
		linePosY -= lineHeight;
		
		font.draw(screen.batch, textTime, margin, linePosY);
		linePosY -= lineHeight;

		int lines_size = lines.size();
		for (int index = 0; index < lines_size; index++)
		{
			String textLine = lines.get(index);
			font.draw(screen.batch, textLine, margin, linePosY);
			linePosY -= lineHeight;
		}
				
		screen.batch.setColor(defaultColor);
		
		screen.batch.end();
	}
	
	public static void clear()
	{
		lines.clear();
	}

	public static void addText(String text)
	{
		lines.add(text);
	}
	
	public static void startTime()
	{
		startTime = System.nanoTime();
	}

	public static void stopTime()
	{
		int updateTime = (int) ((System.nanoTime() - startTime) / 1000000.0f);
				
		bufferTime[measureIndex++] = updateTime;
		
		if (measureIndex > (bufferTime.length - 1))
		{
			measureIndex = 0;
			
			int avgUpdateTime = 0;
			for (int value : bufferTime)
				avgUpdateTime += value;
			
			avgUpdateTime /= bufferTime.length;
			
			textTime = "render ms time: " + Integer.toString(avgUpdateTime);
		}
	}

}
