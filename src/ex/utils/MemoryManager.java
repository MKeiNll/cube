package ex.utils;

public class MemoryManager 
{
	int MB = 1024*1024;
	static boolean isShown;
	Runtime runtime;
	
	public MemoryManager()
	{
		runtime = Runtime.getRuntime();
		isShown = false;
	}
		
	public void printData(Fonts font)
	{	
		if(isShown)
		{
			font.draw(5, 30, "Used Memory: " + (runtime.totalMemory() - runtime.freeMemory()) / MB + "MB");
			font.draw(5, 60, "Free Memory: " + runtime.freeMemory() / MB + "MB");
			font.draw(5, 90, "Total Memory: " + runtime.totalMemory() / MB + "MB");
			font.draw(5, 120, "Max Memory: " + runtime.maxMemory() / MB + "MB");
		}
	}
	
	public static void changeMode()
	{
		isShown = !isShown;
	}
}
