package pl.pzagawa.game.engine;

public class NumberToDigitsConverter
{
	private final int maxDigits;
	private final byte[] digits;
	private final boolean[] leadingZeroes;
	private final long[] mulFactors;
	private int leadingZeroesCount;
	
	public NumberToDigitsConverter(int maxDigits)
	{
		this.maxDigits = maxDigits;
		this.digits = new byte[maxDigits];
		this.leadingZeroes = new boolean[maxDigits];
		this.mulFactors = new long[maxDigits];
		
		for (int index = 0; index < maxDigits; index++)
			mulFactors[index] = (long)Math.pow(10, maxDigits - index);				
	}
	
	public void set(long value)
	{
		leadingZeroesCount = -1;
		boolean isLeadingZero = true;
		
		for (int index = 0; index < maxDigits; index++)
		{
			final long mulFactor = mulFactors[index];
		
			byte digit = (byte)((value % mulFactor) / (mulFactor / 10));

			if (isLeadingZero)
			{
				leadingZeroes[index] = true;
				leadingZeroesCount++;
			}
			
			if (digit != 0)
			{
				leadingZeroes[index] = false;
				isLeadingZero = false;
			}
						
			digits[index] = digit;
		}		

		leadingZeroes[maxDigits - 1] = false;
	}
	
	public long get()
	{
		long value = 0;
		
		for (int index = 0; index < maxDigits; index++)
		{
			final long mulFactor = mulFactors[index] / 10;
			final byte digit = digits[index];

			value += digit * mulFactor;
		}
		
		return value;
	}
	
	public Byte getDigitValue(int digitIndex)
	{
		return digits[digitIndex];
	}

	public boolean isDigitLeadingZero(int digitIndex)
	{
		return leadingZeroes[digitIndex];
	}

	public int getLeadingZeroesCount()
	{
		return leadingZeroesCount;
	}
	
	public int getDigitsCount()
	{
		return maxDigits - leadingZeroesCount;
	}
	
	public String getStringValue()
	{
		String s = "";
		
		for (int index = 0; index < maxDigits; index++)
		{
			if (isDigitLeadingZero(index))
			{
				s += ".";				
			} else {			
				s += Byte.toString(digits[index]);
			}
		}
		
		return s;		
	}
	
}
