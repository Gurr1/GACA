package hills.engine.loader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import hills.engine.texturemap.CubeMap;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class TextureLoader {

	public static final String DIRECTORY = "src/main/resources/textures/";

	/**
	 * Mapping of handle loaded from what file.
	 */
	private static Map<String, Integer> loadedTextures = new HashMap<String, Integer>();

	public static int WRAP_S = GL12.GL_CLAMP_TO_EDGE,
			WRAP_T = GL12.GL_CLAMP_TO_EDGE, WRAP_R = GL12.GL_CLAMP_TO_EDGE,
			MAG = GL11.GL_LINEAR, MIN = GL11.GL_LINEAR;

	private TextureLoader() {
	} // Private constructor no instances

	/**
	 * Converts .png file found in TextureLoader.DIRECTORY + path to ByteBuffer.
	 * OBS! The returned bytebuffer must be explicitly freed with
	 * MemoryUtils.memFree(...).
	 * 
	 * @param path
	 *            - Path to .png file in TextureLoader.DIRECTORY.
	 * @param width
	 *            - IntBuffer whose index 0 will be equal to image width in
	 *            pixels
	 * @param height
	 *            - IntBuffer whose index 0 will be equal to image height in
	 *            pixels
	 * @param flip
	 *            - If image is to be flipped along x-axis making bottom left
	 *            corner 0, 0.
	 * @return ByteBuffer containing image data in RGBA format. OBS! Will return
	 *         null if conversion fails.
	 */
	public static ByteBuffer PNGToByteBuffer(String path, IntBuffer width, IntBuffer height, boolean flip) {
		System.out.println(DIRECTORY + path);
		try (InputStream in = new FileInputStream((DIRECTORY + path))) {
			PNGDecoder decoder = new PNGDecoder(in);
			int imageWidth = decoder.getWidth();
			int imageHeight = decoder.getHeight();

			// Check if image size is power of 2
			if ((imageWidth & (imageWidth - 1)) != 0 || (imageHeight & (imageHeight - 1)) != 0)
				System.err.println("Warning! Image (" + path + ") size not power of 2!");

			// Add image data to buffer
			ByteBuffer buffer = MemoryUtil.memAlloc(4 * imageWidth * imageHeight);
			decoder.decode(buffer, imageWidth * 4, Format.RGBA);
			buffer.flip();

			// Set width & height equal to image width & height in pixels
			if (width != null && height != null) {
				width.put(0, imageWidth);
				height.put(0, imageHeight);
			}

			// If image should not be flipped along x-axis return image byte
			// buffer as is
			if (!flip)
				return buffer;

			// Flip image so that 0, 0 is bottom left corner and return image in
			// form of ByteBuffer
			ByteBuffer flippedBuffer = MemoryUtil.memAlloc(4 * imageWidth
					* imageHeight);
			for (int row = 0; row < imageHeight / 2; row++)
				for (int col = 0; col < imageWidth * 4; col++) {
					flippedBuffer.put(
							col + row * imageWidth * 4,
							buffer.get(col + (imageHeight - 1 - row)
									* imageWidth * 4));
					flippedBuffer.put(col + (imageHeight - 1 - row)
							* imageWidth * 4,
							buffer.get(col + row * imageWidth * 4));
				}
			flippedBuffer.rewind();

			// Free original buffer
			MemoryUtil.memFree(buffer);

			return flippedBuffer;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// If PNG to ByteBuffer conversion fails print error and return null
		System.err.println("Failed to convert \"" + path + "\" to ByteBuffer!");
		return null;
	}

	/**
	 * Converts .png file found in TextureLoader.DIRECTORY + path to ByteBuffer.
	 * 
	 * @param path
	 *            - Path to .png file in TextureLoader.DIRECTORY.
	 * @param flip
	 *            - If image is to be flipped along x-axis making bottom left
	 *            corner 0, 0.
	 * @return ByteBuffer containing image data in RGBA format. OBS! Will return
	 *         null if conversion fails.
	 */
	public static ByteBuffer PNGToByteBuffer(String path, boolean flip) {
		return PNGToByteBuffer(path, null, null, flip);
	}

	/**
	 * Loads a texture from the path specified.<br>
	 * Texture parameters for wrapping around s and t, as well as parameters for<br>
	 * resize method (mag and min) will be set according to
	 * TextureLoader.WRAP_S, TextureLoader.WRAP_T,<br>
	 * TextureLoader.MAG and TextureLoader.MIN.
	 * 
	 * @param path
	 *            - Path to image file starting from TextureLoader.DIRECTORY.
	 * @return The handle of the texture.
	 */
	public static int loadTexture(String path, boolean flip) {
		IntBuffer w = MemoryUtil.memAllocInt(1);
		IntBuffer h = MemoryUtil.memAllocInt(1);
		ByteBuffer textureByteBuffer = PNGToByteBuffer(path, w, h, flip);

		// Load image
		int handle = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w.get(), h.get(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureByteBuffer);

		// Free allocated buffer
		MemoryUtil.memFree(textureByteBuffer);

		// Set parameters
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, WRAP_S);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, WRAP_T);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, MAG);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, MIN);

		// Generate mipmaps
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

		// Free allocated memory
		MemoryUtil.memFree(w);
		MemoryUtil.memFree(h);

		loadedTextures.put(path, handle);
		return handle;
	}

	public static int loadTexture(String path) {
		return loadTexture(path, true);
	}

	/**
	 * Load a cube map from six face images.<br>
	 * Order of faces bound:<br>
	 * - POS-X<br>
	 * - NEG-X<br>
	 * - POS-Y<br>
	 * - NEG-Y<br>
	 * - POS-Z<br>
	 * - NEG-Z<br>
	 * Texture parameters for wrapping around s, t and r, as well as parameters
	 * for<br>
	 * resize method (mag and min) will be set according to
	 * TextureLoader.WRAP_S, TextureLoader.WRAP_T,<br>
	 * TextureLoader.WRAP_R, TextureLoader.MAG and TextureLoader.MIN.
	 * 
	 * @param paths
	 *            - Paths to the 6 images used for the cubes faces.
	 * @param name
	 *            - Name to save the loaded texture under for later guaranteed
	 *            freeing.
	 * @return Cube map handle (id) of the cube map bound.
	 */
	public static int loadCubeMapTexture(String[] paths, String name,
			boolean flip) {
		if (paths.length != CubeMap.FACES)
			throw new IllegalArgumentException("A cube map has exactly "
					+ CubeMap.FACES + " faces. Passed image paths: "
					+ paths.length);

		// Bind cube map
		int handle = GL11.glGenTextures();
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, handle);

		// Go through and add each image to it's respective face
		for (int i = 0; i < paths.length; i++) {
			IntBuffer w = MemoryUtil.memAllocInt(1);
			IntBuffer h = MemoryUtil.memAllocInt(1);
			ByteBuffer textureByteBuffer = PNGToByteBuffer(paths[i], w, h, flip);

			// Load image
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0,
					GL11.GL_RGBA, w.get(), h.get(), 0, GL11.GL_RGBA,
					GL11.GL_UNSIGNED_BYTE, textureByteBuffer);

			// Free allocated buffer
			MemoryUtil.memFree(textureByteBuffer);

			// Free allocated memory
			MemoryUtil.memFree(w);
			MemoryUtil.memFree(h);
		}

		// Set parameters
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S,
				WRAP_S);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T,
				WRAP_T);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R,
				WRAP_R);
		GL11.glTexParameterf(GL13.GL_TEXTURE_CUBE_MAP,
				GL11.GL_TEXTURE_MAG_FILTER, MAG);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,
				GL11.GL_TEXTURE_MIN_FILTER, MIN);

		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);

		loadedTextures.put(name, handle);
		return handle;
	}

	/**
	 * Set texture parameters used when loading.
	 * 
	 * @param WRAP_S
	 *            - GL_TEXTURE_WRAP_S parameter.
	 * @param WRAP_T
	 *            - GL_TEXTURE_WRAP_T parameter.
	 * @param WRAP_R
	 *            - GL_TEXTURE_WRAP_R parameter.
	 * @param MAG
	 *            - GL_TEXTURE_MAG_FILTER parameter.
	 * @param MIN
	 *            - GL_TEXTURE_MIN_FILTER parameter
	 */
	public static void setTexParameters(int WRAP_S, int WRAP_T, int WRAP_R,
			int MAG, int MIN) {
		TextureLoader.WRAP_S = WRAP_S;
		TextureLoader.WRAP_T = WRAP_T;
		TextureLoader.WRAP_R = WRAP_R;
		TextureLoader.MAG = MAG;
		TextureLoader.MIN = MIN;
	}

	/**
	 * @param name
	 *            - Name of texture (path used when loading).
	 * @return Handle of loaded texture if it exists.
	 * @throws Exception
	 *             - If texture isn't loaded.
	 */
	public static int getTexture(String name) throws IllegalArgumentException {
		Integer handle = loadedTextures.get(name);
		if (handle == null)
			throw new IllegalArgumentException("Texture not found!");

		return handle;
	}

	/**
	 * Will delete texture.
	 * 
	 * @param name
	 *            - Name of texture file.
	 */
	public static void freeTexture(String name) throws Exception {
		Integer handle = loadedTextures.remove(name);
		if (handle == null)
			throw new Exception("Texture not found!");

		GL11.glDeleteTextures(handle);
	}

	/**
	 * Will delete texture.
	 * 
	 * @param handle
	 *            - Name of texture file.
	 */
	public static void freeTexture(int handle) throws Exception {
		for (String textureName : loadedTextures.keySet())
			loadedTextures.remove(textureName, handle);

		GL11.glDeleteTextures(handle);
	}

	/**
	 * Remove all textures loaded.
	 */
	public static void cleanUp() {
		for (String textureName : loadedTextures.keySet())
			GL11.glDeleteTextures(loadedTextures.get(textureName));
		loadedTextures.clear();

		System.out.println("TextureLoader cleaned up!");
	}
}
