package ex.input;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import ex.graphicsCore.Camera;
import ex.terrains.Chunk;
import ex.terrains.ChunkStorage;
import ex.terrains.ChunkUpdater;
import ex.utils.MatrixUtils;

public class MousePicker 
{
	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 40;
	
	private Vector3f currentRay;
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	private Vector3f currentBlock;
	
	float crosshairX;
	float crosshairY;
	
	public MousePicker(Camera camera, Matrix4f projectionMatrix)
	{
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
		this.viewMatrix = MatrixUtils.createViewMatrix(camera.pitch, camera.yaw, camera.position);		
		crosshairX = Display.getWidth()/2;
		crosshairY = Display.getHeight()/2;
	}
	
	public Vector3f getCurrentRay()
	{
		return currentRay;
	}
	
	public Vector3f getCurrentBlock() 
	{
		return currentBlock;
	}
	
	public void update()
	{
		viewMatrix = MatrixUtils.createViewMatrix(camera.pitch, camera.yaw, camera.position);
		currentRay = calculateCrosshairRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay)) 
		{
			currentBlock = binarySearch(0, 0, RAY_RANGE, currentRay);
		} 
		else 
		{
			currentBlock = null;
		}
	}
	
	private Vector3f calculateCrosshairRay()
	{
		Vector2f normalizedCoords = getNormalizedDeviceCoords(crosshairX, crosshairY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}
	
	private Vector3f toWorldCoords(Vector4f eyeCoords)
	{
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	private Vector4f toEyeCoords(Vector4f clipCoords)
	{
		Matrix4f invertedProjectionMatrix = Matrix4f.invert(projectionMatrix,  null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjectionMatrix, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}
	
	private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY)
	{
		float x = (2f * mouseX) / Display.getWidth() - 	1;
		float y = (2f * mouseY) / Display.getWidth() - 1;
		return new Vector2f(x, y);
	}
	
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camera.position;
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	
	private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) 
	{
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) 
		{
			Vector3f endPoint = getPointOnRay(ray, half);
			return endPoint;
		}
		if (intersectionInRange(start, half, ray)) 
		{
			return binarySearch(count + 1, start, half, ray);
		} 
		else 
		{
			return binarySearch(count + 1, half, finish, ray);
		}
	}

	private boolean intersectionInRange(float start, float finish, Vector3f ray)
	{
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if(!isUnderGround(startPoint) && isUnderGround(endPoint)) 
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}

	private boolean isUnderGround(Vector3f testPoint)
	{
		Vector2f relativePoint = ChunkStorage.getRelativeToChunkCoords(testPoint.getX(), testPoint.getY());
		Chunk chunk = ChunkUpdater.getUpdatedChunk((int) testPoint.getX() / Chunk.CHUNK_LENGTH, (int) testPoint.getZ() / Chunk.CHUNK_LENGTH);
		
		if(chunk != null)
		{
			float height = chunk.getHeights()[(int) relativePoint.getX()][(int) relativePoint.getY()];
			if (testPoint.y < height) 
			{
				return true;
			} 
			return false;
		}
		return false;
	}
}
