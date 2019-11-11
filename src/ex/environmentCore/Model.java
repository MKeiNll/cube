package ex.environmentCore;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;

public class Model
{
	private int vaoID;
	private int vertexAmount;
	private int vboID1;
	private int vboID2;
	
	public Model(int vaoID, int vboID1, int vboID2, int vertexAmount) 
	{
		this.vaoID = vaoID;
		this.vboID1 = vboID1;
		this.vboID2 = vboID2;
		this.vertexAmount = vertexAmount;
	}
	public Model(int vaoID,  int vertexAmount) 
	{
		this.vaoID = vaoID;
		this.vertexAmount = vertexAmount;
	}
	
	public int getVaoID() 
	{
		return vaoID;
	}
	
	public int getVboID1() 
	{
		return vboID1;
	}
	
	public int getVboID2() 
	{
		return vboID2;
	}

	public int getVertexAmount() 
	{
		return vertexAmount;
	}
	
	public void destroy()
	{
		glDeleteBuffers(getVaoID());
	}
}