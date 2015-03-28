package pl.pzagawa.game.engine.controls;

import pl.pzagawa.game.engine.GameInstance;
import pl.pzagawa.game.engine.gfx.Screen;
import pl.pzagawa.game.engine.objects.BoundingBox;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class UserControls
{	
	private final static int MAX_TOUCH_POINTERS = 2;
	
	public final static int BUTTON_WIDTH = 72;
	public final static int BUTTON_HEIGHT = 72;
	public final static int MARGIN = 12;
	public final static int SPACE = 12;
			
	private final UserControlButton[] buttons =
	{
		new UserControlButton(0, Input.Keys.DPAD_LEFT, BUTTON_WIDTH, BUTTON_HEIGHT),
		new UserControlButton(1, Input.Keys.DPAD_RIGHT, BUTTON_WIDTH, BUTTON_HEIGHT),
		new UserControlButton(2, Input.Keys.DPAD_UP, BUTTON_WIDTH, BUTTON_HEIGHT),
		new UserControlButton(3, Input.Keys.DPAD_DOWN, BUTTON_WIDTH, BUTTON_HEIGHT),		
		new UserControlButton(4, Input.Keys.SPACE, BUTTON_WIDTH, BUTTON_HEIGHT),		
		new UserControlButton(5, Input.Keys.CONTROL_LEFT, BUTTON_WIDTH, BUTTON_HEIGHT),		
	};
	
	protected UserControlButton btnLeft;
	protected UserControlButton btnRight;
	protected UserControlButton btnUp;
	protected UserControlButton btnDown;
	protected UserControlButton btnAction1;
	protected UserControlButton btnAction2;
	
	private final boolean[] enabledButtons;
	
	private Screen screen;
	private Input input;
	
	public UserControls(Screen screen, boolean[] enabledButtons)
	{
		this.screen = screen;
		this.input = Gdx.input;
		this.enabledButtons = enabledButtons;
		
		btnLeft = buttons[0];
		btnRight = buttons[1];
		btnUp = buttons[2];
		btnDown = buttons[3];
		btnAction1 = buttons[4];
		btnAction2 = buttons[5];
	}
	
	public void resize(int width, int height)
	{
		int posY = height - (BUTTON_HEIGHT + MARGIN);
		int posLeft = MARGIN;
		int posRight = width - MARGIN;
		
		if (enabledButtons[0])		
			btnLeft.setPosition(posLeft, posY);
		
		if (enabledButtons[1])		
			btnRight.setPosition(posLeft + BUTTON_WIDTH + SPACE, posY);
		
		if (enabledButtons[4])		
			btnAction1.setPosition(posRight - BUTTON_WIDTH, posY);
	}
	
	public UserControlButton[] getButtons()
	{
		return buttons;
	}
	
	public BoundingBox getEmptySpace()
	{
		float x = btnRight.getPosX() + btnRight.getWidth() + MARGIN;
		float y = btnRight.getPosY();
		
		int width = (int) (btnAction1.getPosX() - x) - MARGIN;
		int height = btnRight.getHeight();
		
		return new BoundingBox(x, y, width, height); 
	}
	
	public void update()
	{
		for (UserControlButton button : buttons)
		{
			button.clear();

			//check touch button			
			if (!GameInstance.IS_APPLET_VERSION)
			{
				if (button.isPositionSet())
				{
					for (int index = 0; index < MAX_TOUCH_POINTERS; index++)
					{
						if (input.isTouched(index))
						{
							int posX = screen.screenToWorldCoordX(input.getX(index));
							int posY = screen.screenToWorldCoordY(input.getY(index));
		
							if (button.isInside(posX, posY))
							{
								button.set();
							}
						}
					}
				}
			}
			
			//check hardware key
			if (button.isHardwareKeyDown())
			{
				button.set();
			}
		}
	}
	
	public boolean isLeft()
	{
		return btnLeft.isDown();
	}

	public boolean isRight()
	{
		return btnRight.isDown();
	}

	public boolean isUp()
	{
		return btnUp.isDown();
	}

	public boolean isDown()
	{
		return btnDown.isDown();
	}

	public boolean isAction1()
	{
		return btnAction1.isDown();
	}

	public boolean isAction2()
	{
		return btnAction2.isDown();
	}

	public boolean isHeldAction1()
	{
		return btnAction1.isHeld();
	}

	public boolean isHeldAction2()
	{
		return btnAction2.isHeld();
	}
	
}
