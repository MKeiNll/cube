package ex.players;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import ex.environmentCore.Model;
import ex.graphicsCore.Camera;
import ex.graphicsCore.Loader;
import ex.shaders.EntityShader;
import ex.utils.Fonts;
import ex.utils.MatrixUtils;
import ex.utils.SimpleUtils;

public class PlayerRenderer 
{
	private Player player;
	
	EntityShader playerShader = new EntityShader("res/shaders/player/playerVertexShader.txt", "res/shaders/player/playerFragmentShader.txt");
	
	public PlayerRenderer(Loader loader, Matrix4f projectionMatrix, Camera camera, Vector3f START_POSITION)
	{
		PlayerCore playerCore = new PlayerCore(new Vector3f(0, 0, 0f));
		Model playerModel = loader.loadToVAOWithoutTexCoords(playerCore.vertices, playerCore.indices);
		player = new Player(playerModel, START_POSITION, camera); 
		
		playerShader.start();
		playerShader.loadProjectionMatrix(MatrixUtils.createProjectionMatrix());
		playerShader.stop();
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public void render(Camera camera)
	{
		playerShader.start();
		playerShader.loadViewMatrix(camera);
		Model model = player.getModel();
		glBindVertexArray(model.getVaoID());
		glEnableVertexAttribArray(0);
		Matrix4f transformationMatrix = MatrixUtils.createTransformationMatrix(player.getPosition(), player.getRotX(), player.getRotY(), player.getRotZ());
		playerShader.loadTransformationMatrix(transformationMatrix);
		GL11.glDrawElements(GL11.GL_QUADS, model.getVertexAmount(), GL11.GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		playerShader.stop();
	}
	
	public void displayPos(Fonts font)
	{
		font.draw(700, 0, "PosX: " + SimpleUtils.round(player.getPosition().x, 1)); 
		font.draw(700, 30, "PosY: " +  SimpleUtils.round(player.getPosition().y, 1)); 
		font.draw(700, 60, "PosZ: " +  SimpleUtils.round(player.getPosition().z, 1));
	}
}
