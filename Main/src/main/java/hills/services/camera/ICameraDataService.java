package hills.services.camera;

import hills.util.math.Vec3;
import hills.util.math.shape.Frustrum;

/**
 * @author Anton
 */
public interface ICameraDataService {
	
	public void setPosition(Vec3 position);
	public void setOrientation(Vec3 right, Vec3 up, Vec3 forward, boolean normalize);
	public void setPerspective(float near, float far, float aspect, float fov);
	public Vec3 getPosition();
	public Frustrum getFrustrum();

}
