package ex.environmentCore;

public class TexturedModel 
{	
	public Model rawModel;
	public Texture texture;
	
	public TexturedModel(Model rawModel, Texture texture)
	{
		this.rawModel = rawModel;
		this.texture = texture;
	}
	
	public int getVaoID() 
	{
		return rawModel.getVaoID();
	}

	public int getVertexAmount() 
	{
		return rawModel.getVertexAmount();
	}
	
	public int getTextureID()
	{
		return texture.getTextureID();
	}
}								