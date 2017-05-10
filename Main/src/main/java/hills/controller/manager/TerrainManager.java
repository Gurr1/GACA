package hills.controller.manager;

import hills.controller.EngineSystem;
import hills.services.ServiceLocator;
import hills.services.camera.ICameraDataService;
import hills.services.terrain.TerrainRenderDataService;
import hills.services.terrain.TerrainTreeService;
import hills.services.terrain.tree.LODNode;
import hills.view.renderer.TerrainRenderer;

import java.util.List;

public final class TerrainManager extends EngineSystem {

	// TODO Move LODNode to Util package?
	private List<LODNode> TerrainNodes;
	
	private TerrainTreeService treeService;
	private TerrainRenderDataService renderDataService;
	private ICameraDataService camDataService;
	
	protected TerrainManager(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
		
		treeService = ServiceLocator.INSTANCE.getTerrainTreeService();
		renderDataService = ServiceLocator.INSTANCE.getTerrianRenderDataService();
		camDataService = ServiceLocator.INSTANCE.getCameraDataService();
	}

	@Override
	protected void update(double delta) {
		TerrainNodes = treeService.getLODNodeTree(camDataService.getPosition(), camDataService.getFrustrum());
	}

	@Override
	public void render() {
		TerrainRenderer.INSTANCE.batchNodes(TerrainNodes, renderDataService.getGridMeshData(), renderDataService.getHeightMapTexture());
	}

	@Override
	public void cleanUp() {
		System.out.println("Cleaned up TerrainManager!");
	}
	
	
	
	
}
