package ex.terrains;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import ex.environment.Grass;
import ex.environmentCore.Block;
import ex.environmentCore.Model;
import ex.environmentCore.Texture;
import ex.environmentCore.TexturedModel;
import ex.graphicsCore.Camera;
import ex.graphicsCore.Loader;
import ex.shaders.StaticShader;
import ex.utils.Fonts;
import ex.utils.MatrixUtils;

public class TerrainRenderer 
{
	public volatile int RENDER_DISTANCE = 1;
	Vector4f PRELOADED_CHUNKS = new Vector4f(-1, -1, 2, 2);
	Vector4f LOADED_CHUNKS = new Vector4f(-1, -1, 2, 2);
	int CHUNK_SIZE = Chunk.CHUNK_LENGTH * Chunk.CHUNK_LENGTH;
	public volatile int ACTIVE_CHUNKS_AMOUNT = (int) Math.pow(2 * RENDER_DISTANCE + 1, 2);
	
	private StaticShader baseShader;
	private StaticShader glLinesShader;
	public static boolean renderMode;

	Loader loader;
	public ChunkStorage chunkStorage;
	public Chunk[] renderedChunks;	
	private int pointer;
	private final FloatBuffer buffer;
	public volatile Chunk currentChunkRendered;
	
	TexturedModel block;
	TexturedModel highlightedBlock;

	public TerrainRenderer(Loader loader, Matrix4f projectionMatrix, Vector2f START_OFFSET)
	{		
		baseShader = new StaticShader("res/shaders/terrain/vertexShader.txt", "res/shaders/terrain/fragmentShader.txt");
		glLinesShader = new StaticShader("res/shaders/terrain/glLinesVertexShader.txt", "res/shaders/terrain/glLinesFragmentShader.txt");
		
		this.loader = loader;
		chunkStorage = new ChunkStorage(loader, START_OFFSET, PRELOADED_CHUNKS);
		buffer = BufferUtils.createFloatBuffer(16 * ACTIVE_CHUNKS_AMOUNT * CHUNK_SIZE);
		renderedChunks = new Chunk[ACTIVE_CHUNKS_AMOUNT];
		renderMode = true;

		Model model = loader.loadToVaoInstanced(Block.getVertices(new Vector3f(0, 0, 0)), Block.getIndices(true, true, true, true), Block.getTextureCoordinates());
		Texture texture = new Texture(loader.loadCubeMap(Grass.TEXTURE_FILES));
		Texture highlightedTexture = new Texture(loader.loadCubeMap(Grass.TEXTURE_FILES_MODE2));
		block = new TexturedModel(model, texture);
		highlightedBlock = new TexturedModel(model, highlightedTexture);
		
		loadChunks(renderedChunks);
		currentChunkRendered = renderedChunks[0];
		
		baseShader.start();
		baseShader.loadProjectionMatrix(MatrixUtils.createProjectionMatrix());
		baseShader.stop();
		
		glLinesShader.start();
		glLinesShader.loadProjectionMatrix(MatrixUtils.createProjectionMatrix());
		glLinesShader.stop();
	}
	
	public void loadChunks(Chunk[] array)
	{
		int i = 0;
		for(int x = (int) LOADED_CHUNKS.x; x < LOADED_CHUNKS.z; x++)
		{
			for(int z = (int) LOADED_CHUNKS.y; z < LOADED_CHUNKS.w; z++)
			{
				array[i] = chunkStorage.getChunk(x, z);
				chunkAppears(array[i]);
				i++;
			}
		}
	}
	
	public void chunkGetsReplaced(Chunk chunk)
	{
		GL15.glDeleteBuffers(chunk.getVbo());
		GL15.glDeleteBuffers(chunk.getVbo1());
		GL15.glDeleteBuffers(chunk.getVbo2());
		GL30.glDeleteVertexArrays(chunk.getVaoID());
	}
	
	public void updateChunk(Chunk chunk)
	{
		chunkGetsReplaced(currentChunkRendered);
		currentChunkRendered = chunk;
		chunkAppears(currentChunkRendered);
	}
	
