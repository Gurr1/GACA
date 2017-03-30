package hills.engine.texturemap;

import hills.engine.loader.TextureLoader;
import hills.engine.renderer.shader.SamplerUniform;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

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
	
	public TerrainTexture(String height, String normal){
		this.height = height == null ? -1 : TextureLoader.loadTexture(height);
		this.normal = normal == null ? -1 : TextureLoader.loadTexture(normal);
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

