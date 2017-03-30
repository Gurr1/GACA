package hills.engine.renderer;

import hills.engine.loader.TerrainLoader;
import hills.engine.model.Mesh;
import hills.engine.renderer.shader.ShaderAttribute;
import hills.engine.renderer.shader.ShaderProgram;
import hills.engine.system.terrain.TerrainSystem;
import hills.engine.system.terrain.quadtree.LODNode;
import hills.engine.texturemap.TerrainTexture;

import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

public final class TerrainRenderer {

	private static final ShaderProgram shaderProgram = ShaderProgram.TERRAIN;
	private static final Mesh gridMesh = TerrainLoader.loadGridMesh(TerrainSystem.GRID_WIDTH, TerrainSystem.GRID_DEPTH);
	private static final TerrainTexture texture = new TerrainTexture("height_map_test_3.png", "height_map_test_3_normal_smooth.png");
	
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
		
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		
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
			try(MemoryStack stack = MemoryStack.stackPush()){
				ByteBuffer dataBuffer = stack.calloc(16);
				node.get140Data(dataBuffer);
				dataBuffer.flip();
				ShaderProgram.map("TERRAIN_NODE", dataBuffer);
				
				// Render grid mesh
				GL11.glDrawElements(GL11.GL_TRIANGLES, gridMesh.getMeshData().getIndicesAmount(), GL11.GL_UNSIGNED_INT, 0);
			}
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
