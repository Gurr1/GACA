package hills.services.camera;

import hills.services.Service;
import hills.util.math.Vec3;
import hills.util.math.shape.Frustrum;

public enum CameraService implements Service {
	INSTANCE;
	
	private final CameraData data;
	
	private CameraService(){
		data = new CameraData();
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
