package ex.terrains;

public class ChunkUpdater implements Runnable
{
	TerrainRenderer terrainRenderer;
	ChunkStorage chunkStorage;
	public static Chunk[] updatedChunks;	
	
	public ChunkUpdater(TerrainRenderer terrainRenderer, ChunkStorage chunkStorage)
	{
		this.terrainRenderer = terrainRenderer;
		this.chunkStorage = chunkStorage;	
		updatedChunks = new Chunk[terrainRenderer.ACTIVE_CHUNKS_AMOUNT];
		terrainRenderer.loadChunks(updatedChunks);
	}
	
	public void run() 
	{
		int boxLength = (int) Math.sqrt(terrainRenderer.ACTIVE_CHUNKS_AMOUNT);
		while(true)
		{
			for(int i = 0; i < updatedChunks.length; i++)
			{
				if(Math.abs(terrainRenderer.renderedChunks[i].getOffsetX() - chunkStorage.getCurrentChunk().getOffsetX()) > terrainRenderer.RENDER_DISTANCE)
				{
					if(terrainRenderer.renderedChunks[i].getOffsetX() > chunkStorage.getCurrentChunk().getOffsetX())
					{
						updatedChunks[i] = chunkStorage.handleChunk(terrainRenderer.renderedChunks[i].getOffsetX() - boxLength, 
								terrainRenderer.renderedChunks[i].getOffsetZ());
					}
					else
					{
						updatedChunks[i] = chunkStorage.handleChunk(terrainRenderer.renderedChunks[i].getOffsetX() + boxLength, 
								terrainRenderer.renderedChunks[i].getOffsetZ());
					}
				}
				else if(Math.abs(terrainRenderer.renderedChunks[i].getOffsetZ() - chunkStorage.getCurrentChunk().getOffsetZ()) > terrainRenderer.RENDER_DISTANCE)
				{	
					if(terrainRenderer.renderedChunks[i].getOffsetZ() > chunkStorage.getCurrentChunk().getOffsetZ())
					{
						updatedChunks[i] = chunkStorage.handleChunk(terrainRenderer.renderedChunks[i].getOffsetX(), 
								terrainRenderer.renderedChunks[i].getOffsetZ() - boxLength);
					}
					else
					{
						updatedChunks[i] = chunkStorage.handleChunk(terrainRenderer.renderedChunks[i].getOffsetX(), 
								terrainRenderer.renderedChunks[i].getOffsetZ() + boxLength);
					}
				}
			}
		}
	}
	
	public static Chunk getUpdatedChunk(int chunkX, int chunkZ)
	{
		for(int i = 0; i < updatedChunks.length; i++)
		{
			if(updatedChunks[i].getOffsetX() == chunkX && updatedChunks[i].getOffsetZ() == chunkZ)
			{
				return updatedChunks[i];
			}
		}
		System.err.println("ERR_NO_CHUNK_RETURNED_at_ChunkUpdater.java");
		return null;
	}
}
