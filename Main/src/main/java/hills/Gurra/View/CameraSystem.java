package hills.Gurra.View;

import hills.Gurra.Controllers.PlayerControllerKeyboard;
import hills.Gurra.Controllers.PlayerControllerMouse;
import hills.engine.display.Display;
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

public class CameraSystem {


	
	public void update(double delta) {

		
		// Move camera
		
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


	}
	
	/**
	 * Rotate camera along cameras right axis.
	 * @param degrees - Degrees to rotate.
	 */
	public void setPitch(float degrees){
		rotate(degrees, right);
	}
	
	/**
	 * Rotate camera along cameras up axis.
	 * @param degrees - Degrees to rotate.
	 */
	public void setYaw(float degrees){
		rotate(degrees, up);
	}
	
	/**
	 * Rotate camera along cameras forward axis.
	 * @param degrees - Degrees to rotate.
	 */
	public void setRoll(float degrees){
		rotate(degrees, forward);
	}
	
	/**
	 * Rotate camera along axis x, y, z.
	 * @param angle - degrees to rotate.
	 * @param x - X component of axis to rotate around.
	 * @param y - Y component of axis to rotate around.
	 * @param z - Z component of axis to rotate around.
	 */
	public void setRotate(float angle, float x, float y, float z){
		rotate(angle, new Vec3(x, y, z));
	}
	
	private void setRotate(float angle, Vec3 axis){
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
		try(MemoryStack stack = MemoryStack.stackPush()){
			Mat4 perspectiveMatrix = Mat4.perspective(near, far, aspect, FOV);
			
			ByteBuffer dataBuffer = stack.calloc(perspectiveMatrix.get140DataSize());
			perspectiveMatrix.get140Data(dataBuffer);
			dataBuffer.flip();
			
			ShaderProgram.map("VIEW", "PERSPECTIVE", dataBuffer);
		}
	}
}
