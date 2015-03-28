package pl.pzagawa.diamond.jack.ui;

import pl.pzagawa.diamond.jack.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class BackgroundLinearLayout
	extends LinearLayout
{
	private Drawable bkgImage;
	private Rect rtBkgImage;
	
	public BackgroundLinearLayout(Context context)
	{
		super(context);
		initialize();
	}

	public BackgroundLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize();
		processAttributes(attrs);
	}

	private void processAttributes(AttributeSet attrs)
	{  
	}
	
	private void initialize()
	{		
		this.setWillNotDraw(false);
		
		bkgImage = this.getResources().getDrawable(R.drawable.background);				
		rtBkgImage = new Rect(0, 0, bkgImage.getIntrinsicWidth(), bkgImage.getIntrinsicHeight()); 
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
				
		final int width = this.getWidth();
		final int height = this.getHeight();
		
		final int countX = (width / rtBkgImage.width()) + 1;
		final int countY = (height / rtBkgImage.height()) + 1;
		
		for (int x = 0; x < countX; x++)
		{
			for (int y = 0; y < countY; y++)
			{
				int posX = x * rtBkgImage.width();			
				int posY = y * rtBkgImage.height();

				bkgImage.setBounds(posX, posY, posX + rtBkgImage.width(), posY + rtBkgImage.height());
				bkgImage.draw(canvas);
			}
		}
	}

}
