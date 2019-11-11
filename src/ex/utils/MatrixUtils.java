package ex.utils;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MatrixUtils 
{
	public static Matrix4f createProjectionMatrix()
	{	
		Matrix4f projectionMatrix;
		float FOV = 75;
		float aspectRatio = (float)Display.getWidth() / (float)Display.getHeight();
		float NEAR_PLANE = 0.1f;
		float FAR_PLANE = 10000000;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((NEAR_PLANE + FAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;	
		
		return projectionMatrix;
	}
	
	public static Matrix4f createViewMatrix(float pitch, float yaw, Vector3f position)
	{
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Vector3f negativePosition = new Vector3f(-position.x, -position.y, -position.z);
		Matrix4f.translate(negativePosition, viewMatrix, viewMatrix);
		return viewMatrix;		
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rotX, float rotY, float rotZ)
	{
		Matrix4f transformationMatrix = new Matrix4f();
		transformationMatrix.setIdentity();
		Matrix4f.translate(translation, transformationMatrix, transformationMatrix);
		Matrix4f.rotate((float) Math.toRadians(rotX), new Vector3f(1, 0, 0), transformationMatrix, transformationMatrix);
		Matrix4f.rotate((float) Math.toRadians(rotY), new Vector3f(0, 1, 0), transformationMatrix, transformationMatrix);
		Matrix4f.rotate((float) Math.toRadians(rotZ), new Vector3f(0, 0, 1), transformationMatrix, transformationMatrix);
		Matrix4f.scale(new Vector3f(1, 1, 1), transformationMatrix, transformationMatrix);
		return transformationMatrix;		
	}
}
