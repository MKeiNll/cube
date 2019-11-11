package ex.graphicsCore;
 
import org.lwjgl.opengl.Display;

import ex.core.Window;

public class WindowLoop 
{
	
	public WindowLoop()
	{
		scene();
	}
	
	public static void scene()
	{	
		MasterRenderer masterRenderer = new MasterRenderer();
		
		while(!Window.isCloseRequested())
		{
			masterRenderer.render();
			
			Display.update();
			//Display.sync(60);
		}
	}
}