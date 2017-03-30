package hills.engine.math;


//TODO Make mutable?
public final class Quaternion {
	
	private final Vec4 versor;
	
	/**
	 * Create a new Quaternion with x, y, z, angle set to 0.
	 */
	public Quaternion() {
		this(new Vec4(0.0f, 0.0f, 0.0f, 0.0f));
	}
	
	/**
	 * Create a new Quaternion.
	 * @param x - x component of rotation axis.
	 * @param y - y component of rotation axis.
	 * @param z - z component of rotation axis.
	 * @param angle - Degrees of rotation around rotation axis.
	 */
	public Quaternion(float x, float y, float z, float angle) {
		this(new Vec4(x, y, z, angle));
	}
	
	/**
	 * Create a new Quaternion.
	 * @param axis - Rotation axis.
	 * @param angle - Degrees of rotation around rotation axis.
	 */
	public Quaternion(Vec3 axis, float angle) {
		this(new Vec4(axis, angle));
	}
	
	/**
	 * Create a new Quaternion with its values copied from q.
	 * @param q - The quaternion to copy values from.
	 */
	public Quaternion(Quaternion q){
		this(new Vec4(q.getAxis(), q.getAngle()));
	}
	
	/**
	 * Create a new Quaternion.
	 * @param vers - the quaternion defined by a rotation axis (x, y, z) and an angle in degrees (w).
	 */
	public Quaternion(final Vec4 vers) {
		float halfAngleInRad = (float) Math.toRadians((vers.getW() / 2));
		
		float x = (float) (Math.sin(halfAngleInRad) * vers.getX());
		float y = (float) (Math.sin(halfAngleInRad) * vers.getY());
		float z = (float) (Math.sin(halfAngleInRad) * vers.getZ());
		float w = (float) Math.cos(halfAngleInRad);
		
		versor = new Vec4(x, y, z, w);
	}
	
	/**
	 * Multiply with a quaternion.
	 * @param quat - The quaternion to multiply with.
	 * @return A new Quaternion made from the product of this quaternion * quat.
	 */
	public Quaternion mul(final Quaternion quat) {
		float x = quat.versor.getX();
		float y = quat.versor.getY();
		float z = quat.versor.getZ();
		float w = quat.versor.getW();
		
		float qx = w * versor.getX() + x * versor.getW() - y * versor.getZ() + z * versor.getY();
		float qy = w * versor.getY() + x * versor.getZ() + y * versor.getW() - z * versor.getX();
		float qz = w * versor.getZ() - x * versor.getY() + y * versor.getX() + z * versor.getW();
		float qw = w * versor.getW() - x * versor.getX() - y * versor.getY() - z * versor.getZ();

		return new Quaternion(qw, qx, qy, qz);
	}
	
	/**
	 * Multiply with a vector.
	 * @param vector - The vector to multiply with.
	 * @return A new Vec3 made from the product of this quaternion * vector.
	 */
	public Vec3 mul(final Vec3 vector) {
		Vec3 v = getAxis().cross(vector);
		return vector.add(v.mul(2 * versor.getW()).add(getAxis().cross(v).mul(2)));
	}
	
	/**
	 * Get the inverse.
	 * @return The inverse of this quaternion.
	 */
	public Quaternion inverse() {
		return new Quaternion(getVersor().mul(new Vec4(-1.0f, -1.0f, -1.0f, 1.0f)));
	}
	
	public Quaternion slerp(final Quaternion quat1, final Quaternion quat2, final float t) { // Spherical interpolation between 2 quaternions
		Vec4 v1 = quat1.getVersor().normalize();
		Vec4 v2 = quat2.getVersor().normalize();

		float dot = v1.dot(v2);

		final float DOT_THRESHOLD = 0.9995f;
		if (dot > DOT_THRESHOLD) // If inputs very close, linearly interpolate + normalize.
			return new Quaternion((v1.add(v2.sub(v1).mul(t))).normalize());

		// If dot product negative, slerp will not take shortest path
		// Fix by reversing one quaternion.
		if(dot < 0.0f){
			v1 = v1.mul(-1);
			dot = -dot;
		}
		
		dot = Math.max(-1.0f, Math.min(1.0f, dot));	// Clamp dot product to stay within domain of acos()
		float theta_0 = (float) Math.acos(dot);  	// theta_0 = angle between input vectors
	    float theta = theta_0 * t;    				// theta = angle between v0 and result 
	    
	    Vec4 v3 = v2.sub(v1.mul(dot)).normalize();	// { v1, v3 } is now an orthonormal basis
	    return new Quaternion(v1.mul(((float) Math.cos(theta))).add( v3.mul((float) Math.sin(theta))));
	}
	
	/**
	 * @return A new quaternion with it's versor normalized.
	 */
	public Quaternion normalize() {
		return new Quaternion(versor.normalize());
	}

	/**
	 * @return A matrix that describes this quaternion.
	 */
	public Mat4 getMatrix() {
		Vec4 normVersor = versor.getLengthSqr() != 1.0f ? versor.normalize() : new Vec4(versor);

		float x = normVersor.getX();
		float y = normVersor.getY();
		float z = normVersor.getZ();
		float w = normVersor.getW();

		float[] mat = {
			//Column 1
			1.0f - 2.0f * y * y - 2.0f * z * z,
			2.0f * x * y + 2.0f * w * z,
			2.0f * x * z - 2.0f * w * y,
			0.0f,

			//Column 2
			2.0f * x * y - 2.0f * w * z,
			1.0f - 2.0f * x * x - 2.0f * z * z,
			2.0f * y * z + 2.0f * w * x,
			0.0f,

			//Column 3
			2.0f * x * z + 2.0f * w * y,
			2.0f * y * z - 2.0f * w * x,
			1.0f - 2.0f * x * x - 2.0f * y * y,
			0.0f,

			//Column 4
			0.0f,
			0.0f,
			0.0f,
			1.0f
		};

		return new Mat4(mat);
	}
	
	public Vec4 getVersor(){
		return versor;
	}
	
	public Vec3 getAxis(){
		return new Vec3(versor.getX(), versor.getY(), versor.getZ());
	}
	
	public float getAngle(){
		return versor.getW();
	}
}
