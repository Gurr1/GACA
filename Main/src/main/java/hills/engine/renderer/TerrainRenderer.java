package hills.engine.renderer;

import hills.engine.renderer.shader.SamplerUniform;
import hills.engine.renderer.shader.ShaderAttribute;
import hills.engine.renderer.shader.ShaderProgram;
import hills.engine.system.terrain.mesh.GridMeshData;
import hills.engine.system.terrain.quadtree.LODNode;
import hills.engine.texturemap.TerrainTexture;
import hills.engine.texturemap.TextureMap2D;

import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.system.MemoryStack;

public enum TerrainRenderer {
	INSTANCE();

	private final ShaderProgram shaderProgram = ShaderProgram.TERRAIN;
	private GridMeshData gridMeshData; // TerrainLoader.loadGridMesh(TerrainSystem.GRID_WIDTH, TerrainSystem.GRID_DEPTH);
	private TerrainTexture texture;

	private final TextureMap2D testTexture = new TextureMap2D("grass.png", SamplerUniform.TERRAIN_TEST_SAMPLER.getTextureSlot(), false);

	private List<LODNode> nodes;

	private TerrainRenderer() {
	} // Private constructor no instance

	public void batchNodes(List<LODNode> nodes, GridMeshData gridMeshData, TerrainTexture texture) {
		this.nodes = nodes;
		this.gridMeshData = gridMeshData;
		this.texture = texture;
	}

	public void render() {
		if (nodes == null) {
			System.err.println("No terrain nodes batched!");
			return;
		}

		// Activate shader program
		shaderProgram.enable();

		// Bind mesh VAO
		GL30.glBindVertexArray(gridMeshData.getVao());

		// Enable attributes
		GL20.glEnableVertexAttribArray(ShaderAttribute.POSITION.getLocation()); // Position attribute
		GL20.glEnableVertexAttribArray(ShaderAttribute.TEXTURECOORD.getLocation()); // Texture coordinate attribute
		GL20.glEnableVertexAttribArray(ShaderAttribute.NORMAL.getLocation()); // Normal attribute
		GL20.glEnableVertexAttribArray(ShaderAttribute.TANGENT.getLocation()); // Tangent attribute
		GL20.glEnableVertexAttribArray(ShaderAttribute.CLIP_PLANE1.getLocation()); // Instanced clip plane 1 info
		GL20.glEnableVertexAttribArray(ShaderAttribute.CLIP_PLANE2.getLocation()); // Instanced clip plane 2 info
		GL20.glEnableVertexAttribArray(ShaderAttribute.TERRAIN_ACTIVE_CLIP_MODES.getLocation()); // Info on which clipping mode to use
		GL20.glEnableVertexAttribArray(ShaderAttribute.TERRAIN_POS_AND_LOD.getLocation()); // Instanced terrain pos and lod info

		// TODO Fix terrain textures
		// Bind mesh texture
		texture.bind();
		testTexture.bind();

		// Enable clip distances
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		GL11.glEnable(GL30.GL_CLIP_DISTANCE1);
		GL11.glEnable(GL30.GL_CLIP_DISTANCE2);
		GL11.glEnable(GL30.GL_CLIP_DISTANCE3);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer instancedData = stack.calloc(nodes.size() * LODNode.INSTANCED_DATA_SIZE);
			
			// Get the instance specific data of tall the nodes
			for (LODNode node : nodes)
				node.getInstanceData(instancedData);

			// Flip the buffer so it's ready to be loaded into the gridMesh instanced VBO
			instancedData.flip();

			// Load instancedData into the instancedVBO of the gridMeshData
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, gridMeshData.getInstancedVBO());
			//GL15.glBufferData(GL15.GL_ARRAY_BUFFER, nodes.size() * LODNode.INSTANCED_DATA_SIZE, GL15.GL_DYNAMIC_DRAW);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, instancedData);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			
			// Render grid mesh
			GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, gridMeshData.getIndicesAmount(), GL11.GL_UNSIGNED_INT, 0, nodes.size());

		}

		// Disable clip distances
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		GL11.glDisable(GL30.GL_CLIP_DISTANCE1);
		GL11.glDisable(GL30.GL_CLIP_DISTANCE2);
		GL11.glDisable(GL30.GL_CLIP_DISTANCE3);

		// Disable attributes
		GL20.glDisableVertexAttribArray(ShaderAttribute.POSITION.getLocation()); // Position attribute
		GL20.glDisableVertexAttribArray(ShaderAttribute.TEXTURECOORD.getLocation()); // Texture coordinate attribute
		GL20.glDisableVertexAttribArray(ShaderAttribute.NORMAL.getLocation()); // Normal attribute
		GL20.glDisableVertexAttribArray(ShaderAttribute.TANGENT.getLocation()); // Tangent attribute
		GL20.glDisableVertexAttribArray(ShaderAttribute.CLIP_PLANE1.getLocation()); // Instanced clip plane 1 info
		GL20.glDisableVertexAttribArray(ShaderAttribute.CLIP_PLANE2.getLocation()); // Instanced clip plane 2 info
		GL20.glDisableVertexAttribArray(ShaderAttribute.TERRAIN_ACTIVE_CLIP_MODES.getLocation()); // Info on which clipping mode to use
		GL20.glDisableVertexAttribArray(ShaderAttribute.TERRAIN_POS_AND_LOD.getLocation()); // Instanced terrain pos and lod info

		// Unbind mesh VAO
		GL30.glBindVertexArray(0);

		// Deactivate shaders.
		GL20.glUseProgram(0);
	}

	public void clearBatch() {
		// Clear batched terrain nodes
		nodes = null;
	}
}
