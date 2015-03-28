package pl.pzagawa.diamond.jack.gfx;

import pl.pzagawa.game.engine.objects.BoundingBox;

public class OSDTextureMap
{
	protected final int tileWidth;
	protected final int tileHeight;
	
	public final BoundingBox boxDigits;
	public final BoundingBox boxScore;
	public final BoundingBox boxKey1;
	public final BoundingBox boxKey2;
	public final BoundingBox boxHat;
	
	private BoundingBox boxDigit;
	
	public OSDTextureMap(int tileWidth, int tileHeight)
	{
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		boxDigits = new BoundingBox(0, 0, tileWidth * 10, tileHeight);
		boxScore = new BoundingBox(10 * tileWidth, 0, tileWidth * 4, tileHeight);
		boxKey1 = new BoundingBox(0, tileHeight, tileWidth * 2, 32);
		boxKey2 = new BoundingBox(2 * tileWidth, tileHeight, tileWidth * 2, 32);
		boxHat = new BoundingBox(4 * tileWidth, tileHeight, tileWidth * 2, 32);
		
		boxDigit = new BoundingBox(0, 0, tileWidth, tileHeight);
	}
	
	public BoundingBox getDigitPos(int digitIndex)
	{
		boxDigit.setPosition(digitIndex * tileWidth, 0);
		return boxDigit;
	}
	
}
