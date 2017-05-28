package hills.util.texturemap;

import hills.util.loader.TextureLoader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * @Author Anton Annlöv
 */
public class TextureMap2D {

	/**
	 * Handle of the texture
	 */
	private final int handle;

	/**
	 * Texture slot to bind the texture to (Default GL13.GL_TEXTURE0).
	 */
	private int textureSlot = GL13.GL_TEXTURE0;

	/**
	 * 
	 */
	public TextureMap2D(int handle, int textureSlot) {
		this.handle = handle;
		this.textureSlot = textureSlot;
	}

	/**
	 * Create a new Cube map from the six faces defined by the input paths.
	 * 
	 * @param flip
	 *            - Flip face images.
	 */
	public TextureMap2D(String path, int textureSlot, boolean flip) {
		handle = TextureLoader.loadTexture(path, flip);
		this.textureSlot = textureSlot;
	}

	/**
	 * Bind the 2D texture.
	 */
	public void bind() {
		GL13.glActiveTexture(textureSlot);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);
	}

	/**
	 * Deletes texture.
	 */
	public void delete() {
		try {
			TextureLoader.freeTexture(handle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
