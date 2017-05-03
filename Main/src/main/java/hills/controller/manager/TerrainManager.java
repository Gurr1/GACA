package hills.controller.manager;

import java.util.List;

import hills.engine.system.EngineSystem;
import hills.services.camera.CameraService;
import hills.services.terrain.TerrainService;
import hills.services.terrain.tree.LODNode;
import hills.view.renderer.TerrainRenderer;

public final class TerrainManager extends EngineSystem {

	// TODO Move LODNode to Util package?
	private List<LODNode> TerrainNodes;
	
	protected TerrainManager(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
	}

	@Override
	protected void update(double delta) {
		TerrainNodes = TerrainService.INSTANCE.updateLODNodeTree(CameraService.INSTANCE.getPosition(), CameraService.INSTANCE.getFrustrum());
	}

	@Override
	public void render() {
		TerrainRenderer.INSTANCE.batchNodes(TerrainNodes, TerrainService.INSTANCE.getGridMeshData(), TerrainService.INSTANCE.getHeightMapTexture());
	}

	@Override
	public void cleanUp() {
		System.out.println("Cleaned up TerrainManager!");
	}
	
	
	
	
}
