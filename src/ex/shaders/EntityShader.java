package ex.shaders;

import org.lwjgl.util.vector.Matrix4f;

import ex.graphicsCore.Camera;
import ex.utils.MatrixUtils;

public class EntityShader extends ShaderProgram
{   
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int transformationMatrixLocation;

    public EntityShader(String VERTEX_FILE, String FRAGMENT_FILE) 
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
    	transformationMatrixLocation = super.getUniformLocation("transformationMatrix"); 
    }
    
    public void loadProjectionMatrix(Matrix4f projectionMatrix)
    {
    	super.loadMatrix(projectionMatrixLocation, projectionMatrix);
    }
    
    public void loadTransformationMatrix(Matrix4f transformationMatrix)
    {
    	super.loadMatrix(transformationMatrixLocation, transformationMatrix);
    }
    
    public void loadViewMatrix(Camera camera)
    {
    	Matrix4f viewMatrix = MatrixUtils.createViewMatrix(camera.pitch, camera.yaw, camera.position);
    	super.loadMatrix(viewMatrixLocation, viewMatrix);
    }   
}
