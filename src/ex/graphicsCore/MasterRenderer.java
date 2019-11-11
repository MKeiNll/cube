package ex.graphicsCore;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ex.input.Input;
import ex.input.MousePicker;
import ex.players.PlayerRenderer;
import ex.skyboxes.SkyboxRenderer;
import ex.terrains.Chunk;
import ex.terrains.ChunkUpdater;
import ex.terrains.TerrainRenderer;
import ex.ui.GuiRenderer;
import ex.utils.Fonts;
import ex.utils.MatrixUtils;
import ex.utils.MemoryManager;
import ex.utils.Time;

public class MasterRenderer 
{
	Vector3f START_POSITION = new Vector3f(16, 0, 16);
	Vector2f START_OFFSET = new Vector2f((float) Math.floor(START_POSITION.x/Chunk.CHUNK_LENGTH), (float) Math.floor(START_POSITION.z/Chunk.CHUNK_LENGTH));
	
	Matrix4f projectionMatrix = MatrixUtils.createProjectionMatrix();
	int delta;
	
	Loader loader;
	Camera camera;	
	Time time;
	SkyboxRenderer skyboxRenderer;
	PlayerRenderer playerRenderer;
	TerrainRenderer terrainRenderer;
	ChunkUpdater chunkUpdater;
	Input input;	
	Fonts font;
	MemoryManager memoryManager;
	MousePicker mousePicker;
	GuiRenderer guiRenderer;
	
	public MasterRenderer()
	{		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		loader = new Loader();
		camera = new Camera();
		time = new Time();
		font = new Fonts();
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		playerRenderer = new PlayerRenderer(loader, projectionMatrix, camera, START_POSITION);
		terrainRenderer = new TerrainRenderer(loader, projectionMatrix, START_OFFSET);
		chunkUpdater = new ChunkUpdater(terrainRenderer, terrainRenderer.chunkStorage);
		input = new Input(playerRenderer.getPlayer(), terrainRenderer);
		memoryManager = new MemoryManager();
		mousePicker = new MousePicker(camera, projectionMatrix);
		guiRenderer = new GuiRenderer(loader, camera);
		
		Thread chunkUpdaterThread = new Thread(chunkUpdater);
		chunkUpdaterThread.setDaemon(true);
		chunkUpdaterThread.start();
	}
	
	public void render()
	{
		Vector3f playerPosition = playerRenderer.getPlayer().getPosition();
		
		delta = time.getDelta();
		input.manageInputs(delta);
		mousePicker.update();
		Vector3f currentBlock = mousePicker.getCurrentBlock();
		System.out.println(currentBlock);
		
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		
		playerRenderer.render(camera);
		terrainRenderer.render(camera, playerPosition, loader);
		skyboxRenderer.render(camera);
		guiRenderer.render();
		
		playerRenderer.getPlayer().calculateGravity(delta, terrainRenderer.chunkStorage.getCurrentChunk());
		playerRenderer.getPlayer().view(camera);
		
		time.countFPS(font);
		playerRenderer.displayPos(font);
		terrainRenderer.displayChunkOffset(font);
		memoryManager.printData(font);
	}
}
