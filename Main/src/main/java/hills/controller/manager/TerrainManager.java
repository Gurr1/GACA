package hills.controller.manager;

import hills.controller.AbstractController;
import hills.services.ServiceLocator;
import hills.services.camera.ICameraDataService;
import hills.services.terrain.ITerrainRenderDataService;
import hills.services.terrain.ITerrainTreeService;
import hills.services.terrain.tree.LODNode;
import hills.view.RenderLocator;
import hills.view.TerrainRenderer;

import java.util.List;

/**
 * @Author Anton Annlöv
 */
public final class TerrainManager extends AbstractController {

	// TODO Move LODNode to Util package?
	private List<LODNode> TerrainNodes;
	
	private ITerrainTreeService treeService;
	private ITerrainRenderDataService renderDataService;
	private ICameraDataService camDataService;
	
	public TerrainManager(float scale, boolean isPaused, float startTime) {
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
		RenderLocator.INSTANCE.getTerrainBatchable().batchNodes(TerrainNodes, renderDataService.getGridMeshData(), renderDataService.getHeightMapTexture());
	}

	@Override
	public void cleanUp() {
		System.out.println("Cleaned up TerrainManager!");
	}
	
	
	
	
}
