package ex.utils;

import java.awt.Font;
import java.io.InputStream;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class Fonts 
{
	private TrueTypeFont font;
	
	public Fonts()
	{
		setUpFonts();
	}
	
	private void setUpFonts()
	{
		try {
			InputStream inputStream	= ResourceLoader.getResourceAsStream("res/Fonts/Glametrix.otf");
 
			Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont = awtFont.deriveFont(40f); // set font size
			font = new TrueTypeFont(awtFont, false);
 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
	}
	
	public void draw(int posX, int posY, String text)
	{
		font.drawString(posX, posY, text, Color.green);
	}
}
