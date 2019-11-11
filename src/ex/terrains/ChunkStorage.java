package ex.terrains;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import ex.gen.ValueNoise;
import ex.graphicsCore.Loader;

public class ChunkStorage
{
	private Chunk currentChunk;
	
	ValueNoise valueNoise;	
	
	Loader loader;
	
	String WORLD_NAME = "DUMPSTER";
	String worldPath;
	
	public ChunkStorage(Loader loader, Vector2f START_OFFSET, Vector4f PRELOADED)
	{
		worldInit();
		this.loader = loader;
		addChunks((int) PRELOADED.x, (int) PRELOADED.y, (int) PRELOADED.z, (int) PRELOADED.w);
		currentChunk = getChunk((int) START_OFFSET.x, (int) START_OFFSET.y);
	}
	
	public void worldInit()
	{
		worldPath = "Saves/" + WORLD_NAME + "/";
		File world = new File(worldPath);
		if(world.exists()) 
		{ 
			try 
			{
				FileInputStream fileInput = new FileInputStream(worldPath + "DATA.txt");
				ObjectInputStream objectInput = new ObjectInputStream(fileInput);			
				try
				{
					valueNoise = (ValueNoise) objectInput.readObject();
				}
				finally
				{
					objectInput.close();
			    }
			}
			catch(IOException | ClassNotFoundException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			world.mkdirs();
			valueNoise = new ValueNoise();
			try 
			{						
				FileOutputStream fileOutput = new FileOutputStream(worldPath + "DATA.txt");
				ObjectOutput objectOutput = new ObjectOutputStream(fileOutput);		
				try
				{
					objectOutput.writeObject(valueNoise);
				}
				finally
				{
					objectOutput.close();
				}
			}
			catch(IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void addChunk(int offsetX, int offsetZ, ValueNoise noise)
	{			
		File chunkFile = new File(worldPath + Integer.toString(offsetX) + " " + Integer.toString(offsetZ) + ".txt");
		if(!chunkFile.exists()) 
		{ 
			try 
			{
				Chunk chunk = new Chunk(offsetX, offsetZ, noise);				
				FileOutputStream fileOutput = new FileOutputStream(worldPath + Integer.toString(offsetX) + " " + Integer.toString(offsetZ) + ".txt");
				ObjectOutput objectOutput = new ObjectOutputStream(fileOutput);		
				try
				{
					objectOutput.writeObject(chunk);
				}
				finally
				{
					objectOutput.close();
				}
			}
			catch(IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void addChunks(int minX, int minZ, int maxX, int maxZ)
	{
		for(int offsetX = minX; offsetX < maxX; offsetX++)
		{
			for(int offsetZ = minZ; offsetZ < maxZ; offsetZ++)
			{
				addChunk(offsetX, offsetZ, valueNoise);
			}
		}
	}
	
	public Chunk getChunk(int offsetX, int offsetZ)
	{
		Chunk chunk = null;
		try 
		{
			FileInputStream fileInput = new FileInputStream(worldPath + Integer.toString(offsetX) + " " + Integer.toString(offsetZ) + ".txt");
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);			
			try
			{
				chunk = (Chunk) objectInput.readObject();
			}
			finally
			{
				objectInput.close();
				//System.out.println("CHUNK LOADED");
		    }
		}
		catch(IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return chunk;
	}
	
	public Chunk handleChunk(int offsetX, int offsetZ)
	{
		addChunk(offsetX, offsetZ, valueNoise);
		return getChunk(offsetX, offsetZ);
	}
	
	public void updateCurrentChunk(Vector3f playerPosition, int renderedIndex, Chunk[] renderedChunks)
	{
			if((renderedChunks[renderedIndex].getOffsetX() + 1) * Chunk.CHUNK_LENGTH > playerPosition.x &&
					playerPosition.x >= (renderedChunks[renderedIndex].getOffsetX()) * Chunk.CHUNK_LENGTH &&
							(renderedChunks[renderedIndex].getOffsetZ() + 1) * Chunk.CHUNK_LENGTH > playerPosition.z && 
								playerPosition.z >= (renderedChunks[renderedIndex].getOffsetZ()) * Chunk.CHUNK_LENGTH)
			{
				currentChunk = renderedChunks[renderedIndex];
			}
	}
	
	public static Vector2f getRelativeToChunkCoords(float x, float y)
	{
		x = Math.abs(x);
		y = Math.abs(y);
		int relativeX = 0;
		int relativeY = 0;
		
		if(x < Chunk.CHUNK_LENGTH)
		{
			relativeX = (int) x;
		}
		else
		{
			relativeX = (int) (x % Chunk.CHUNK_LENGTH);
		}
		
		if(y < Chunk.CHUNK_LENGTH)
		{
			relativeY = (int) y;
		}
		else
		{
			relativeY = (int) (y % Chunk.CHUNK_LENGTH);
		}
		
		return new Vector2f(relativeX, relativeY);
	}
	
	public boolean ifChunkCrossed(Vector3f playerPosition)
	{
		return ((int) playerPosition.x - (currentChunk.getOffsetX() - 1) * Chunk.CHUNK_LENGTH <= 0 || 
				(int) playerPosition.z - (currentChunk.getOffsetZ() - 1) * Chunk.CHUNK_LENGTH <= 0 ||
				(int) playerPosition.x - (currentChunk.getOffsetX() - 1) * Chunk.CHUNK_LENGTH >= Chunk.CHUNK_LENGTH || 
				(int) playerPosition.z - (currentChunk.getOffsetZ() - 1) * Chunk.CHUNK_LENGTH >= Chunk.CHUNK_LENGTH);
	}
	
	public Chunk getCurrentChunk()
	{
		return currentChunk;
	}
}
