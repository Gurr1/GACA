package hills.services.camera;

import hills.util.math.Vec3;
import hills.util.math.shape.Frustrum;

/**
 * @author Anton
 */
public class CameraData {

    /**
     * Camera world position.
     */
    private Vec3 position;

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
    private float fov;
    
    /**
     * Create new camera data with default values.
     * position - (0, 0, 0).
     * right - (1, 0, 0).
     * up - (0, 1, 0).
     * forward - (0, 0, -1).
     * near - 0.1.
     * far - 3000.
     * aspect - 4:3
     * fov - 75.
     */
    protected CameraData(){
    	position = new Vec3(0.0f, 0.0f, 0.0f);
    	right = new Vec3(1.0f, 0.0f, 0.0f);
    	up = new Vec3(0.0f, 1.0f, 0.0f);
    	forward = new Vec3(0.0f, 0.0f, -1.0f);
    	near = 0.1f;
    	far = 3000.0f;
    	aspect = 4.0f / 3.0f;
    	fov = 75.0f;
    }
    
    /**
     * Create new camera data
     * @param position - camera position data.
     * @param right - camera right orientation data.
     * @param up - camera up orientation data.
     * @param forward - camera forward orientation data.
     * @param near - camera near plane data.
     * @param far - camera far plane data.
     * @param aspect - aspect ratio data.
     * @param fov - camera vertical field of view data.
     */
	protected CameraData(Vec3 position, Vec3 right, Vec3 up, Vec3 forward, float near, float far, float aspect, float fov) {
		this.position = position;
		this.right = right;
		this.forward = forward;
		this.up = up;
		
		setPerspective(near, far, aspect, fov);
	}
	
	protected Vec3 getPosition() {
		return position;
	}

	protected Vec3 getForward() {
		return forward;
	}

	protected Vec3 getUp() {
		return up;
	}

	protected Vec3 getRight() {
		return right;
	}

	protected float getNear() {
		return near;
	}

	protected float getFar() {
		return far;
	}

	protected float getAspect() {
		return aspect;
	}

	protected float getFov() {
		return fov;
	}

	protected void setPerspective(float near, float far, float aspect, float fov){
		this.near = near;
		this.far = far;
		this.aspect = aspect;
		this.fov = fov;
	}
	
	protected void setPosition(float x, float y, float z){
		position = new Vec3(x, y, z);
	}
	
	protected void setPosition(Vec3 position){
		setPosition(position.getX(), position.getY(), position.getZ());
	}
    
	protected void translatePosition(Vec3 translation){
		position = position.add(translation);
	}
	
	protected void translatePosition(float x, float y, float z){
		translatePosition(new Vec3(x, y, z));
	}
	
	protected void setOrientation(Vec3 right, Vec3 up, Vec3 forward, boolean normalize){
		setRight(right, normalize);
		setUp(up, normalize);
		setForward(forward, normalize);
	}
	
	protected void setRight(float x, float y, float z, boolean normalize){
		Vec3 right = new Vec3(x, y, z);
		
		if(normalize)
			right.normalize();
		
		this.right = right;
	}
	
	protected void setRight(Vec3 right, boolean normalize){
		setRight(right.getX(), right.getY(), right.getZ(), normalize);
	}
	
	protected void setUp(float x, float y, float z, boolean normalize){
		Vec3 up = new Vec3(x, y, z);
		
		if(normalize)
			up.normalize();
		
		this.up = up;
	}
	
	protected void setUp(Vec3 up, boolean normalize){
		setUp(up.getX(), up.getY(), up.getZ(), normalize);
	}

	protected void setForward(float x, float y, float z, boolean normalize){
		Vec3 forward = new Vec3(x, y, z);
		
		if(normalize)
			forward.normalize();
		
		this.forward = forward;
	}
	
	protected void setForward(Vec3 forward, boolean normalize){
		setForward(forward.getX(), forward.getY(), forward.getZ(), normalize);
	}
	
	/**
	 * @return A new camera frustrum.
	 */
    protected Frustrum getFrustrum(){
    	return new Frustrum(near, far, aspect, fov, position, forward, up, right, false);
    }
}
