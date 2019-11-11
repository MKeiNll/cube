package ex.main;
import java.io.File;

import ex.core.Window;
import ex.graphicsCore.WindowLoop;

public class Main
{
	public static void main(String[] args)
	{	
		System.setProperty("org.lwjgl.librarypath", new File("natives/windows").getAbsolutePath());		
		start(); 
		render(); 
		close(); 
	}
	
	private static void start()
	{
		Window SD = new Window();
		SD.launch();
	}
	
	private static void render()
	{
		new WindowLoop();		
		
		//TODO
		//Draw a crosshair at the center of the screen as a texture (Just go through tutorial 6 and check each step)
		
		//TODO
		//Print exact block position, not point on ray position
		
		//TODO
		//Highlight current block
	}
	
	private static void close()
	{
		Window.close();
	}	
}		