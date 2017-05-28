package hills.util.loader;

import hills.services.terrain.mesh.GridMeshData;
import hills.services.terrain.tree.LODNode;
import hills.util.math.Mat4;
import hills.util.math.Vec2;
import hills.util.math.Vec3;
import hills.util.math.Vec4;
import hills.util.math.Vertex;
import hills.util.model.Mesh;
import hills.util.shader.ShaderAttribute;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

/**
 * @Author Anton Annlöv
 */
//TODO Move to service
public final class TerrainLoader {

	public static GridMeshData loadGridMesh(int width, int depth, int terrainWidth, int terrainDepth) {
		Vertex[] ver = new Vertex[width * depth * 4];
		int[] ind = new int[width * depth * 6];

		for (int tileZ = 0; tileZ < depth; tileZ++)
			for (int tileX = 0; tileX < width; tileX++) {
				int currentTileIndex = tileX + tileZ * depth;

				// For each tile create 4 vertices
				ver[currentTileIndex * 4 + 0] = new Vertex(new Vec3(tileX, 0.0f, tileZ), new Vec2(0, 1), new Vec3(tileX, tileZ, currentTileIndex));
				ver[currentTileIndex * 4 + 1] = new Vertex(new Vec3(tileX + 1, 0.0f, tileZ), new Vec2(1, 1), new Vec3(tileX, tileZ, currentTileIndex));
				ver[currentTileIndex * 4 + 2] = new Vertex(new Vec3(tileX + 1, 0.0f, tileZ + 1), new Vec2(1, 0), new Vec3(tileX, tileZ, currentTileIndex));
				ver[currentTileIndex * 4 + 3] = new Vertex(new Vec3(tileX, 0.0f, tileZ + 1), new Vec2(0, 0), new Vec3(tileX, tileZ, currentTileIndex));

				// For each tile create 6 indices
				ind[currentTileIndex * 6 + 0] = currentTileIndex * 4;
				ind[currentTileIndex * 6 + 1] = currentTileIndex * 4 + 2;
				ind[currentTileIndex * 6 + 2] = currentTileIndex * 4 + 1;

				ind[currentTileIndex * 6 + 3] = currentTileIndex * 4;
				ind[currentTileIndex * 6 + 4] = currentTileIndex * 4 + 3;
				ind[currentTileIndex * 6 + 5] = currentTileIndex * 4 + 2;
			}

		Mesh mesh = ModelLoader.load(ver, ind, null, Mat4.identity());

		// Bind max vertex attribute instance data needed for terrain rendering
		// (terrainWidth / gridWidth) * (terrainHeight / gridHeight) * size of:
		// vec4 (clip_plane)
		// vec4 (clip_plane2)
		// vec4 (active clip modes)
		// vec3 (posX, posZ, lodLevel)

		// Calculate size and stride in bytes and allocate vertex array buffer
		// for storing instance variables.
		int stride = LODNode.INSTANCED_DATA_SIZE;
		long size = stride * (terrainWidth / width) * (terrainDepth / depth);
		
		int instanceVBO = ModelLoader.createEmptyVBO(GL15.GL_ARRAY_BUFFER, size, GL15.GL_DYNAMIC_DRAW);

		// Get the current grid mesh vao, calculate the stride of the attributes
		// and add the instanced attributes.
		int vao = mesh.getVao();
		ModelLoader.addVAOAttribute(vao, instanceVBO, ShaderAttribute.CLIP_PLANE1.getLocation(), 4, GL11.GL_FLOAT, false, stride, 0, 1);
		ModelLoader.addVAOAttribute(vao, instanceVBO, ShaderAttribute.CLIP_PLANE2.getLocation(), 4, GL11.GL_FLOAT, false, stride, Vec4.SIZE * Float.BYTES, 1);
		ModelLoader.addVAOAttribute(vao, instanceVBO, ShaderAttribute.TERRAIN_ACTIVE_CLIP_MODES.getLocation(), 4, GL11.GL_FLOAT, false, stride, 2 * Vec4.SIZE * Float.BYTES, 1);
		ModelLoader.addVAOAttribute(vao, instanceVBO, ShaderAttribute.TERRAIN_POS_AND_LOD.getLocation(), 3, GL11.GL_FLOAT, false, stride, 3 * Vec4.SIZE * Float.BYTES, 1);

		return new GridMeshData(vao, instanceVBO, mesh.getMeshData().getIndicesAmount());
	}
}
