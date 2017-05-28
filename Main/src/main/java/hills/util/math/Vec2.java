package hills.util.math;

import lombok.Value;

import java.nio.ByteBuffer;

/**
 * @author Anton
 */
@Value
public class Vec2 implements STD140Formatable {

	/**
	 * The dimensions of this vector (2)
	 */
	public static final int SIZE = 2;

	private final float x, y;

	/**
	 * Will create a new vector with the values of vector
	 * 
	 * @param vector
	 *            - The vector to copy values from.
	 */
	public Vec2(final Vec2 vector) {
		this(vector.getX(), vector.getY());
	}

	/**
	 * Will create a new vector with the values from vector
	 * 
	 * @param vector
	 *            - The float array to copy values from.
	 */
	public Vec2(final float[] vector) {
		x = vector[0];
		y = vector[1];
	}

	/**
	 * Will create a new vector with the values x, y
	 * 
	 * @param x
	 *            - The x value of the new vector
	 * @param y
	 *            - The y value of the new vector
	 */
	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return This vector normalized
	 */
	public Vec2 normalize() {
		float length = getLength();
		if(length == 0){
			return new Vec2(0,0);
		}
		return new Vec2(x / length, y / length);
	}

	/**
	 * @param vector
	 *            - Vector to do dot product with
	 * @return This vector (dot) vector
	 */
	public float dot(final Vec2 vector) {
		return x * vector.x + y * vector.y;
	}

	/**
	 * @param vector
	 *            - The vector to add to vector.
	 * @return This vector + vector
	 */
	public Vec2 add(final Vec2 vector) {
		return new Vec2(x + vector.x, y + vector.y);
	}

	/**
	 * @param scalar
	 *            - The scalar to add to vector.
	 * @return This vector + scalar
	 */
	public Vec2 add(final float scalar) {
		return new Vec2(x + scalar, y + scalar);
	}

	/**
	 * @param vector
	 *            - The vector to subtract from vector.
	 * @return This vector - vector
	 */
	public Vec2 sub(final Vec2 vector) {
		return new Vec2(x - vector.x, y - vector.y);
	}

	/**
	 * @param scalar
	 *            - The scalar to subtract from vector.
	 * @return This vector - scalar
	 */
	public Vec2 sub(final float scalar) {
		return new Vec2(x - scalar, y - scalar);
	}

	/**
	 * @param vector
	 *            - The vector to divide with vector.
	 * @return This vector * vector
	 */
	public Vec2 div(final Vec2 vector) {
		return new Vec2(x / vector.x, y / vector.y);
	}

	/**
	 * @param scalar
	 *            - The scalar to divide with vector.
	 * @return This vector / scalar
	 */
	public Vec2 div(final float scalar) {
		return new Vec2(x / scalar, y / scalar);
	}

	/**
	 * @param vector
	 *            - The vector to multiply with vector.
	 * @return This vector * vector
	 */
	public Vec2 mul(final Vec2 vector) {
		return new Vec2(x * vector.x, y * vector.y);
	}

	/**
	 * @param scalar
	 *            - The scalar to multiply with vector.
	 * @return This vector * scalar
	 */
	public Vec2 mul(final float scalar) {
		return new Vec2(x * scalar, y * scalar);
	}

	/**
	 * @return Length of this 2D vector
	 */
	public float getLength() {
		return (float) Math.sqrt(getLengthSqr());
	}

	/**
	 * @return Length of this 2D vector squared
	 */
	public float getLengthSqr() {
		return x * x + y * y;
	}

	/**
	 * @param vector
	 *            - Vector to equals with.
	 * @return True if This vector is equal to vector
	 */
	public boolean equals(Vec2 vector) {
		if (x == vector.x && y == vector.y)
			return true;

		return false;
	}

	@Override
	public void get140Data(ByteBuffer buffer) {
		buffer.putFloat(x);
		buffer.putFloat(y);
	}

	@Override
	public int get140DataSize() {
		return STD140Formatable.VECTOR_2_ALIGNMENT;
	}
}
