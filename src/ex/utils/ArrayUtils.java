package ex.utils;

import java.util.ArrayList;

public class ArrayUtils 
{
	public static float[] concatFloat(float[] a, float[] b) 
	{
		   int aLen = a.length;
		   int bLen = b.length;
		   float[] c = new float[aLen + bLen];
		   System.arraycopy(a, 0, c, 0, aLen);
		   System.arraycopy(b, 0, c, aLen, bLen);
		   return c;
	}
	
	public static int[] concatInt(int[] a, int[] b) 
	{
		   int aLen = a.length;
		   int bLen = b.length;
		   int[] c = new int[aLen + bLen];
		   System.arraycopy(a, 0, c, 0, aLen);
		   System.arraycopy(b, 0, c, aLen, bLen);
		   return c;
	}
	
	public static int[] convertFloatToInt(float[] input)
	{
	    int[] output = new int[input.length];
	    for (int i = 0; i < input.length; i++)
	    {
	        output[i] = (int) input[i];
	    }
	    return output;
	}
	
	public static ArrayList <int[]> convertFloatListToIntList(ArrayList<float[]> input)
	{
		ArrayList<int[]> output = new ArrayList<int[]>();
	    for (int i = 0; i < input.size(); i++)
	    {
	        output.add(convertFloatToInt(input.get(i)));
	    }
	    return output;
	}
}
