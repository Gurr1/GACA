package hills.engine.system.camera;

import hills.engine.display.Display;
import hills.engine.input.Keyboard;
import hills.engine.input.Mouse;
import hills.engine.math.Mat4;
import hills.engine.math.Quaternion;
import hills.engine.math.Vec3;
import hills.engine.math.shape.Frustrum;
import hills.engine.renderer.shader.ShaderProgram;
import hills.engine.system.EngineSystem;
import hills.engine.system.domainModel.World;
import lombok.Getter;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public class CameraSystem extends EngineSystem {

	private static CameraSystem instance = null;
	
	private enum Direction {
		FORWARD(1.0f), BACKWARD(-1.0f), LEFT(-1.0f), RIGHT(1.0f), NONE(0.0f);
		
		private final float multiplier;
		private Direction(float multiplier){
			this.multiplier = multiplier;
		}
	}
	
	/**
	 * Going forwards/backwards.
	 */
	private Direction medial = Direction.NONE;
	
	/**
	 * Going left/right.
	 */
	private Direction lateral = Direction.NONE;
	
	/**
	 * Forwards/Backwards speed.
	 */
	private float medialSpeed = 10.0f;
	
	/**
	 * Left/Right speed.
	 */
	private float lateralSpeed = 10.0f;
	
	/**
	 * Camera world position.
	 */
	@Getter private Vec3 position;
	
	/**
	 * Camera forward direction.
	 */
	private Vec3 forward;
	
	/**
	 * Camera up direction.
	 */
	private Vec3 up;
	
	/**
	 * Camera right direction.
	 */
	private Vec3 right;
	
	/**
	 * Camera view frustrum.
	 */
	@Getter private Frustrum frustrum;
	
	/**
	 * Distance to near plane of view frustrum
	 */
	private float near;
	
	/**
	 * Distance to far plane of view frustrum
	 */
	private float far;
	
	/**
	 * Aspect ratio of display area
	 */
	private float aspect;
	
	/**
	 * Vertical field of view in degrees
	 */
	private float FOV;
	
	/**
	 * To update camera or not (No need to update when nothing has changed).
	 */
	private boolean toUpdate;
	private World w;
	private CameraSystem(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
		
		// Initialize camera at 0, 0, 0. Right is +X, Up is +Y, Depth is -Z.
		this.position = new Vec3(100, 100, 0);
		this.forward = new Vec3(0.0f, 0.0f, -1.0f);
		this.up = new Vec3(0.0f, 1.0f, 0.0f);
		this.right = new Vec3(1.0f, 0.0f, 0.0f);
		w = World.getInstance();
		frustrum = new Frustrum(near, far, aspect, FOV, position, forward, up, right, false);
		
		try(MemoryStack stack = MemoryStack.stackPush()){
			Mat4 identityMatrix = Mat4.identity();
			ByteBuffer dataBuffer = stack.calloc(identityMatrix.get140DataSize());
			identityMatrix.get140Data(dataBuffer);
			dataBuffer.flip();
			
			ShaderProgram.map("VIEW", "CAMERA", dataBuffer);
		}
		
		try(MemoryStack stack = MemoryStack.stackPush()){
			ByteBuffer dataBuffer = stack.calloc(position.get140DataSize());
			position.get140Data(dataBuffer);
			dataBuffer.flip();
			
			ShaderProgram.map("VIEW", "CAMPOSWORLD", dataBuffer);
		}
		
		toUpdate = false;
	}
	
	protected void update(double delta) {
		// Check input
		input();
		
		// If nothing has changed don't update!
		if(!toUpdate)
			return;
		
		// Make sure forward, up and right vectors are normalized.
		forward = forward.normalize();
		up = up.normalize();
		right = forward.cross(up);
		
		// Move camera
		position = position.add(forward.mul(medialSpeed * medial.multiplier * (float) delta));
		position = position.add(right.mul(lateralSpeed * lateral.multiplier * (float) delta));
		w.setPlayerPosition(position);
		medial = Direction.NONE;
		lateral = Direction.NONE;
		
		// Construct camera matrix according to position, and direction vectors.
		Mat4 cameraMatrix = Mat4.look(position, forward, up, right, false);
		
		// Update camera view frustrum
		frustrum = new Frustrum(near, far, aspect, FOV, position, forward, up, right, false);
		
		try(MemoryStack stack = MemoryStack.stackPush()){
			ByteBuffer dataBuffer = stack.calloc(cameraMatrix.get140DataSize());
			cameraMatrix.get140Data(dataBuffer);
			dataBuffer.flip();
			
			// Map camera matrix to uniform buffer VIEW
			ShaderProgram.map("VIEW", "CAMERA", dataBuffer);
		}
		
		try(MemoryStack stack = MemoryStack.stackPush()){
			ByteBuffer dataBuffer = stack.calloc(position.get140DataSize());
			position.get140Data(dataBuffer);
			dataBuffer.flip();
			
			// Map camera position to uniform buffer VIEW
			ShaderProgram.map("VIEW", "CAMPOSWORLD", dataBuffer);
		}
		
		// Set toUpdate false
		toUpdate = false;
	}
	
	/**
	 * Move camera according to input.
	 */
	private void input(){
		if(Keyboard.isDown(GLFW.GLFW_KEY_W))
			setDirection(Direction.FORWARD);
		
		if(Keyboard.isDown(GLFW.GLFW_KEY_S))
			setDirection(Direction.BACKWARD);
		
		if(Keyboard.isDown(GLFW.GLFW_KEY_A))
			setDirection(Direction.LEFT);
		
		if(Keyboard.isDown(GLFW.GLFW_KEY_D))
			setDirection(Direction.RIGHT);
		
		if(Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)){
			medialSpeed *= 2.0f;
			lateralSpeed *= 2.0f;
		}
		
		if(Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_CONTROL)){
			medialSpeed *= 0.5f;
			lateralSpeed *= 0.5f;
		}
		
		if(Keyboard.isPressed(GLFW.GLFW_KEY_SPACE))
			if(Display.isMouseCaptured())
				Display.captureMouse(false);
			else
				Display.captureMouse(true);
		
		if(Display.isMouseCaptured()){
			float yaw = -Mouse.getXVelocity() * 0.3f;
			float pitch = -Mouse.getYVelocity() * 0.3f;
			
			if(yaw != 0.0f)
				rotate(yaw, 0.0f, 1.0f, 0.0f); // ROTATE AROUND WORLD UP-AXIS
			
			if(pitch != 0.0f)
				pitch(pitch);
		}
	}
	
	/**
	 * Rotate camera along cameras right axis.
	 * @param degrees - Degrees to rotate.
	 */
	public void pitch(float degrees){
		rotate(degrees, right);
	}
	
	/**
	 * Rotate camera along cameras up axis.
	 * @param degrees - Degrees to rotate.
	 */
	public void yaw(float degrees){
		rotate(degrees, up);
	}
	
	/**
	 * Rotate camera along cameras forward axis.
	 * @param degrees - Degrees to rotate.
	 */
	public void roll(float degrees){
		rotate(degrees, forward);
	}
	
	/**
	 * Rotate camera along axis x, y, z.
	 * @param angle - degrees to rotate.
	 * @param x - X component of axis to rotate around.
	 * @param y - Y component of axis to rotate around.
	 * @param z - Z component of axis to rotate around.
	 */
	public void rotate(float angle, float x, float y, float z){
		rotate(angle, new Vec3(x, y, z));
	}
	
	private void rotate(float angle, Vec3 axis){
		Quaternion rotQuat = new Quaternion(axis, angle);
		
		forward = rotQuat.mul(forward).normalize();
		up = rotQuat.mul(up).normalize();
		right = forward.cross(up);
		
		toUpdate = true;
	}
	
	/**
	 * Set direction for camera to move in.
	 * @param dir - Direction to move in.
	 * If set to NONE camera will stop.
	 */
	public void setDirection(Direction dir){
		switch(dir){
		case FORWARD:
			medial = Direction.FORWARD;
			break;
		case BACKWARD:
			medial = Direction.BACKWARD;
			break;
		case LEFT:
			lateral = Direction.LEFT;
			break;
		case RIGHT:
			lateral = Direction.RIGHT;
			break;
		case NONE:
			medial = Direction.NONE;
			lateral = Direction.NONE;
			break;
		}
		
		toUpdate = true;
	}
	
	/**
	 * Update the perspective matrix used when rendering. The camera system<br>
	 * will also store the near, far, aspect, FOV, values for computing of the<br>
	 * view frustrum. Will also trigger camera system to update.
	 * @param near
	 * @param far
	 * @param aspect
	 * @param FOV
	 */
	public void updatePerspective(float near, float far, float aspect, float FOV){
		this.near = near;
		this.far = far;
		this.aspect = aspect;
		this.FOV = FOV;
		
		try(MemoryStack stack = MemoryStack.stackPush()){
			Mat4 perspectiveMatrix = Mat4.perspective(near, far, aspect, FOV);
			
			ByteBuffer dataBuffer = stack.calloc(perspectiveMatrix.get140DataSize());
			perspectiveMatrix.get140Data(dataBuffer);
			dataBuffer.flip();
			
			ShaderProgram.map("VIEW", "PERSPECTIVE", dataBuffer);
		}
		
		toUpdate = true;
	}
	
	public void render() {}
	
	public void cleanUp() {
		System.out.println("Camera system cleaned up!");
	}
	
	/**
	 * Creates the singleton instance of CameraSystem.
	 * @return False if an instance has already been created.
	 */
	public static boolean createInstance(float scale, boolean isPaused, float startTime) {
		if(instance != null)
			return false;
		
		instance = new CameraSystem(scale, isPaused, startTime);
		return true;
	}
	
	/**
	 * 
	 * @return The singleton instance of CameraSystem.
	 * @throws NullPointerException If singleton instance has not been created.
	 */
	public static CameraSystem getInstance() throws NullPointerException {
		if(instance == null)
			throw new NullPointerException("Singleton instance not created!");
		
		return instance;
	}

}
