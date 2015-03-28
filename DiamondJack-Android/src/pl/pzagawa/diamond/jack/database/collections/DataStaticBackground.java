package pl.pzagawa.diamond.jack.database.collections;

public class DataStaticBackground
{
	private static final int mapWidth = 18;
	private static final int mapHeight = 12;
	
	private static final String dataBackground =
		"1,2,3,14,4,5,6,4,27,7,8,9,24,29,1,2,3,14,3,17,18,30,7,8,9,3,43,27,19,20,21,45,3,17,18,30,10,11,4,46,17,18,26,5,6,43,17,18,5,6,10,11,4,46,8,9,19,20,21,15,42,24,19,20,21,5,6,27,24,4,24,7,5,6,27,1,2,31,22,23,24,14,10,11,24,43,29,4,27,4,11,14,43,3,4,47,29,5,6,30,5,6,17,18,45,14,43,10,21,30,28,19,20,21,45,27,4,46,4,22,23,10,11,30,19,20,3,46,44,15,3,17,18,43,19,20,21,17,18,26,3,46,4,15,17,18,4,31,22,23,4,25,3,1,2,26,3,42,17,18,4,31,24,10,11,47,19,20,21,41,10,11,4,42,17,18,15,10,11,47,20,21,7,8,9,27,10,11,17,18,10,11,3,25,31,17,18,19,19,20,21,10,11,43,5,6,4,22,23,4,4,41,47,19,20,21,";
	
	public static String getData()
	{
		return dataBackground;		
	}

	public static String getMapSize()
	{
		return Integer.toString(mapWidth) + "*" + Integer.toString(mapHeight);
	}

}
