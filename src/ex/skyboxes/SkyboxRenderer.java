package ex.skyboxes;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;

import ex.environmentCore.Model;
import ex.graphicsCore.Camera;
import ex.graphicsCore.Loader;

public class SkyboxRenderer 
{
	private static final float SIZE = 500f;
	
	private static final float[] vertices = 
		{        
			-SIZE, SIZE, -SIZE, 
			-SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,
			SIZE, SIZE, -SIZE, 
			
			-SIZE, -SIZE, -SIZE,
			-SIZE, -SIZE, SIZE,
			SIZE, -SIZE, SIZE,
			SIZE, - SIZE, -SIZE,		
		};
	
	private static final int[] indices =
		{
				2, 6, 5, 1,
				0, 4, 7, 3,
				5, 6, 7, 4,
				3, 2, 1, 0,
				1, 5, 4, 0,
				3, 7, 6, 2
		};
	
	private Model cube;
	private int texture;
	private SkyboxShader shader = new SkyboxShader("res/shaders/skybox/vertexShader.txt", "res/shaders/skybox/fragmentShader.txt");
	
	public static String[] TEXTURE_FILES =
		{
			"right", "left", "top", "bottom", "back", "front"
			//"right", "left", "top", "bottom", "back", "front"
		};
	
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix)
	{
		cube = loader.loadToVAOWithoutTexCoords(vertices, indices);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Camera camera)
	{
		shader.start();
		shader.loadViewMatrix(camera);
		glBindVertexArray(cube.getVaoID());
		glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
		GL11.glDrawElements(GL11.GL_QUADS, cube.getVertexAmount(), GL11.GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}
}
