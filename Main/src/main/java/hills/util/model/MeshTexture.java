package hills.util.model;

import hills.util.loader.TextureLoader;
import hills.util.shader.SamplerUniform;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * @author Anton
 */
public class MeshTexture {

	public static final int MAP_AMOUNT = 6;
	/**
	 * Diffuse texture handle
	 */
	private final int diffuse;

	/**
	 * Normal texture handle
	 */
	private final int normal;

	/**
	 * Ambient occlusion texture handle
	 */
	private final int occlusion;

	/**
	 * Opacity texture handle
	 */
	private final int opacity;

	/**
	 * Specular and gloss texture handle
	 */
	private final int specularGloss;

	/**
	 * Emissive texture handle
	 */
	private final int emissive;

	public MeshTexture(String diffuse, String normal, String occlusion,
			String opacity, String specularGloss, String emissive) {
		this.diffuse = diffuse == null ? -1 : TextureLoader
				.loadTexture(diffuse);
		this.normal = normal == null ? -1 : TextureLoader.loadTexture(normal);
		this.occlusion = occlusion == null ? -1 : TextureLoader
				.loadTexture(occlusion);
		this.opacity = opacity == null ? -1 : TextureLoader
				.loadTexture(opacity);
		this.specularGloss = specularGloss == null ? -1 : TextureLoader
				.loadTexture(specularGloss);
		this.emissive = emissive == null ? -1 : TextureLoader
				.loadTexture(emissive);
	}

	public MeshTexture(String diffuse, String normal, String occlusion,
			String opacity, String specularGloss) {
		this(diffuse, normal, occlusion, opacity, specularGloss, null);
	}

	public MeshTexture(String diffuse, String normal, String occlusion,
			String opacity) {
		this(diffuse, normal, occlusion, opacity, null, null);
	}

	public MeshTexture(String diffuse, String normal, String occlusion) {
		this(diffuse, normal, occlusion, null, null, null);
	}

	public MeshTexture(String diffuse, String normal) {
		this(diffuse, normal, null, null, null, null);
	}

	public MeshTexture(String diffuse) {
		this(diffuse, null, null, null, null, null);
	}

	public MeshTexture(String[] paths) {
		this(paths[0], paths[1], paths[2], paths[3], paths[4], paths[5]);
	}

	/**
	 * Binds textures included in this mesh texture.
	 */
	public void bind() {
		// TODO Bind black texture when no texture exists?

		// Bind diffuse texture
		if (diffuse >= 0) {
			GL13.glActiveTexture(SamplerUniform.DIFFUSE.getTextureSlot());
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, diffuse);
		}

		// Bind normal texture
		if (normal >= 0) {
			GL13.glActiveTexture(SamplerUniform.NORMAL.getTextureSlot());
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, normal);
		}

		// Bind ambient occlusion texture
		if (occlusion >= 0) {
			GL13.glActiveTexture(SamplerUniform.OCCLUSION.getTextureSlot());
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, occlusion);
		}

		// Bind opacity texture
		if (opacity >= 0) {
			GL13.glActiveTexture(SamplerUniform.OPACITY.getTextureSlot());
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, opacity);
		}

		// Bind specular and gloss texture
		if (specularGloss >= 0) {
			GL13.glActiveTexture(SamplerUniform.SPECULAR_GLOSS.getTextureSlot());
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, specularGloss);
		}

		// Bind emissive texture
		if (emissive >= 0) {
			GL13.glActiveTexture(SamplerUniform.EMISSIVE.getTextureSlot());
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, emissive);
		}
	}

	/**
	 * Deletes all textures held by this mesh texture.
	 */
	public void delete() {
		try {
			TextureLoader.freeTexture(diffuse);
			TextureLoader.freeTexture(normal);
			TextureLoader.freeTexture(occlusion);
			TextureLoader.freeTexture(opacity);
			TextureLoader.freeTexture(specularGloss);
			TextureLoader.freeTexture(emissive);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
