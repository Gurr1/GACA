package hills.services.terrain;

import hills.services.terrain.mesh.GridMeshData;
import hills.util.texturemap.TerrainTexture;

public interface ITerrainRenderDataService {

	public TerrainTexture getHeightMapTexture();
	public GridMeshData getGridMeshData();
}
