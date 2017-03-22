package hills.Anton.engine.math;

import lombok.val;

@val public class Mat4 {

	public static final int WIDTH = 4; // Width of matrix (4 * 4)
	public static final int HEIGHT = 4; // Height of matrix (4 * 4)
	public static final int SIZE = WIDTH * HEIGHT; // Size of matrix (4 * 4)
	
	private float[] m = new float[SIZE]; // Matrix Column major order
	 				   					 /* | x, x, x, t | Row 1
	 				   					 *  | y, y, y, t | Row 2
	 				   					 *  | z, z, z, t | Row 3
	 				   					 *  | 0, 0, 0, 1 | Row 4
	 				   					 *  
	 				   					 *  | m[0], m[4], m[8],  m[12] | Row 1
	 				   					 *  | m[1], m[5], m[9],  m[13] | Row 2
	 				   					 *  | m[2], m[6], m[10], m[14] | Row 3
	 				   					 *  | m[3], m[7], m[11], m[15] | Row 4
	 				   					 */
	
	public Mat4() {
		identity();
	}

	public Mat4(final float[] matrix) {
		//setMatrix(matrix);
	}
	
	public Mat4(final Mat4 matrix){
		//setMatrix(matrix);
	}
	
	/**
	 * Set this matrix to an identity matrix
	 */
	private void identity() {
		for (int i = 0; i < SIZE; i++)
			m[i] = 0.0f;

		m[0] = 1.0f;
		m[5] = 1.0f;
		m[10] = 1.0f;
		m[15] = 1.0f;
	}
	
	/**
	 * @param vector - Vector to multiply with matrix. W component of vector is set to 0.0.
	 * @return matrix * vector
	 */
	public Vec3 mul(final Vec3 vector) { // Multiply this and 'vector'. W component is set to 0.0f
		Vec4 v = mul(new Vec4(vector, 0.0f));
		return new Vec3(v.getX(), v.getY(), v.getZ());
	}

	/**
	 * @param vector - Vector to multiply with matrix.
	 * @return matrix * vector
	 */
	public Vec4 mul(final Vec4 vector) { // Multiply this and 'vector'
		float[] vec = new float[4];
		for (int i = 0; i < vec.length; i++)
			vec[i] = m[i] * vector.getX() + m[i + 4] * vector.getY() + m[i + 8] * vector.getZ() + m[i + 12] * vector.getW();

		return new Vec4(vec);
	}
}