	public void chunkAppears(Chunk chunk)
	{
		Model model = loader.loadToVaoInstanced(Block.getVertices(new Vector3f(0, 0, 0)), Block.getIndices(true, true, true, true), Block.getTextureCoordinates());
		chunk.setVaoID(model.getVaoID());
		pointer = 0;
		int vbo = loader.createEmptyVBO(4  * ACTIVE_CHUNKS_AMOUNT * CHUNK_SIZE);
		chunk.setVbo(vbo);
		chunk.setVbo1(model.getVboID1());
		chunk.setVbo2(model.getVboID2());
		float[] vboData = new float[16 * ACTIVE_CHUNKS_AMOUNT * CHUNK_SIZE];
		loader.addInstancedAttribute(chunk.getVaoID(), vbo, 2, 4, 4, 0);
		loader.addInstancedAttribute(chunk.getVaoID(), vbo, 3, 4, 4, 4);
		loader.addInstancedAttribute(chunk.getVaoID(), vbo, 4, 4, 4, 8);
		loader.addInstancedAttribute(chunk.getVaoID(), vbo, 5, 4, 4, 12);
		
		for(int count = 0; count < chunk.getMatrices().length; count++)
		{
			storeMatrixData(chunk.getMatrices()[count], vboData);
		}
		loader.updateVBO(vbo, vboData, buffer);
	}
	
	private void storeMatrixData(Matrix4f matrix, float[] vboData)
	{
		vboData[pointer++] = matrix.m00;
		vboData[pointer++] = matrix.m01;
		vboData[pointer++] = matrix.m02;
		vboData[pointer++] = matrix.m03;
		vboData[pointer++] = matrix.m10;
		vboData[pointer++] = matrix.m11;
		vboData[pointer++] = matrix.m12;
		vboData[pointer++] = matrix.m13;
		vboData[pointer++] = matrix.m20;
		vboData[pointer++] = matrix.m21;
		vboData[pointer++] = matrix.m22;
		vboData[pointer++] = matrix.m23;
		vboData[pointer++] = matrix.m30;
		vboData[pointer++] = matrix.m31;
		vboData[pointer++] = matrix.m32;
		vboData[pointer++] = matrix.m33;
	}
	
	public void render(Camera camera, Vector3f playerPosition, Loader loader)
	{
		for(int renderedIndex = 0; renderedIndex < renderedChunks.length; renderedIndex++)
		{
			chunkStorage.updateCurrentChunk(playerPosition, renderedIndex, renderedChunks);
			if(renderedChunks[renderedIndex] != ChunkUpdater.updatedChunks[renderedIndex]) //Sync renderedChunks with updatedChunks
			{
				chunkGetsReplaced(renderedChunks[renderedIndex]);
				renderedChunks[renderedIndex] = ChunkUpdater.updatedChunks[renderedIndex];
				chunkAppears(renderedChunks[renderedIndex]);
			}
			currentChunkRendered = renderedChunks[renderedIndex];
			
			baseShader.start();
			baseShader.loadViewMatrix(camera);
			glBindVertexArray(renderedChunks[renderedIndex].getVaoID());
			glEnableVertexAttribArray(0);
			glEnableVertexAttribArray(2);
			glEnableVertexAttribArray(3);
			glEnableVertexAttribArray(4);
			glEnableVertexAttribArray(5);
			if(renderMode)
			{
				glEnableVertexAttribArray(1);
				GL13.glActiveTexture(GL13.GL_TEXTURE0);				
				if(chunkStorage.getCurrentChunk() == renderedChunks[renderedIndex])
				{
					GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, highlightedBlock.getTextureID());
				}
				else
				{
					GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, block.getTextureID());
				}				
				GL31.glDrawElementsInstanced(GL11.GL_QUADS, block.getVertexAmount(), GL11.GL_UNSIGNED_INT, 0, CHUNK_SIZE);				
				glDisableVertexAttribArray(1);
			}
			else
			{
				GL31.glDrawElementsInstanced(GL11.GL_LINES, block.getVertexAmount(), GL11.GL_UNSIGNED_INT, 0, CHUNK_SIZE);
			}			
			glDisableVertexAttribArray(5);
			glDisableVertexAttribArray(4);
			glDisableVertexAttribArray(3);
			glDisableVertexAttribArray(2);
			glDisableVertexAttribArray(0);
			glBindVertexArray(0);
			baseShader.stop();
		}
	}
	
	public void displayChunkOffset(Fonts font)
	{
		font.draw(900, 0, "ChunkX: " + chunkStorage.getCurrentChunk().getOffsetX());
		font.draw(900, 30, "ChunkZ: " + chunkStorage.getCurrentChunk().getOffsetZ());
	}
	
	public static boolean getRenderMode()
	{
		return renderMode;
	}
	
	public static void changeRenderMode()
	{
		renderMode = !renderMode;
	}
}
