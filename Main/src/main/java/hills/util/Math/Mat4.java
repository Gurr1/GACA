package hills.util.Math;

import java.nio.ByteBuffer;

import lombok.val;

//TODO Make mutable?
@val
public class Mat4 implements STD140Formatable {

	/**
	 * Width of matrix (4 * 4)
	 */
	public static final int WIDTH = 4;

	/**
	 * Height of matrix (4 * 4)
	 */
	public static final int HEIGHT = 4;

	/**
	 * Size of matrix (4 * 4)
	 */
	public static final int SIZE = WIDTH * HEIGHT;

	/**
	 * Float array that holds all the values of the matrix.<br>
	 * <br>
	 * Matrix Column major order<br>
	 * | x, x, x, t | Row 1<br>
	 * | y, y, y, t | Row 2<br>
	 * | z, z, z, t | Row 3<br>
	 * | 0, 0, 0, 1 | Row 4<br>
	 * <br>
	 * | m[0], m[4], m[8], m[12] | Row 1<br>
	 * | m[1], m[5], m[9], m[13] | Row 2<br>
	 * | m[2], m[6], m[10], m[14] | Row 3<br>
	 * | m[3], m[7], m[11], m[15] | Row 4<br>
	 */
	private float[] m = new float[SIZE];

	/**
	 * Creates a new empty matrix (All values of the matrix are 0).<br>
	 * If an identity matrix is desirable, use the static method
	 * Mat4.identity().
	 */
	public Mat4() {
	}

	/**
	 * Creates a new matrix with its values set to the float array (matrix).
	 * 
	 * @param matrix
	 *            - Values of the new matrix.
	 */
	public Mat4(float[] matrix) {
		for (int i = 0; i < SIZE; i++)
			m[i] = matrix[i];
	}

	/**
	 * Creates a new matrix with its values copied from the passed in matrix.
	 * 
	 * @param matrix
	 *            - Matrix to copy values from
	 */
	public Mat4(final Mat4 matrix) {
		for (int i = 0; i < SIZE; i++)
			m[i] = matrix.m[i];
	}

	/**
	 * @param vector
	 *            - Vector to multiply with matrix. W component of vector is set
	 *            to 0.0.
	 * @return matrix * vector
	 */
	public Vec3 mul(Vec3 vector) {
		Vec4 v = mul(new Vec4(vector, 0.0f));
		return new Vec3(v.getX(), v.getY(), v.getZ());
	}

	/**
	 * @param vector
	 *            - Vector to multiply with matrix.
	 * @return matrix * vector
	 */
	public Vec4 mul(Vec4 vector) {
		float[] vec = new float[Vec4.SIZE];
		for (int i = 0; i < vec.length; i++)
			vec[i] = m[i] * vector.getX() + m[i + 4] * vector.getY() + m[i + 8]
					* vector.getZ() + m[i + 12] * vector.getW();

		return new Vec4(vec);
	}

	/**
	 * @param matrix
	 *            - Matrix to multiply with this matrix
	 * @return This matrix * matrix
	 */
	public Mat4 mul(Mat4 matrix) {
		float[] mat = new float[SIZE];
		for (int x = 0; x < WIDTH; x++)
			for (int y = 0; y < HEIGHT; y++)
				mat[x + y * 4] = m[x] * matrix.m[y * 4] + m[x + 4]
						* matrix.m[y * 4 + 1] + m[x + 8] * matrix.m[y * 4 + 2]
						+ m[x + 12] * matrix.m[y * 4 + 3];

		return new Mat4(mat);
	}

	/**
	 * @param matrix
	 *            - Matrix values to multiply with this matrix. OBS! Must have
	 *            length 16.
	 * @return This matrix * matrix
	 */
	public Mat4 mul(float[] matrix) {
		float[] mat = new float[SIZE];
		for (int x = 0; x < WIDTH; x++)
			for (int y = 0; y < HEIGHT; y++)
				mat[x + y * 4] = m[x] * matrix[y * 4] + m[x + 4]
						* matrix[y * 4 + 1] + m[x + 8] * matrix[y * 4 + 2]
						+ m[x + 12] * matrix[y * 4 + 3];

		return new Mat4(mat);
	}

	/**
	 * Multiply this matrix with a scalar.
	 * 
	 * @param scalar
	 *            - Scalar to multiply matrix with
	 * @return A new matrix equal to this matrix * scalar.
	 */
	public Mat4 mul(float scalar) {
		float[] result = new float[SIZE];
		for (int i = 0; i < result.length; i++)
			result[i] = m[i] * scalar;

		return new Mat4(result);
	}

