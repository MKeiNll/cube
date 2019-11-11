package ex.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import ex.players.Player;
import ex.terrains.ChunkStorage;
import ex.terrains.TerrainRenderer;
import ex.utils.HeapDumper;
import ex.utils.MemoryManager;

public class Input 
{
	Player player;
	TerrainRenderer terrainRenderer;
	ChunkStorage chunkStorage;
	
	public Input(Player player, TerrainRenderer terrainRenderer)
	{
		Mouse.setGrabbed(true);
		this.player = player;
		this.terrainRenderer = terrainRenderer;
		this.chunkStorage = terrainRenderer.chunkStorage;
	}
	
	public void manageInputs(int delta) //Action is performed only once when button is pressed/released
	{
		playerMovement(delta);	
        
	    while(Keyboard.next()) //Key pressed
	    {
	        if (Keyboard.getEventKeyState()) 
	        {
	        	if(Keyboard.getEventKey() == Keyboard.KEY_F2)
	        	{
	        		TerrainRenderer.changeRenderMode();
	        	}      	
	        	if(Keyboard.getEventKey() == Keyboard.KEY_F1)
	        	{
	        		player.currentView = !player.currentView;
	        	}
        	
	            if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) 
	            {
	            	player.jump();
	            }	 
	            if (Keyboard.getEventKey() == Keyboard.KEY_F3) 
	            {
	            	MemoryManager.changeMode();
	            }
	            if (Keyboard.getEventKey() == Keyboard.KEY_F5) 
	            {
	            	HeapDumper.dumpHeap("Heaps/heap.bin", true);
	            }
	            if (Keyboard.getEventKey() == Keyboard.KEY_1) 
	            {
	            	System.out.println("BREAKPOINT1");
	            }
	            if (Keyboard.getEventKey() == Keyboard.KEY_2) 
	            {
	            	System.out.println("BREAKPOINT2");
	            }
	        } 
	        else 
	        {
	            if (Keyboard.getEventKey() == Keyboard.KEY_LSHIFT) 
	            {
	            	player.walk();
	            }
	        }	        
	    }
	    
	    while(Mouse.next())
	    {
	        if (Mouse.getEventButtonState()) 
	        {
	        	
	        } 
	        else 
	        {
	        	if(Mouse.getEventButton() == 1)
	        	{
	        		player.lookForward();
	        	}
	        }	        
	    }
	}
	
	public void playerMovement(int delta)
    {	
		if(!player.currentView)
		{
			if(Mouse.isButtonDown(1))
			{
				player.cameraLookAround();
			}
		}
		
    	if (Keyboard.isKeyDown(Keyboard.KEY_W)) 
    	{
    		player.moveForward(delta);
    	}
    	
    	if (Keyboard.isKeyDown(Keyboard.KEY_S)) 
        {
        	player.moveBackwards(delta);
        }
    	
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) 
        {
        	player.strafeLeft(delta);
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) 
        {
        	player.strafeRight(delta);
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) 
        {
        	player.sprint();
        }
    }
}
