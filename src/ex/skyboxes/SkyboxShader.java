package ex.skyboxes;

import org.lwjgl.util.vector.Matrix4f;

import ex.graphicsCore.Camera;
import ex.shaders.ShaderProgram;
import ex.utils.MatrixUtils;
 
public class SkyboxShader extends ShaderProgram
{
    private int projectionMatrixLocation;
    private int viewMatrixLocation;

    public SkyboxShader(String VERTEX_FILE, String FRAGMENT_FILE) 
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    @Override
	protected void bindAttributes() 
    {
        super.bindAttribute(0, "position");
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
    	viewMatrix.m30 = 0;
    	viewMatrix.m31 = 0;
    	viewMatrix.m32 = 0;
    	super.loadMatrix(viewMatrixLocation, viewMatrix);
    }   
}