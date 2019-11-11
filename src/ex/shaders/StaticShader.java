package ex.shaders;

import org.lwjgl.util.vector.Matrix4f;

import ex.graphicsCore.Camera;
import ex.utils.MatrixUtils;

public class StaticShader extends ShaderProgram 
{
    private int projectionMatrixLocation;
    private int viewMatrixLocation;

    public StaticShader(String VERTEX_FILE, String FRAGMENT_FILE) 
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    @Override
	protected void bindAttributes() 
    {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "transformationMatrix");
    }
    
    @Override
    protected void getUniformLocations()
    {
    	projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
    	viewMatrixLocation = super.getUniformLocation("viewMatrix");  
    }
    
    public void loadProjectionMatrix(Matrix4f projectionMatrix)
    {
    	super.loadMatrix(projectionMatrixLocation, projectionMatrix);
    }
    
    public void loadViewMatrix(Camera camera)
    {
    	Matrix4f viewMatrix = MatrixUtils.createViewMatrix(camera.pitch, camera.yaw, camera.position);
    	super.loadMatrix(viewMatrixLocation, viewMatrix);
    }   
}