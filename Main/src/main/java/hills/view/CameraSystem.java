package hills.view;

import hills.util.math.Mat4;
import hills.util.math.Vec3;
import hills.util.shader.ShaderProgram;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public class CameraSystem {


	public void update(Vec3 position, Vec3 forward, Vec3 up, Vec3 right) {

		
		// Move camera
		
		// Construct camera matrix according to position, and direction vectors.
		Mat4 cameraMatrix = Mat4.look(position, forward, up, right, false);
		
		// Update camera view frustrum
		
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
	}
	
	/**
	 * Move camera according to input.
	 */
	private void input(){


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
		try(MemoryStack stack = MemoryStack.stackPush()){
			Mat4 perspectiveMatrix = Mat4.perspective(near, far, aspect, FOV);
			
			ByteBuffer dataBuffer = stack.calloc(perspectiveMatrix.get140DataSize());
			perspectiveMatrix.get140Data(dataBuffer);
			dataBuffer.flip();
			
			ShaderProgram.map("VIEW", "PERSPECTIVE", dataBuffer);
		}
	}
}
