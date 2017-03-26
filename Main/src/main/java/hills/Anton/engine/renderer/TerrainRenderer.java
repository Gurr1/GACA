package hills.Anton.engine.renderer;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import hills.Anton.engine.loader.TerrainLoader;
import hills.Anton.engine.math.Mat4;
import hills.Anton.engine.math.Vec2;
import hills.Anton.engine.model.Mesh;
import hills.Anton.engine.model.MeshTexture;
import hills.Anton.engine.renderer.shader.ShaderAttribute;
import hills.Anton.engine.renderer.shader.ShaderProgram;
import hills.Anton.engine.system.terrain.quadtree.LODNode;

public final class TerrainRenderer {

	private static final ShaderProgram shaderProgram = ShaderProgram.TERRAIN;
	private static final Mesh gridMesh = TerrainLoader.loadGridMesh(16, 16);
	private static final MeshTexture texture = new MeshTexture("height_map_test_1.png");
	
	private static List<LODNode> nodes;
	
	private TerrainRenderer(){} // Private constructor no instance
	
	public static void batchNodes(List<LODNode> nodes){
		TerrainRenderer.nodes = nodes;
	}
	
	public static void render(){
		if(TerrainRenderer.nodes == null){
			System.err.println("No terrain nodes batched!");
			return;
		}
		
		// Activate shader program
		shaderProgram.enable();
					
		// Bind mesh VAO
		GL30.glBindVertexArray(gridMesh.getVao());
		
		// Enable attributes
		GL20.glEnableVertexAttribArray(ShaderAttribute.POSITION.getLocation());		// Position attribute
		GL20.glEnableVertexAttribArray(ShaderAttribute.TEXTURECOORD.getLocation());	// Texture coordinate attribute
		GL20.glEnableVertexAttribArray(ShaderAttribute.NORMAL.getLocation());		// Normal attribute
		GL20.glEnableVertexAttribArray(ShaderAttribute.TANGENT.getLocation());		// Tangent attribute
		
		// TODO Fix terrain texture
		// Bind mesh texture
		texture.bind();
		
		for(LODNode node: nodes){
			ShaderProgram.map("TERRAIN", node.get140Data());
			
			// Render grid mesh
			GL11.glDrawElements(GL11.GL_TRIANGLES, gridMesh.getMeshData().getIndicesAmount(), GL11.GL_UNSIGNED_INT, 0);
		}
		
		// Disable attributes
		GL20.glDisableVertexAttribArray(ShaderAttribute.POSITION.getLocation()); 		// Position attribute
		GL20.glDisableVertexAttribArray(ShaderAttribute.TEXTURECOORD.getLocation()); 	// Texture coordinate attribute
		GL20.glDisableVertexAttribArray(ShaderAttribute.NORMAL.getLocation()); 			// Normal attribute
		GL20.glDisableVertexAttribArray(ShaderAttribute.TANGENT.getLocation());			// Tangent attribute
		
		// Unbind mesh VAO
		GL30.glBindVertexArray(0);
		
		// Deactivate shaders.
		GL20.glUseProgram(0);
		
		// Clear batched terrain nodes
		TerrainRenderer.nodes = null;
	}
}
