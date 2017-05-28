package hills.view;

import hills.services.terrain.mesh.GridMeshData;
import hills.services.terrain.tree.LODNode;
import hills.util.texturemap.TerrainTexture;

import java.util.List;

/**
 * @author Anton
 */
public interface ITerrainRendererBatchable {
	public void batchNodes(List<LODNode> nodes, GridMeshData gridMeshData, TerrainTexture texture);
	public void clearBatch();
}
