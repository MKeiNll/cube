package ex.graphicsCore;

import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;

public class Camera 
{
	public float distanceFromPlayer = 50;
	public float angleAroundPlayer = 0;
	public float horizontalDistance;
	public float verticalDistance;
	
	public Vector3f position = new Vector3f(0, 0, 0);
	public float yaw;
	public float pitch = 20;
	
	int delta;
	int fps;
	long lastFPS = getTime();
	long lastFrame;
	
	private long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
}	