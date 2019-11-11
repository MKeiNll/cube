package ex.shaders;

public class UiShader extends ShaderProgram 
{
    public UiShader(String VERTEX_FILE, String FRAGMENT_FILE) 
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    @Override
	protected void bindAttributes() 
    {
    	
    }
    
    @Override
    protected void getUniformLocations()
    {
    	
    }

}