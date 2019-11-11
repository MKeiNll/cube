package ex.utils;

import org.lwjgl.Sys;

public class Time 
{
	public int delta;
	public int fps;
	public long lastFPS = getTime();
	public long lastFrame;
	
	private int screenFPS;
	
	public long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	
	public int getDelta()
	{
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		return delta;
	}
	
	public void countFPS(Fonts font)
	{
        if(getTime() - lastFPS > 1000)
		{
        	screenFPS = fps;
			fps = 0;
			lastFPS += 1000;
		}
        font.draw(5, 0, "FPS: " + screenFPS);
		fps++;
	}
}
