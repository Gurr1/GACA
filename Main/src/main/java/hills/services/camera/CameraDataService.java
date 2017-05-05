package hills.services.camera;

import hills.util.math.Vec3;
import hills.util.math.shape.Frustrum;

public interface CameraDataService {
	
	public Vec3 getPosition();
	
	public Frustrum getFrustrum();

}
