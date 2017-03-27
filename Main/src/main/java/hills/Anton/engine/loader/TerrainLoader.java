package hills.Anton.engine.loader;

import hills.Anton.engine.math.Mat4;
import hills.Anton.engine.math.Vec2;
import hills.Anton.engine.math.Vec3;
import hills.Anton.engine.math.Vertex;
import hills.Anton.engine.model.Mesh;

public final class TerrainLoader {

	public static Mesh loadGridMesh(int width, int depth){
		Vertex[] ver = new Vertex[width * depth * 4];
		int[] ind = new int[width * depth * 6];
		
		for(int tileZ = 0; tileZ < depth; tileZ++)
			for(int tileX = 0; tileX < width; tileX++){
				int currentTileIndex = tileX + tileZ * depth;
				
				// For each tile create 4 vertices
				ver[currentTileIndex * 4 + 0] = new Vertex(new Vec3(tileX, 0.0f, tileZ), new Vec2((float) tileX / width, ((float) tileZ / depth)), new Vec3(0.0f, 1.0f, 0.0f));
				ver[currentTileIndex * 4 + 1] = new Vertex(new Vec3(tileX + 1, 0.0f, tileZ), new Vec2((float) (tileX + 1) / width, ((float) tileZ / depth)), new Vec3(0.0f, 1.0f, 0.0f));
				ver[currentTileIndex * 4 + 2] = new Vertex(new Vec3(tileX + 1, 0.0f, tileZ + 1), new Vec2((float) (tileX + 1) / width, ((float) (tileZ + 1) / depth)), new Vec3(0.0f, 1.0f, 0.0f));
				ver[currentTileIndex * 4 + 3] = new Vertex(new Vec3(tileX, 0.0f, tileZ + 1), new Vec2((float) tileX / width, ((float) (tileZ + 1) / depth)), new Vec3(0.0f, 1.0f, 0.0f));
			
				// For each tile create 6 indices
				ind[currentTileIndex * 6 + 0] = currentTileIndex * 4;
				ind[currentTileIndex * 6 + 1] = currentTileIndex * 4 + 2;
				ind[currentTileIndex * 6 + 2] = currentTileIndex * 4 + 1;
				
				ind[currentTileIndex * 6 + 3] = currentTileIndex * 4;
				ind[currentTileIndex * 6 + 4] = currentTileIndex * 4 + 3;
				ind[currentTileIndex * 6 + 5] = currentTileIndex * 4 + 2;
			}
		
		return ModelLoader.load(ver, ind, null, Mat4.identity());
	}
}
