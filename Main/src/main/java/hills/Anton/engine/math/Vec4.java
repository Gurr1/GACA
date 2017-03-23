package hills.Anton.engine.math;

import lombok.Value;

@Value public class Vec4 {
	
	/**
	 * The dimensions of this vector (4)
	 */
	public static final int SIZE = 4;
	
	private final float x, y, z, w;
	
	/**
	 * Will create a new vector with the values of vector and z, w
	 * @param vector - The vector to copy values from.
	 * @param z - The z value of the vector
	 * @param w - The w value of the vector
	 */
	public Vec4(final Vec2 vector, float z, float w){
		this(vector.getX(), vector.getY(), z, w);
	}
	
	/**
	 * Will create a new vector with the values of vector and w
	 * @param vector - The vector to copy values from.
	 * @param w - The w value of the vector
	 */
	public Vec4(final Vec3 vector, float w){
		this(vector.getX(), vector.getY(), vector.getZ(), w);
	}
	
	/**
	 * Will create a new vector with the values of vector
	 * @param vector - The vector to copy values from.
	 */
	public Vec4(final Vec4 vector) {
		this(vector.getX(), vector.getY(), vector.getZ(), vector.getW());
	}
	
	/**
	 * Will create a new vector with the values from vector
	 * @param vector - The float array to copy values from.
	 */
	public Vec4(final float[] vector){
		this(vector[0], vector[1], vector[2], vector[3]);
	}

	/**
	 * Will create a new vector with the values x, y, z
	 * @param x - The x value of the new vector
	 * @param y - The y value of the new vector
	 * @param z - The z value of the new vector
	 * @param w - The w value of the new vector
	 */
	public Vec4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * @return This vector normalized
	 */
	public Vec4 normalize() {
		float length = getLength();
		return new Vec4(x / length, y / length, z / length, w / length);
	}

	/**
	 * @param vector - Vector to do dot product with
	 * @return This vector (dot) vector
	 */
	public float dot(final Vec4 vector) {
		return x * vector.x + y * vector.y + z * vector.z + w * vector.w;
	}
	
	/**
	 * @param vector - Vector to do cross product with
	 * @return this vector (cross) vector
	 */
	public Vec4 cross(final Vec4 vector) {
		float crossX = y * vector.z - z * vector.y;
		float crossY = z * vector.x - x * vector.z;
		float crossZ = x * vector.y - y * vector.x;
		
		return new Vec4(crossX, crossY, crossZ, w);
	}

	/**
	 * @param vector - The vector to add to vector.
	 * @return This vector + vector
	 */
	public Vec4 add(final Vec4 vector) {
		return new Vec4(x + vector.x, y + vector.y, z + vector.z, w + vector.w);
	}

	/**
	 * @param scalar - The scalar to add to vector.
	 * @return This vector + scalar
	 */
	public Vec4 add(final float scalar) {
		return new Vec4(x + scalar, y + scalar, z + scalar, w + scalar);
	}
	
	/**
	 * @param vector - The vector to subtract from vector.
	 * @return This vector - vector
	 */
	public Vec4 sub(final Vec4 vector) { 
		return new Vec4(x - vector.x, y - vector.y, z - vector.z, w - vector.w);
	}

	/**
	 * @param scalar - The scalar to subtract from vector.
	 * @return This vector - scalar
	 */
	public Vec4 sub(final float scalar) {
		return new Vec4(x - scalar, y - scalar, z - scalar, w - scalar);
	}

	/**
	 * @param vector - The vector to divide with vector.
	 * @return This vector * vector
	 */
	public Vec4 div(final Vec4 vector) {
		return new Vec4(x / vector.x, y / vector.y, z / vector.z, w / vector.w);
	}
	
	/**
	 * @param scalar - The scalar to divide with vector.
	 * @return This vector / scalar
	 */
	public Vec4 div(final float scalar) {
		return new Vec4(x / scalar, y / scalar, z / scalar, w / scalar);
	}

	/**
	 * @param vector - The vector to multiply with vector.
	 * @return This vector * vector
	 */
	public Vec4 mul(final Vec4 vector) { 
		return new Vec4(x * vector.x, y * vector.y, z * vector.z, w * vector.w);
	}

	/**
	 * @param scalar - The scalar to multiply with vector.
	 * @return This vector * scalar
	 */
	public Vec4 mul(final float scalar) {
		return new Vec4(x * scalar, y * scalar, z * scalar, w * scalar);
	}

	/**
	 * @return Length of this 3D vector
	 */
	public float getLength() { 
		return (float) Math.sqrt(getLengthSqr());
	}

	/**
	 * @return Length of this 3D vector squared
	 */
	public float getLengthSqr() {
		return x * x + y * y + z * z + w * w;
	}
	
	/**
	 * @param vector - Vector to compare with.
	 * @return True if this vector is equal to vector
	 */
	public boolean compare(Vec4 vector){
		if(x == vector.getX() && y == vector.getY() && z == vector.getZ() && w == vector.getW())
			return true;
		
		return false;
	}
}
