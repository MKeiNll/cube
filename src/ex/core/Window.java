package ex.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window 
{
	public void launch() 
	{
		/* FULLSCREEN
		try 
		{ 
			 	DisplayMode displayMode = null;
		        DisplayMode[] modes = Display.getAvailableDisplayModes();
		        
		         for (int i = 0; i < modes.length; i++)
		         {
		             if (modes[i].getWidth() == 1366 && modes[i].getHeight() == 768 && modes[i].isFullscreenCapable())
		               {
		                    displayMode = modes[i];
		               }
		         }
			Display.setFullscreen(true);
			Display.create();
		} 
		catch (LWJGLException e) 
		{
				e.printStackTrace();
		}
		*/
		try
		
		{
			Display.setDisplayMode(new DisplayMode(1300, 800));
			Display.create();
		} catch (LWJGLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void close()
	{
		Display.destroy();
	}
	
	public static boolean isCloseRequested()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}	