	/**
	 * Add a matrix to this matrix.
	 * 
	 * @param matrix
	 *            - The matrix to add with this matrix.
	 * @return The sum of this matrix + matrix
	 */
	public Mat4 add(Mat4 matrix) {
		float[] matrixValues = matrix.getValues();

		float[] result = new float[SIZE];
		for (int i = 0; i < result.length; i++)
			result[i] = m[i] + matrixValues[i];

		return new Mat4(result);
	}

	/**
	 * @return The determinant of this matrix
	 */
	public float determinant() {
		float kpol = m[10] * m[15] - m[11] * m[14];
		float jpnl = m[6] * m[15] - m[7] * m[14];
		float jonk = m[6] * m[11] - m[7] * m[14];
		float ipml = m[2] * m[15] - m[3] * m[14];
		float iomk = m[2] * m[11] - m[3] * m[10];
		float inmj = m[2] * m[7] - m[3] * m[6];

		return m[0] * (m[5] * kpol - m[9] * jpnl + m[13] * jonk) - m[4]
				* (m[1] * kpol - m[9] * ipml + m[13] * iomk) + m[8]
				* (m[1] * jpnl - m[5] * ipml + m[13] * inmj) - m[12]
				* (m[1] * jonk - m[5] * iomk + m[9] * inmj);
	}

	/**
	 * @return The inverse of this matrix. OBS! Will return this Mat4 if no
	 *         inverse exists!
	 */
	public Mat4 getInverse() {
		float[] inv = new float[SIZE];
		float det;

		inv[0] = m[5] * m[10] * m[15] - m[5] * m[11] * m[14] - m[9] * m[6]
				* m[15] + m[9] * m[7] * m[14] + m[13] * m[6] * m[11] - m[13]
				* m[7] * m[10];

		inv[4] = -m[4] * m[10] * m[15] + m[4] * m[11] * m[14] + m[8] * m[6]
				* m[15] - m[8] * m[7] * m[14] - m[12] * m[6] * m[11] + m[12]
				* m[7] * m[10];

		inv[8] = m[4] * m[9] * m[15] - m[4] * m[11] * m[13] - m[8] * m[5]
				* m[15] + m[8] * m[7] * m[13] + m[12] * m[5] * m[11] - m[12]
				* m[7] * m[9];

		inv[12] = -m[4] * m[9] * m[14] + m[4] * m[10] * m[13] + m[8] * m[5]
				* m[14] - m[8] * m[6] * m[13] - m[12] * m[5] * m[10] + m[12]
				* m[6] * m[9];

		inv[1] = -m[1] * m[10] * m[15] + m[1] * m[11] * m[14] + m[9] * m[2]
				* m[15] - m[9] * m[3] * m[14] - m[13] * m[2] * m[11] + m[13]
				* m[3] * m[10];

		inv[5] = m[0] * m[10] * m[15] - m[0] * m[11] * m[14] - m[8] * m[2]
				* m[15] + m[8] * m[3] * m[14] + m[12] * m[2] * m[11] - m[12]
				* m[3] * m[10];

		inv[9] = -m[0] * m[9] * m[15] + m[0] * m[11] * m[13] + m[8] * m[1]
				* m[15] - m[8] * m[3] * m[13] - m[12] * m[1] * m[11] + m[12]
				* m[3] * m[9];

		inv[13] = m[0] * m[9] * m[14] - m[0] * m[10] * m[13] - m[8] * m[1]
				* m[14] + m[8] * m[2] * m[13] + m[12] * m[1] * m[10] - m[12]
				* m[2] * m[9];

		inv[2] = m[1] * m[6] * m[15] - m[1] * m[7] * m[14] - m[5] * m[2]
				* m[15] + m[5] * m[3] * m[14] + m[13] * m[2] * m[7] - m[13]
				* m[3] * m[6];

		inv[6] = -m[0] * m[6] * m[15] + m[0] * m[7] * m[14] + m[4] * m[2]
				* m[15] - m[4] * m[3] * m[14] - m[12] * m[2] * m[7] + m[12]
				* m[3] * m[6];

		inv[10] = m[0] * m[5] * m[15] - m[0] * m[7] * m[13] - m[4] * m[1]
				* m[15] + m[4] * m[3] * m[13] + m[12] * m[1] * m[7] - m[12]
				* m[3] * m[5];

		inv[14] = -m[0] * m[5] * m[14] + m[0] * m[6] * m[13] + m[4] * m[1]
				* m[14] - m[4] * m[2] * m[13] - m[12] * m[1] * m[6] + m[12]
				* m[2] * m[5];

		inv[3] = -m[1] * m[6] * m[11] + m[1] * m[7] * m[10] + m[5] * m[2]
				* m[11] - m[5] * m[3] * m[10] - m[9] * m[2] * m[7] + m[9]
				* m[3] * m[6];

		inv[7] = m[0] * m[6] * m[11] - m[0] * m[7] * m[10] - m[4] * m[2]
				* m[11] + m[4] * m[3] * m[10] + m[8] * m[2] * m[7] - m[8]
				* m[3] * m[6];

		inv[11] = -m[0] * m[5] * m[11] + m[0] * m[7] * m[9] + m[4] * m[1]
				* m[11] - m[4] * m[3] * m[9] - m[8] * m[1] * m[7] + m[8] * m[3]
				* m[5];

		inv[15] = m[0] * m[5] * m[10] - m[0] * m[6] * m[9] - m[4] * m[1]
				* m[10] + m[4] * m[2] * m[9] + m[8] * m[1] * m[6] - m[8] * m[2]
				* m[5];

		det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];

