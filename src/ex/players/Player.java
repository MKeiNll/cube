package ex.players;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import ex.environmentCore.Entity;
import ex.environmentCore.Model;
import ex.graphicsCore.Camera;
import ex.terrains.Chunk;

public class Player extends Entity
{
	Camera camera;
	
	private float speed = 0.01f;
	private float STRAFE_SPEED = 0.01f;
	private float MAX_ZOOM = 120;
	private float MIN_ZOOM = 15;
	private float MAX_NECK_TURN = 0;
	private float MAX_NECK_TILT = 90;
	private float GRAVITY = -0.00015f;
	private float JUMP_POWER = 0.04f;
	private float PLAYER_HEIGHT = 6.5f;
	
	private float mouseDX;
	private float mouseDY;
	
	private float fallSpeed = 0;
	private boolean isInAir = false;
	
	public boolean currentView = true;
	
	public Player(Model model, Vector3f position, Camera camera) 
	{
		super(model, position);
		this.camera = camera;
	}
	
	public void sprint()
	{
		speed = 0.1f;
		STRAFE_SPEED = 0.1f;
	}
	
	public void walk()
	{
		speed = 0.02f;
		STRAFE_SPEED = 0.01f;
	}
	
	public void moveForward(int delta)
	{
		position.x -= speed * Math.sin(Math.toRadians(-rotY)) * delta;
		position.z += speed * Math.cos(Math.toRadians(-rotY)) * delta;
	}
	
	public void moveBackwards(int delta)
	{
		position.x += speed * Math.sin(Math.toRadians(-rotY)) * delta;
    	position.z -= speed * Math.cos(Math.toRadians(-rotY)) * delta;
	}
	
	public void strafeLeft(int delta)
	{
		position.x -= STRAFE_SPEED * Math.sin(Math.toRadians(-rotY - 90)) * delta;
    	position.z += STRAFE_SPEED * Math.cos(Math.toRadians(-rotY - 90)) * delta;
	}
	
	public void strafeRight(int delta)
	{
		position.x -= STRAFE_SPEED * Math.sin(Math.toRadians(-rotY + 90)) * delta;
    	position.z += STRAFE_SPEED * Math.cos(Math.toRadians(-rotY + 90)) * delta;
	}
	
	public void calculateGravity(int delta, Chunk currentChunk)
	{		
		float currentPosition = 0;
		fallSpeed += GRAVITY * delta;
		position.y += fallSpeed * delta;
		
		if(currentChunk.getOffsetX() < 0 && currentChunk.getOffsetZ() < 0) //Both negative
		{
			currentPosition = PLAYER_HEIGHT + currentChunk.getHeights()
				[(int) Math.abs(position.x) - (Math.abs(currentChunk.getOffsetX()) - 1) * Chunk.CHUNK_LENGTH]
						[(int) Math.abs(position.z) - (Math.abs(currentChunk.getOffsetZ()) - 1) * Chunk.CHUNK_LENGTH];
		}
		else if(currentChunk.getOffsetX() >= 0 && currentChunk.getOffsetZ() < 0) //X positive, Z negative
		{
			currentPosition = PLAYER_HEIGHT + currentChunk.getHeights()
				[(int) position.x - currentChunk.getOffsetX() * Chunk.CHUNK_LENGTH]
						[(int) Math.abs(position.z) - (Math.abs(currentChunk.getOffsetZ()) - 1) * Chunk.CHUNK_LENGTH];
		}
		else if(currentChunk.getOffsetX() < 0 && currentChunk.getOffsetZ() >= 0) //X negative, Z positive
		{
			currentPosition = PLAYER_HEIGHT + currentChunk.getHeights()
				[(int) Math.abs(position.x) - (Math.abs(currentChunk.getOffsetX()) - 1) * Chunk.CHUNK_LENGTH]
					[(int) position.z - currentChunk.getOffsetZ() * Chunk.CHUNK_LENGTH];
		}
		else if(currentChunk.getOffsetX() >= 0 && currentChunk.getOffsetZ() >= 0) //Both positive
		{
			currentPosition = PLAYER_HEIGHT + currentChunk.getHeights()
				[(int) position.x - currentChunk.getOffsetX() * Chunk.CHUNK_LENGTH]
					[(int) position.z - currentChunk.getOffsetZ() * Chunk.CHUNK_LENGTH];
		}
		else
		{
			System.out.println("ERR_NO_CURRENT_POSITION_GENERATED_at_Player.java");
		}
		
		if(position.y < currentPosition)
		{
			fallSpeed = 0;
			position.y = currentPosition;
			isInAir = false;
		}
	}
	
