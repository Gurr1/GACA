package hills.services.terrain;

import hills.services.terrain.mesh.GridMeshData;
import hills.util.texturemap.TerrainTexture;

public interface TerrainRenderDataService {

	public TerrainTexture getHeightMapTexture();
	public GridMeshData getGridMeshData();
}
