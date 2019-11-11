package ex.players;

import org.lwjgl.util.vector.Vector3f;

public class PlayerCore
{
	public float[] vertices;
	public int[] indices =
			{
				2, 6, 7, 3,
				//Left face
				0, 4, 5, 1,
				//Top face
				0, 1, 2, 3,
				//Bottom face
				4, 7, 6, 5,
				//Back face
				3, 7, 4, 0,
				//Front face
				1, 5, 6, 2
			};
	
	public PlayerCore(Vector3f position)
	{
		float vertices[] = 
			{
				position.x - 2.0f, position.y + 2.0f, position.z - 2.0f, // 0, left top back vertex
				position.x - 2.0f, position.y + 2.0f, position.z + 2.0f, // 1, left top front vertex
				position.x + 2.0f, position.y + 2.0f, position.z + 2.0f, // 2, right top front vertex
				position.x + 2.0f, position.y + 2.0f, position.z - 2.0f, // 3, right top back vertex
				
				position.x - 2.0f, position.y - 6.0f, position.z - 2.0f, // 4, left bottom back vertex
				position.x - 2.0f, position.y - 6.0f, position.z + 2.0f, // 5, left bottom front vertex
				position.x + 2.0f, position.y - 6.0f, position.z + 2.0f, // 6, right bottom front vertex
				position.x + 2.0f, position.y - 6.0f, position.z - 2.0f, // 7, right bottom back vertex 
			};
		this.vertices = vertices;
	}
}
