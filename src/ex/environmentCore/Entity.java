package ex.environmentCore;

import org.lwjgl.util.vector.Vector3f;

public class Entity 
{
	Model model;
	public Vector3f position;
	public float rotX = 0;
	public float rotY = 0;
	public float rotZ = 0;
	
	public Entity(Model model, Vector3f position)
	{
		this.model = model;
		this.position = position;
	}

	public Model getModel() 
	{
		return model;
	}

	public Vector3f getPosition() 
	{
		return position;
	}

	public float getRotX() 
	{
		return rotX;
	}

	public float getRotY() 
	{
		return rotY;
	}

	public float getRotZ() 
	{
		return rotZ;
	}
}
