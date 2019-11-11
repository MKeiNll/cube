package ex.ui;

public class CrosshairCore 
{
	public float[] vertices =
			{
				-1, 1, 0,
				1, 1, 0,
				1, -1, 0,
				-1, -1, 0
			};
	
	public float[] textureCoords =
		{
			-1, 1,
			1, 1,
			1, -1, 
			-1, -1
		};
	
	public String TEXTURE_FILE = "crosshair";
}
