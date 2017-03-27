package hills.Anton.engine.math;

import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryUtil;

import lombok.Value;

@Value public class Vec3 implements STD140Formatable {

	/**
	 * The dimensions of this vector (3)
	 */
	public static final int SIZE = 3;
	
	private final float x, y, z;
	
	/**
	 * Will create a new vector with the values of vector and z
	 * @param vector - The vector to copy values from.
	 * @param z - The z value of the vector
	 */
	public Vec3(final Vec2 vector, float z){
		this(vector.getX(), vector.getY(), z);
	}
	
	/**
	 * Will create a new vector with the values of vector
	 * @param vector - The vector to copy values from.
	 */
	public Vec3(final Vec3 vector) {
		this(vector.getX(), vector.getY(), vector.getZ());
	}

	/**
	 * Will create a new vector with the values from vector
	 * @param vector - The float array to copy values from.
	 */
	public Vec3(final float[] vector){
		x = vector[0];
		y = vector[1];
		z = vector[2];
	}
	
	/**
	 * Will create a new vector with the values x, y, z
	 * @param x - The x value of the new vector
	 * @param y - The y value of the new vector
	 * @param z - The z value of the new vector
	 */
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @return this vector normalized
	 */
	public Vec3 normalize() {
		float length = getLength();
		return new Vec3(x / length, y / length, z / length);
	}

	/**
	 * @param vector - Vector to do dot product with
	 * @return this vector (dot) vector
	 */
	public float dot(final Vec3 vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}
	
	/**
	 * @param vector - Vector to do cross product with
	 * @return this vector (cross) vector
	 */
	public Vec3 cross(final Vec3 vector) {
		float crossX = y * vector.z - z * vector.y;
		float crossY = z * vector.x - x * vector.z;
		float crossZ = x * vector.y - y * vector.x;
		
		return new Vec3(crossX, crossY, crossZ);
	}

	/**
	 * @param vector - The vector to add to vector.
	 * @return This vector + vector
	 */
	public Vec3 add(final Vec3 vector) { // Add 'vector' and this
		return new Vec3(x + vector.x, y + vector.y, z + vector.z);
	}
	
	/**
	 * @param scalar - The scalar to add to vector.
	 * @return This vector + scalar
	 */
	public Vec3 add(final float scalar) { // Add scalar and this
		return new Vec3(x + scalar, y + scalar, z + scalar);
	}
	
	/**
	 * @param vector - The vector to subtract from vector.
	 * @return This vector - vector
	 */
	public Vec3 sub(final Vec3 vector) { // Subtract this and 'vector' 
		return new Vec3(x - vector.x, y - vector.y, z - vector.z);
	}

	/**
	 * @param scalar - The scalar to subtract from vector.
	 * @return This vector - scalar
	 */
	public Vec3 sub(final float scalar) { // Subtract this and scalar
		return new Vec3(x - scalar, y - scalar, z - scalar);
	}

	/**
	 * @param vector - The vector to divide with vector.
	 * @return This vector * vector
	 */
	public Vec3 div(final Vec3 vector) { // Divide 'vector' and this
		return new Vec3(x / vector.x, y / vector.y, z / vector.z);
	}

	/**
	 * @param scalar - The scalar to divide with vector.
	 * @return This vector / scalar
	 */
	public Vec3 div(final float scalar) { // Divide scalar and this
		return new Vec3(x / scalar, y / scalar, z / scalar);
	}

	/**
	 * @param vector - The vector to multiply with vector.
	 * @return This vector * vector
	 */
	public Vec3 mul(final Vec3 vector) { // Multiply 'vector' and this
		return new Vec3(x * vector.x, y * vector.y, z * vector.z);
	}

	/**
	 * @param scalar - The scalar to multiply with vector.
	 * @return This vector * scalar
	 */
	public Vec3 mul(final float scalar) { // Multiply scalar and this
		return new Vec3(x * scalar, y * scalar, z * scalar);
	}

	/**
	 * @return Length of this 3D vector
	 */
	public float getLength() { // Get length of this
		return (float) Math.sqrt(getLengthSqr());
	}

	/**
	 * @return Length of this 3D vector squared
	 */
	public float getLengthSqr() { // Get length of this squared
		return x * x + y * y + z * z;
	}
	
	/**
	 * @param vector - Vector to compare with.
	 * @return True if this vector is equal to vector
	 */
	public boolean compare(Vec3 vector){
		if(x == vector.x && y == vector.y && z == vector.z)
			return true;
		
		return false;
	}

	/**
	 * @return Byte array of this vectors data. (x, y, z)
	 */
	public byte[] getData(){
		ByteBuffer bytes = MemoryUtil.memAlloc(Float.BYTES * SIZE);
		bytes.putFloat(x);
		bytes.putFloat(y);
		bytes.putFloat(z);
		bytes.flip();
		
		byte[] data = new byte[Float.BYTES * SIZE];
		for(int i = 0; i < data.length; i++)
			data[i] = bytes.get();
		
		MemoryUtil.memFree(bytes);
		
		return data;
	}
	
	@Override
	public byte[] get140Data(){
		ByteBuffer bytes = MemoryUtil.memAlloc(Float.BYTES * STD140Formatable.STD140_FLOATS);
		bytes.putFloat(x);
		bytes.putFloat(y);
		bytes.putFloat(z);
		bytes.putFloat(0.0f);
		bytes.flip();
		
		byte[] data = new byte[Float.BYTES * STD140Formatable.STD140_FLOATS];
		for(int i = 0; i < data.length; i++)
			data[i] = bytes.get();
		
		MemoryUtil.memFree(bytes);
		
		return data;
	}
	
}
