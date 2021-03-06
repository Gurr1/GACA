package hills.util.texturemap;

import hills.util.loader.TextureLoader;
import hills.util.shader.SamplerUniform;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

/**
 * @Author Anton Annlöv
 */
public class TerrainTexture {
	
	public static final int MAP_AMOUNT = 2;
	
	/**
	 * Height map texture handle
	 */
	private final int height;
	
	/**
	 * Normal texture handle
	 */
	private final int normal;

	public TerrainTexture(String height, String normal) {
		this.height = height == null ? -1 : TextureLoader.loadTexture(height, false);
		this.normal = normal == null ? -1 : TextureLoader.loadTexture(normal, false);
	}
	
	public TerrainTexture(String diffuse){
		this(diffuse, null);
	}
	
	/**
	 * Binds textures included in this mesh texture.
	 */
	public void bind(){
		// TODO Bind black texture when no texture exists?
		
		// Bind height map texture
		if(height >= 0){
			GL13.glActiveTexture(SamplerUniform.TERRAIN_HEIGHT.getTextureSlot());
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, height);
		}
		
		// Bind normal texture
		if(normal >= 0){
			GL13.glActiveTexture(SamplerUniform.TERRAIN_NORMAL.getTextureSlot());
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, normal);
		}
	}
	
	/**
	 * Deletes all textures held by this mesh texture.
	 */
	public void delete(){
		try {
			TextureLoader.freeTexture(height);
			TextureLoader.freeTexture(normal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

