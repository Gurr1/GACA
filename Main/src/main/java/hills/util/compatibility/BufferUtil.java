package hills.util.compatibility;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public final class BufferUtil {

	private BufferUtil() {
	} // Private constructor no instances

	/**
	 * Store integer array in an integer buffer.
	 * 
	 * @param data
	 *            - Integer array to store.
	 * @return New integer buffer filled with data.
	 */
	public static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/**
	 * Store float array in a float buffer.
	 * 
	 * @param data
	 *            - Float array to store.
	 * @return New float buffer filled with data.
	 */
	public static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);// FloatBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
