package hills.view;

import java.util.List;

import hills.services.terrain.mesh.GridMeshData;
import hills.services.terrain.tree.LODNode;
import hills.util.texturemap.TerrainTexture;

public interface ITerrainRenderBatchable {
	public void batchNodes(List<LODNode> nodes, GridMeshData gridMeshData, TerrainTexture texture);
	public void clearBatch();
}
