package ex.ui;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import ex.environmentCore.Model;
import ex.environmentCore.Texture;
import ex.environmentCore.TexturedModel;
import ex.graphicsCore.Camera;
import ex.graphicsCore.Loader;
import ex.shaders.UiShader;

public class GuiRenderer 
{
	Loader loader;
	Camera camera;
	UiShader uiShader;
	CrosshairCore crosshairCore;
	TexturedModel crosshairTexModel;
	
	public GuiRenderer(Loader loader, Camera camera)
	{
		this.loader = loader;
		this.camera = camera;
		uiShader = new UiShader("res/shaders/ui/vertexShader.txt", "res/shaders/ui/fragmentShader.txt");
		
		crosshairCore = new CrosshairCore();
		Model crosshairModel = loader.loadToVao(crosshairCore.vertices, crosshairCore.textureCoords);
		Texture texture = new Texture(loader.loadTexture(crosshairCore.TEXTURE_FILE));
		crosshairTexModel = new TexturedModel(crosshairModel, texture);
	}
	
	private void renderCrosshair()
	{
		uiShader.start();
		glBindVertexArray(crosshairTexModel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);				
		GL11.glBindTexture(GL13.GL_TEXTURE0, crosshairTexModel.getTextureID());			
		GL11.glDrawArrays(GL11.GL_QUADS, crosshairTexModel.getVertexAmount(), GL11.GL_UNSIGNED_INT);						
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		uiShader.stop();
	}
	
	public void render()
	{
		renderCrosshair();
	}
}
