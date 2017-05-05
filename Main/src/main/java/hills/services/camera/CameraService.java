package hills.services.camera;

import hills.services.Service;
import hills.util.math.Vec3;
import hills.util.math.shape.Frustrum;

public class CameraService implements Service, CameraDataService {
	
	private final CameraData data;
	
	public CameraService(){
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
