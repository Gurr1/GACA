package hills.util.math;

import java.nio.ByteBuffer;

/**
 * @author Anton
 */
public interface STD140Formatable {

	/**
	 * Number of bytes of scalar base alignment.
	 */
	public static final int SCALAR_ALIGNMENT = 4;

	/**
	 * Number of bytes of 2D vector base alignment.
	 */
	public static final int VECTOR_2_ALIGNMENT = 8;

	/**
	 * Number of bytes of 3D vector base alignment.
	 */
	public static final int VECTOR_3_ALIGNMENT = 16;

	/**
	 * Number of bytes of 4D vector base alignment.
	 */
	public static final int VECTOR_4_ALIGNMENT = 16;

	/**
	 * Number of bytes of scalar array / vector array base alignment.
	 */
	public static final int ARRAY_ALIGNMENT = 16;

	/**
	 * Number of bytes of matrix base alignment.
	 */
	public static final int MATRIX_ALIGNMENT = 64;

	/**
	 * Will store this classes data in the input buffer<br>
	 * according to the std140 format. The size of the<br>
	 * buffer needed in bytes can be retrieved from<br>
	 * get140DataSize().<br>
	 */
	public void get140Data(ByteBuffer buffer);

	/**
	 * @return Size (bytes) of this classes data in std140 format.
	 */
	public int get140DataSize();
}
