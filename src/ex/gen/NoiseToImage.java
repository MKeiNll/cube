package ex.gen;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NoiseToImage 
{
	public static void greyWhiteImage(int[][] data)
	{
		BufferedImage image = new BufferedImage(data.length, data[0].length, BufferedImage.TYPE_INT_RGB);
		for(int y = 0; y < data[0].length; y++)
		{
			for(int x = 0; x < data.length; x++)
			{
				if(data[x][y] > 1)
				{
					data[x][y] = 1;
				}
				if(data[x][y] < 0)
				{
					data[x][y] = 0;
				}
				Color col = new Color(data[x][y], data[x][y], data[x][y]);
				image.setRGB(x, y, col.getRGB());
			}
		}
		
		File outputfile = new File("noise.png");
		try 
		{
			outputfile.createNewFile();
			ImageIO.write(image, "png", outputfile);
		} 
			catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
