package hills.services.terrain;

import hills.services.terrain.mesh.GridMeshData;
import hills.util.texturemap.TerrainTexture;

/**
 * @author Anton
 */
public interface ITerrainRenderDataService {

	public TerrainTexture getHeightMapTexture();
	public GridMeshData getGridMeshData();
}