		if (det == 0) {
			System.err.println("No inverse of current matrix!");
			return this;
		}

		det = 1.0f / det;

		for (int i = 0; i < inv.length; i++)
			inv[i] *= det;

		return new Mat4(inv);
	}

	/**
	 * @return The transpose of this matrix.
	 */
	public Mat4 getTranspose() {
		float[] mat = new float[SIZE];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				mat[j + i * 4] = m[i + j * 4];

		return new Mat4(mat);
	}

	/**
	 * @param translation
	 *            - Translation vector to add to matrix.
	 * @return A new matrix with the translation vector added on to the
	 *         translation factors of the matrix.
	 */
	public Mat4 translate(Vec3 translation) {
		return translate(translation.getX(), translation.getY(),
				translation.getZ());
	}

	/**
	 * @param x
	 *            - Translation x factor to add to matrix.
	 * @param y
	 *            - Translation y factor to add to matrix.
	 * @param z
	 *            - Translation z factor to add to matrix.
	 * @return A new matrix with the translation factors added on to the
	 *         translation factors of the matrix.
	 */
	public Mat4 translate(float x, float y, float z) {
		float[] tm = new float[m.length];
		for (int i = 0; i < tm.length; i++)
			tm[i] = m[i];

		tm[12] += x;
		tm[13] += y;
		tm[14] += z;

		return new Mat4(tm);
	}

	/**
	 * @param translation
	 *            - Translation vector to set in matrix.
	 * @return A new matrix with the translation factors of the matrix set to
	 *         the translation vector.
	 */
	public Mat4 setTranslation(Vec3 translation) {
		return setTranslation(translation.getX(), translation.getY(),
				translation.getZ());
	}

	/**
	 * @param x
	 *            - Translation x factor to set in matrix.
	 * @param y
	 *            - Translation y factor to set in matrix.
	 * @param z
	 *            - Translation z factor to set in matrix.
	 * @return A new matrix with the translation factors of the matrix set to
	 *         the translation factors.
	 */
	public Mat4 setTranslation(float x, float y, float z) {
		float[] tm = new float[m.length];
		for (int i = 0; i < tm.length - 4; i++)
			tm[i] = m[i];

		tm[12] = x;
		tm[13] = y;
		tm[14] = z;
		tm[15] = m[15];

		return new Mat4(tm);
	}

	/**
	 * @param scale
	 *            - Scale vector to multiply to matrix.
	 * @return A new matrix with the scale multiplied on to the scale factors of
	 *         the matrix.
	 */
	public Mat4 scale(Vec3 scale) {
		return scale(scale.getX(), scale.getY(), scale.getZ());
	}

	/**
	 * @param x
	 *            - Scale x factor to multiply to matrix.
	 * @param y
	 *            - Scale y factor to multiply to matrix.
	 * @param z
	 *            - Scale z factor to multiply to matrix.
	 * @return A new matrix with the scale factors multiplied on to the scale
	 *         factors of the matrix.
	 */
	public Mat4 scale(float x, float y, float z) {
		float[] sm = new float[m.length];
		for (int i = 0; i < sm.length; i++)
			sm[i] = m[i];

		sm[0] *= x;
		sm[5] *= y;
		sm[10] *= z;

		return new Mat4(sm);
	}

	/**
	 * @param scale
	 *            - Scale vector to set in matrix.
	 * @return A new matrix with the scale factors of the matrix set to the
	 *         scale vector.
	 */
	public Mat4 setScale(Vec3 scale) {
		return setScale(scale.getX(), scale.getY(), scale.getZ());
	}

	/**
	 * @param x
	 *            - Scale x factor to set in matrix.
	 * @param y
	 *            - Scale y factor to set in matrix.
	 * @param z
	 *            - Scale z factor to set in matrix.
	 * @return A new matrix with the scale factors of the matrix set to the
	 *         scale factors.
	 */
	public Mat4 setScale(float x, float y, float z) {
		float[] sm = new float[m.length];
		for (int i = 0; i < sm.length; i++)
			sm[i] = m[i];

		sm[0] = x;
		sm[5] = y;
		sm[10] = z;

		return new Mat4(sm);
	}

	/**
	 * @param degrees
	 *            - Degrees to rotate around x-axis (CCW).
	 * @return A new matrix rotated degrees degrees around the x-axis.<br>
	 *         (This matrix) * (x rotation matrix)
	 */
	public Mat4 rotateX(float degrees) {
		float[] rm = new float[m.length];
		rm[0] = 1.0f;
		rm[15] = 1.0f;

		rm[5] = (float) Math.cos(Math.toRadians(degrees));
		rm[9] = (float) -Math.sin(Math.toRadians(degrees));
		rm[6] = (float) Math.sin(Math.toRadians(degrees));
		rm[10] = (float) Math.cos(Math.toRadians(degrees));

		return this.mul(rm);
	}

	/**
	 * @param degrees
	 *            - Degrees to rotate around y-axis (CCW).
	 * @return A new matrix rotated degrees degrees around the y-axis.<br>
	 *         (This matrix) * (y rotation matrix)
	 */
	public Mat4 rotateY(float degrees) {
		float[] rm = new float[m.length];
		rm[5] = 1.0f;
		rm[15] = 1.0f;

		rm[0] = (float) Math.cos(Math.toRadians(degrees));
		rm[8] = (float) Math.sin(Math.toRadians(degrees));
		rm[2] = (float) -Math.sin(Math.toRadians(degrees));
		rm[10] = (float) Math.cos(Math.toRadians(degrees));

		return this.mul(rm);
	}

	/**
	 * @param degrees
	 *            - Degrees to rotate around z-axis (CCW).
	 * @return A new matrix rotated degrees degrees around the z-axis.<br>
	 *         (This matrix) * (z rotation matrix)
	 */
	public Mat4 rotateZ(float degrees) {
		float[] rm = new float[m.length];
		rm[10] = 1.0f;
		rm[15] = 1.0f;

		rm[0] = (float) Math.cos(Math.toRadians(degrees));
		rm[4] = (float) -Math.sin(Math.toRadians(degrees));
		rm[1] = (float) Math.sin(Math.toRadians(degrees));
		rm[5] = (float) Math.cos(Math.toRadians(degrees));

		return this.mul(rm);
	}

	/**
	 * @param rotation
	 *            - Degrees to rotate around each axis (x, y, z) (CCW).
	 * @return A new matrix that has first been rotated around the x-axis, then
	 *         the y-axis and last the z-axis.
	 */
	public Mat4 rotate(Vec3 rotation) {
		return rotate(rotation.getX(), rotation.getY(), rotation.getZ());
	}

	/**
	 * @param xDegrees
	 *            - Degrees to rotate around the x-axis (CCW).
	 * @param yDegrees
	 *            - Degrees to rotate around the y-axis (CCW).
	 * @param zDegrees
	 *            - Degrees to rotate around the z-axis (CCW).
	 * @return A new matrix that has first been rotated around the x-axis, then
	 *         the y-axis and last the z-axis.
	 */
	public Mat4 rotate(float xDegrees, float yDegrees, float zDegrees) {
		return rotateZ(zDegrees).rotateY(yDegrees).rotateX(xDegrees);
	}

	/**
	 * @return A Vec3 with it's values set to the translation factors of this
	 *         matrix.
	 */
	public Vec3 getTranslation() {
		return new Vec3(m[12], m[13], m[14]);
	}

	/**
	 * @return The values of this matrix in column-major order.<br>
	 *         Changes to this array will not effect the matrix values.
	 */
	public float[] getValues() {
		float[] rm = new float[m.length];
		for (int i = 0; i < m.length; i++)
			rm[i] = m[i];

		return rm;
	}

	/**
	 * @return A new identity matrix.
	 */
	public static Mat4 identity() {
		float[] m = new float[SIZE];

		m[0] = 1.0f;
		m[5] = 1.0f;
		m[10] = 1.0f;
		m[15] = 1.0f;

		return new Mat4(m);
	}

	/**
	 * @param x
	 *            - x scale factor.
	 * @param y
	 *            - y scale factor.
	 * @param z
	 *            - z scale factor.
	 * @return A new matrix scale matrix, an identity matrix with it's scale
	 *         factors set to x, y, z.
	 */
	public static Mat4 scaleMatrix(float x, float y, float z) {
		float[] m = new float[SIZE];

		m[0] = x;
		m[5] = y;
		m[10] = z;
		m[15] = 1.0f;

		return new Mat4(m);
	}

	/**
	 * @param scale
	 *            - Vector with scale components x, y, z.
	 * @return A new matrix scale matrix, an identity matrix with it's scale
	 *         factors set to x, y, z.
	 */
	public static Mat4 scaleMatrix(Vec3 scale) {
		return Mat4.scaleMatrix(scale);
	}

	/**
	 * @param x
	 *            - x translation component.
	 * @param y
	 *            - y translation component.
	 * @param z
	 *            - z translation component.
	 * @return A new translation matrix, an identity matrix with it's
	 *         translation components set to x, y, z
	 */
	public static Mat4 translationMatrix(float x, float y, float z) {
		float[] m = new float[SIZE];

		m[0] = 1.0f;
		m[5] = 1.0f;
		m[10] = 1.0f;
		m[15] = 1.0f;

		m[12] = x;
		m[13] = y;
		m[14] = z;

		return new Mat4(m);
	}

	/**
	 * @param translation
	 *            - Vector with translation components x, y, z.
	 * @return A new translation matrix, an identity matrix with it's
	 *         translation components set to x, y, z
	 */
	public static Mat4 translationMatrix(Vec3 translation) {
		return Mat4.translationMatrix(translation.getX(), translation.getY(),
				translation.getZ());
	}

	/**
	 * @param near
	 *            - Distance to near plane.
	 * @param far
	 *            - Distance to far plane.
	 * @param aspect
	 *            - Aspect ratio of display area.
	 * @param FOV
	 *            - Vertical field of view in degrees.
	 * @return A new perspective matrix.
	 */
	public static Mat4 perspective(float near, float far, float aspect,
			float FOV) {
		float range = (float) (Math.tan(Math.toRadians(FOV * 0.5f)) * near);

		float Sx = (2.0f * near) / (range * aspect + range * aspect);
		float Sy = near / range;
		float Sz = -(far + near) / (far - near);
		float Pz = -(2.0f * far * near) / (far - near);

		float[] pm = new float[SIZE];
		pm[0] = Sx;
		pm[5] = Sy;
		pm[10] = Sz;
		pm[14] = Pz;
		pm[11] = -1.0f;

		return new Mat4(pm);
	}

	/**
	 * Construct a transposed basis matrix, which if forward, up and right are
	 * normalized then inverted,<br>
	 * that can be used to transform into the basis of forward, up and right.
	 * 
	 * @param pos
	 *            - Basis translation
	 * @param forward
	 *            - z-component of basis.
	 * @param up
	 *            - y-component of basis.
	 * @param right
	 *            - x-component of basis.
	 * @param normalize
	 *            - If true will normalize input vectors before matrix
	 *            construction.
	 * @return The transposed basis matrix.
	 */
	public static Mat4 look(Vec3 pos, Vec3 forward, Vec3 up, Vec3 right,
			boolean normalize) {
		if (normalize) {
			forward.normalize();
			up.normalize();
			right.normalize();
		}

		float[] lm = new float[SIZE];

		// Construct a transposed basis matrix (inverse if forward, up & down
		// are normalized)

		// Row 1
		lm[0] = right.getX();
		lm[4] = right.getY();
		lm[8] = right.getZ();

		// Row 2
		lm[1] = up.getX();
		lm[5] = up.getY();
		lm[9] = up.getZ();

		// Row 3
		lm[2] = -forward.getX();
		lm[6] = -forward.getY();
		lm[10] = -forward.getZ();

		// Bottom-right corner
		lm[15] = 1.0f;

		return new Mat4(lm).mul(Mat4.identity().setTranslation(pos.mul(-1)));
	}

	public String toString() {
		String output = "";
		for (int i = 0; i < 4; i++)
			output += "| " + m[i] + ", " + m[i + 4] + ", " + m[i + 8] + ", "
					+ m[i + 12] + " |\n";

		return output;
	}

	@Override
	public void get140Data(ByteBuffer buffer) {
		for (float f : m)
			buffer.putFloat(f);
	}

	@Override
	public int get140DataSize() {
		return STD140Formatable.MATRIX_ALIGNMENT;
	}

}
