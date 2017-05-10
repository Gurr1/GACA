package hills.services.camera;

import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryStack;

import hills.services.Service;
import hills.util.math.Mat4;
import hills.util.math.Vec3;
import hills.util.math.shape.Frustrum;
import hills.util.shader.ShaderProgram;

public class CameraService implements Service, ICameraDataService, ICameraUpdateService {
	
	private final CameraData data;
	
	private boolean toUpdate = true;
	
	public CameraService(){
		data = new CameraData();
	}
	
	public void updateGPUCameraMatrix(){
		if(!toUpdate)
			return;
		
		Vec3 position = data.getPosition();
		Vec3 forward = data.getForward();
		Vec3 up = data.getUp();
		Vec3 right = data.getRight();
		
		// Construct camera matrix according to position, and direction vectors.
		Mat4 cameraMatrix = Mat4.look(position, forward, up, right, false);
		
		// Update camera matrix
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
		
		toUpdate = false;
	}
	
	public void updateGPUPerspectiveMatrix(){
		try(MemoryStack stack = MemoryStack.stackPush()){
			Mat4 perspectiveMatrix = Mat4.perspective(data.getNear(), data.getFar(), data.getAspect(), data.getFov());
			
			ByteBuffer dataBuffer = stack.calloc(perspectiveMatrix.get140DataSize());
			perspectiveMatrix.get140Data(dataBuffer);
			dataBuffer.flip();
			
			ShaderProgram.map("VIEW", "PERSPECTIVE", dataBuffer);
		}
	}
	
	public void setPosition(Vec3 position){
		data.setPosition(position);
		toUpdate = true;
	}
	
	public void setOrientation(Vec3 right, Vec3 up, Vec3 forward, boolean normalize){
		data.setOrientation(right, up, forward, normalize);
		toUpdate = true;
	}
	
	public void setPerspective(float near, float far, float aspect, float fov){
		data.setPerspective(near, far, aspect, fov);
	}
	
	public Vec3 getPosition(){
		return data.getPosition();
	}
	
	public Frustrum getFrustrum(){
		return data.getFrustrum();
	}
	
	@Override
	public void cleanUp() {
		
	}

}
