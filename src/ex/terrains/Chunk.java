package ex.terrains;

import java.io.Serializable;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import ex.gen.ValueNoise;
import ex.utils.MatrixUtils;

public class Chunk implements Serializable
{
	private static final long serialVersionUID = 247456746264766855L;

	public static int CHUNK_LENGTH = 64;
	
	int[][] heights = new int[CHUNK_LENGTH][CHUNK_LENGTH];
	Matrix4f[] matrices = new Matrix4f[CHUNK_LENGTH * CHUNK_LENGTH];

	int offsetX;
	int offsetZ;
	int vaoID;
	int vbo;
	int vbo1;
	int vbo2;
	
	public Chunk(int offsetX, int offsetZ, ValueNoise noise)
	{
		this.offsetX = offsetX;
		this.offsetZ = offsetZ;
		//this.vaoID = vaoID;
		
		generateHeights(noise);
		generateMatrices(noise);
	}
	
	private void generateHeights(ValueNoise noise)
	{
		for(int x = 0; x < CHUNK_LENGTH; x++)
		{
			for(int z = 0; z < CHUNK_LENGTH; z++)
			{
				if(offsetX < 0 && offsetZ < 0) //Both negative
				{
					heights[x][z] = noise.generateHeight(-x, -z, (offsetX + 1) * CHUNK_LENGTH, (offsetZ + 1) * CHUNK_LENGTH);
				}
				else if(offsetX >= 0 && offsetZ < 0) //X positive, Z negative
				{
					heights[x][z] = noise.generateHeight(x, -z, offsetX * CHUNK_LENGTH, (offsetZ + 1) * CHUNK_LENGTH);
				}
				else if(offsetX < 0 && offsetZ >= 0) //X negative, Z positive
				{
					heights[x][z] = noise.generateHeight(-x, z, (offsetX + 1) * CHUNK_LENGTH, offsetZ * CHUNK_LENGTH);
				}
				else if(offsetX >= 0 && offsetZ >= 0) //Both positive
				{
					heights[x][z] = noise.generateHeight(x, z, offsetX * CHUNK_LENGTH, offsetZ * CHUNK_LENGTH);
				}
				else
				{
					System.out.println("ERR_NO_CHUNK_HEIGHT_GENERATED_at_Chunk.java");
				}
			}
		}
	}
	
	private void generateMatrices(ValueNoise noise)
	{
		int i = 0;
		for(int x = 0; x < CHUNK_LENGTH; x++)
		{
			for(int z = 0; z < CHUNK_LENGTH; z++)
			{
				if(offsetX < 0 && offsetZ < 0) //Both negative
				{
					matrices[i] = MatrixUtils.createTransformationMatrix(new Vector3f(-x + (offsetX + 1) * CHUNK_LENGTH, heights[x][z], -z + (offsetZ + 1) * CHUNK_LENGTH), 0, 0, 0);
				}
				else if(offsetX >= 0 && offsetZ < 0) //X positive, Z negative
				{
					matrices[i] = MatrixUtils.createTransformationMatrix(new Vector3f(x + offsetX * CHUNK_LENGTH, heights[x][z], -z + (offsetZ + 1) * CHUNK_LENGTH), 0, 0, 0);
				}
				else if(offsetX < 0 && offsetZ >= 0) //X negative, Z positive
				{
					matrices[i] = MatrixUtils.createTransformationMatrix(new Vector3f(-x + (offsetX + 1) * CHUNK_LENGTH, heights[x][z], z + offsetZ * CHUNK_LENGTH), 0, 0, 0);
				}
				else if(offsetX >= 0 && offsetZ >= 0) //Both positive
				{
					matrices[i] = MatrixUtils.createTransformationMatrix(new Vector3f(x + offsetX * CHUNK_LENGTH, heights[x][z], z + offsetZ * CHUNK_LENGTH), 0, 0, 0);
				}
				else
				{
					System.out.println("ERR_NO_CHUNK_MATRICES_GENERATED_at_Chunk.java");
				}
				i++;
			}
		}
	}
	
	public int[][] getHeights()
	{
		return heights;
	}
	
	public Matrix4f[] getMatrices()
	{
		return matrices;
	}
	
	public int getOffsetX()
	{
		return offsetX;
	}
	
	public int getOffsetZ()
	{
		return offsetZ;
	}
	
	public int getVaoID()
	{
		return vaoID;
	}
	
	public void setVaoID(int newID)
	{
		vaoID = newID;
	}
	
	public int getVbo()
	{
		return vbo;
	}
	
	public void setVbo(int newID)
	{
		vbo = newID;
	}
	
	public void setVbo1(int newID)
	{
		vbo1 = newID;
	}
	
	public void setVbo2(int newID)
	{
		vbo2 = newID;
	}
	
	public int getVbo1()
	{
		return vbo1;
	}
	
	public int getVbo2()
	{
		return vbo2;
	}
}
