package ex.environmentCore;


import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import ex.utils.ArrayUtils;

public class Block 
{
	public static float[] getTextureCoordinates()
	{
		float[] textureCoordinates =
		{
			-1, 1, -1, //0
			-1, 1, 1, //1
			1, 1, 1, //2
			1, 1, -1, //3
			
			-1, -1, -1, //4
			-1, -1, 1, //5
			1, -1, 1, //6
			1, -1, -1, //7		
		};
		return textureCoordinates;
	}
	
	public static float[] getVertices(Vector3f position)
	{
		float vertices[] = 
			{
				position.x - 0.5f, position.y + 0.5f, position.z - 0.5f, // 0, left top back vertex
				position.x - 0.5f, position.y + 0.5f, position.z + 0.5f, // 1, left top front vertex
				position.x + 0.5f, position.y + 0.5f, position.z + 0.5f, // 2, right top front vertex
				position.x + 0.5f, position.y + 0.5f, position.z - 0.5f, // 3, right top back vertex
				
				position.x - 0.5f, position.y - 0.5f, position.z - 0.5f, // 4, left bottom back vertex
				position.x - 0.5f, position.y - 0.5f, position.z + 0.5f, // 5, left bottom front vertex
				position.x + 0.5f, position.y - 0.5f, position.z + 0.5f, // 6, right bottom front vertex
				position.x + 0.5f, position.y - 0.5f, position.z - 0.5f, // 7, right bottom back vertex 
			};
		return vertices;
	}
	
	public static float[] getTopVertices(Vector3f position)
	{
		float vertices[] =
			{
				position.x - 0.5f, position.y + 0.5f, position.z - 0.5f, // 0, left top back vertex
				position.x - 0.5f, position.y + 0.5f, position.z + 0.5f, // 1, left top front vertex
				position.x + 0.5f, position.y + 0.5f, position.z + 0.5f, // 2, right top front vertex
				position.x + 0.5f, position.y + 0.5f, position.z - 0.5f, // 3, right top back vertex
			};
		return vertices;
	}
		
	public static int[] getIndices(boolean right, boolean left, boolean back, boolean front)
	{	
		int[] rightFace =
			{
				2, 6, 7, 3
			};
		int[] leftFace =
			{
				0, 4, 5, 1
			};
		int[] topFace =
			{
				0, 1, 2, 3,
			};
		int[] bottomFace =
			{
				4, 7, 6, 5,
			};	
		int[] backFace =
			{
				3, 7, 4, 0
			};
		int[] frontFace =
			{
				1, 5, 6, 2
			};
		
		ArrayList<int[]> indicesList = new ArrayList<int[]>();
		if(right)
		{
			indicesList.add(rightFace);
		}
		if(left)
		{
			indicesList.add(leftFace);
		}
		indicesList.add(topFace);
		indicesList.add(bottomFace);
		if(back)
		{
			indicesList.add(backFace);
		}
		if(front)
		{
			indicesList.add(frontFace);
		}
		
		int[] indices = null;
		for (int i = 0; i < indicesList.size(); i++)
		{
		    int[] row = indicesList.get(i);
		    if(i == 0)
		    {
			    indices = row;
		    }
		    indices = ArrayUtils.concatInt(row, indices);
		} 
		return indices;
	}
}