	public void jump()
	{
		if(!isInAir)
		{
			fallSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	public void cameraLookAround()
	{
		camera.angleAroundPlayer -= mouseDX;
		rotY += mouseDX;
	}
	
	public void lookForward()
	{
		camera.angleAroundPlayer = 0;
	}
	
	public void thirdPersonView(Camera camera)
	{	
		//distance in mouse movement from the last getDX() call.
		mouseDX = (float) (Mouse.getDX() * 0.1);
        //distance in mouse movement from the last getDY() call.
        mouseDY = (float) (Mouse.getDY() * 0.1);
        
        rotY -= mouseDX;
        
		if(camera.distanceFromPlayer < MAX_ZOOM && camera.distanceFromPlayer > MIN_ZOOM && MAX_ZOOM != 0) //Zoom Logic
		{
			float zoomLevel = Mouse.getDWheel() * 0.05f;
			camera.distanceFromPlayer -= zoomLevel;
		}
		else if(camera.distanceFromPlayer >= MAX_ZOOM && MAX_ZOOM != 0)
		{
			float zoomLevel = Mouse.getDWheel() * 0.05f;
			if(zoomLevel > 0)
			{				
				camera.distanceFromPlayer -= zoomLevel;
			}
		}
		else if(camera.distanceFromPlayer <= MIN_ZOOM && MIN_ZOOM != 0 && MIN_ZOOM != 0)
		{
			float zoomLevel = Mouse.getDWheel() * 0.05f;
			if(zoomLevel < 0)
			{				
				camera.distanceFromPlayer -= zoomLevel;
			}
		}
		else //Variable == 0
		{
			float zoomLevel = Mouse.getDWheel() * 0.05f;
			camera.distanceFromPlayer -= zoomLevel;
		}
		
		camera.horizontalDistance = (float) (camera.distanceFromPlayer * Math.cos(Math.toRadians(camera.pitch)));
		camera.verticalDistance = (float) (camera.distanceFromPlayer * Math.sin(Math.toRadians(camera.pitch)));
		
		float theta = rotY + camera.angleAroundPlayer;
		float offsetX = (float) (camera.horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (camera.horizontalDistance * Math.cos(Math.toRadians(theta)));
		
		camera.position.x = position.x - offsetX;
		camera.position.z = position.z - offsetZ;
		camera.position.y = position.y + camera.verticalDistance;
		
		camera.yaw = 180 - (theta);
		camera.pitch -= mouseDY;
	} 
	
	public void firstPersonView(Camera camera)
	{
		if(camera.pitch < MAX_NECK_TILT && camera.pitch > -MAX_NECK_TILT && MAX_NECK_TILT != 0)
		{
			camera.pitch -= mouseDY;
		}
		else if(camera.pitch >= MAX_NECK_TILT && MAX_NECK_TILT != 0)
		{
			if(mouseDY > 0)
			{
				camera.pitch -= mouseDY;
			}
		}
		else if(camera.pitch <= -MAX_NECK_TILT && MAX_NECK_TILT != 0)
		{
			if(mouseDY < 0)
			{
				camera.pitch -= mouseDY;
			}			
		}
		else //Variable == 0
		{
			camera.pitch -= mouseDY;
		}
		
		//distance in mouse movement from the last getDX() call.
		mouseDX = (float) (Mouse.getDX() * 0.1);
        //distance in mouse movement from the last getDY() call.
        mouseDY = (float) (Mouse.getDY() * 0.1);
        
        rotY -= mouseDX;
		
        camera.horizontalDistance = -2;
		
		float theta = rotY + camera.angleAroundPlayer;
		float offsetX = (float) (camera.horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (camera.horizontalDistance * Math.cos(Math.toRadians(theta)));
		
		camera.position.x = position.x - offsetX;
		camera.position.z = position.z - offsetZ; 
		camera.position.y = position.y + 1.5f;
		
		camera.yaw = 180 - (theta);
	}
	
	public void view(Camera camera)
	{
		if(currentView)
		{
			firstPersonView(camera);
		}
		else if(!currentView)
		{
			thirdPersonView(camera);
		}
	}
}
