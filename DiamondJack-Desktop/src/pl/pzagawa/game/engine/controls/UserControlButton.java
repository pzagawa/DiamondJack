package pl.pzagawa.game.engine.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class UserControlButton
{
	private int posX = 0;
	private int posY = 0;
	private int width = 0;
	private int height = 0;
	
	private int tileIndex = -1; 
	private int keyCode = Input.Keys.UNKNOWN;
		
	private boolean isDown = false;
	private boolean prevStateDown = false;
	
	public UserControlButton(int tileIndex, int keyCode, int width, int height)
	{
		this.tileIndex = tileIndex;
		this.keyCode = keyCode;
		this.width = width;
		this.height = height;
	}

	public int getTileIndex()
	{
		return tileIndex;
	}
	
	public int getKeyCode()
	{
		return keyCode;
	}

	public void clear()
	{
		prevStateDown = isDown;
		isDown = false;
	}
	
	public void set()
	{
		isDown = true;
	}

	public boolean isHeld()
	{
		return (prevStateDown == true) && (isDown == true);
	}

	public void setPosition(int x, int y)
	{
		posX = x;
		posY = y;		
	}
		
	public boolean isPositionSet()
	{
		return posX != 0;
	}

	public boolean isDown()
	{
		return isDown;
	}
	
	public boolean isHardwareKeyDown()
	{
		return Gdx.input.isKeyPressed(keyCode);
	}
	
	public boolean isInside(int x, int y)
	{
		if ((x > posX) && (x < (posX + width)))
			if ((y > posY) && (y < (posY + height)))
				return true;

		return false;
	}
	
	public int getPosX()
	{
		return posX;
	}

	public int getPosY()
	{
		return posY;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
	
}
