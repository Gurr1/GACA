package hills.util.texturemap;

import hills.util.loader.TextureLoader;
import hills.util.shader.SamplerUniform;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * @author Anton
 */
public class CubeMap {
	
	public static final int FACES = 6;
	private static int LOADED_CUBEMAPS = 0;
	
	private final int handle;
	
	/**
	 * Create a new Cube map from the six faces defined by imagePaths.
	 * @param imagePaths - Paths to the six cube map face images.<br>
	 * Order of faces bound:<br>
	 *     - POS-X<br>
	 *     - NEG-X<br>
	 *     - POS-Y<br>
	 *     - NEG-Y<br>
	 *     - POS-Z<br>
	 *     - NEG-Z<br>
	 * @param flip - Flip face images.
	 */
	public CubeMap(String[] imagePaths, boolean flip){
		if(imagePaths.length != FACES)
			throw new IllegalArgumentException("A cube map has exactly " + FACES + " faces. Passed image paths: " + imagePaths.length);
		
		// Create a name for the cube map for later freeing.
		String name = imagePaths[0] + LOADED_CUBEMAPS++;
		
		// Generate a new handle (id) and load the cube map with its face images 
		handle = TextureLoader.loadCubeMapTexture(imagePaths, name, flip);
	}
	
	/**
	 * Create a new Cube map from the six faces defined by the input paths.
	 * @param posXImagePath - cube POS-X image. 
	 * @param negXImagePath - cube NEG-X image.
	 * @param posYImagePath - cube POS-Y image.
	 * @param negYImagePath - cube NEG-Y image.
	 * @param posZImagePath - cube POS-Z image.
	 * @param negZImagePath - cube NEG-Z image.
	 */
	public CubeMap(String posXImagePath, String negXImagePath, String posYImagePath,
				   String negYImagePath, String posZImagePath, String negZImagePath, boolean flip){
		this(new String[]{posXImagePath, negXImagePath, posYImagePath, negYImagePath, posZImagePath, negZImagePath}, flip);
	}
	
	/**
	 * Bind the cube map.
	 */
	public void bind(){
		GL13.glActiveTexture(SamplerUniform.CUBE_MAP.getTextureSlot());
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, handle);
	}
	
	/**
	 * Deletes texture.
	 */
	public void delete(){
		try {
			TextureLoader.freeTexture(handle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
