package hills.view;

import hills.util.loader.ModelLoader;
import hills.util.math.Mat4;
import hills.util.math.Vec2;
import hills.util.math.Vec3;
import hills.util.math.Vertex;
import hills.util.model.MeshData;
import hills.util.shader.ShaderAttribute;
import hills.util.shader.ShaderProgram;
import hills.util.texturemap.CubeMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * @author Anton
 */
public final class SkyBoxRenderer implements IRendererDrawable, ISkyBoxData {
	
	private final ShaderProgram shaderProgram;
	private final MeshData meshData;
	
	private final String EXTENSION = "_blue";
	private CubeMap skyBoxCubeMap;
	
	protected SkyBoxRenderer(){
		shaderProgram = ShaderProgram.SKY_BOX;
		meshData = ModelLoader.load(SKY_BOX_VERTICES, SKY_BOX_INDICES, null, Mat4.identity()).getMeshData();
		
		// Set default sky box
		setSkyBoxCubeMap(new CubeMap("sky_box_pos_x" + EXTENSION + ".png", "sky_box_neg_x" + EXTENSION + ".png", "sky_box_pos_y" + EXTENSION + ".png", "sky_box_neg_y" + EXTENSION + ".png", "sky_box_pos_z" + EXTENSION + ".png", "sky_box_neg_z" + EXTENSION + ".png", false));
	}
	
	public void render(){
		if(skyBoxCubeMap == null)
			return;
		
		// Activate shader program
		shaderProgram.enable();
		
		// Bind mesh VAO
		GL30.glBindVertexArray(meshData.getVao());
		
		// Enable attributes
		GL20.glEnableVertexAttribArray(ShaderAttribute.POSITION.getLocation());	// Position attribute
		
		// Bind cube map texture
		skyBoxCubeMap.bind(); 
		
		// Render sky box
		GL11.glDrawElements(GL11.GL_TRIANGLES, meshData.getIndicesAmount(), GL11.GL_UNSIGNED_INT, 0);
		
		// Disable attributes
		GL20.glDisableVertexAttribArray(ShaderAttribute.POSITION.getLocation()); // Position attribute
		
		// Unbind mesh VAO
		GL30.glBindVertexArray(0);
		
		// Deactivate shader.
		GL20.glUseProgram(0);
	}
	
	public void setSkyBoxCubeMap(CubeMap skyBox){
		skyBoxCubeMap = skyBox;
	}
	
	public CubeMap getSkyBoxCubeMap(){
		return skyBoxCubeMap;
	}
	
	private final Vertex[] SKY_BOX_VERTICES = {
		// POS-Z face
		new Vertex(new Vec3(0.5f, -0.5f, 0.5f), new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
		new Vertex(new Vec3(-0.5f, -0.5f, 0.5f), new Vec2(0.0f, 1.0f), new Vec3(0.0f, -1.0f, 0.0f)),
		new Vertex(new Vec3(-0.5f, 0.5f, 0.5f), new Vec2(1.0f, 0.0f), new Vec3(-1.0f, 0.0f, 0.0f)),
		new Vertex(new Vec3(0.5f, 0.5f, 0.5f), new Vec2(1.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
		
		// NEG-Z face
		new Vertex(new Vec3(-0.5f, -0.5f, -0.5f), new Vec2(1.0f, 1.0f), new Vec3(0.0f, -1.0f, 0.0f)),
		new Vertex(new Vec3(0.5f, -0.5f, -0.5f), new Vec2(0.0f, 0.0f), new Vec3(1.0f, 0.0f, 0.0f)),
		new Vertex(new Vec3(0.5f, 0.5f, -0.5f), new Vec2(1.0f, 1.0f), new Vec3(0.0f, 0.0f, 1.0f)),
		new Vertex(new Vec3(-0.5f, 0.5f, -0.5f), new Vec2(1.0f, 0.0f), new Vec3(0.0f, 1.0f, 0.0f))
	};
	
	private final int[] SKY_BOX_INDICES = {
			// POS-Z face
			0, 1, 2,
			0, 2, 3,
			
			// NEG-Z face
			4, 5, 6,
			4, 6, 7,
			
			// POS-X face
			1, 4, 7,
			1, 7, 2,
			
			// NEG-X face
			5, 0, 3,
			5, 3, 6,
			
			// POS-Y face
			7, 6, 2,
			2, 6, 3,
			
			// NEG-Y face
			5, 4, 1,
			5, 1, 0
	};

}
