package hills.Anton.engine.system.terrain;

import java.util.List;

import hills.Anton.engine.math.Vec3;
import hills.Anton.engine.renderer.TerrainRenderer;
import hills.Anton.engine.system.EngineSystem;
import hills.Anton.engine.system.camera.CameraSystem;
import hills.Anton.engine.system.terrain.quadtree.LODNode;

public class TerrainSystem extends EngineSystem {

	/** Singleton instance **/
	private static TerrainSystem instance = null;
	
	private final LODNode topNode;
	private List<LODNode> leafNodes;
	
	private CameraSystem cam;
	
	private TerrainSystem(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
		
		topNode = new LODNode(0.0f, 0.0f, 2048.0f, 2048.0f, 10.0f);
		
		cam = CameraSystem.getInstance();
	}

	@Override
	protected void update(double delta) {
		float ranges[] = {16.0f, 32.0f, 64.0f, 128.0f, 256.0f, 512.0f, 1024.0f, 2048.0f, 4096.0f, 8192.0f};
		topNode.genLODNodeTree(cam.getPosition(), ranges, 7);
		leafNodes = topNode.getLeafNodes();
	}

	@Override
	public void render() {
		TerrainRenderer.batchNodes(leafNodes);
	}

	@Override
	public void cleanUp() {
		System.out.println("Terrain system cleaned up!");
	}
	
	/**
	 * Creates the singleton instance of TerrainSystem.
	 * @return False if an instance has already been created.
	 */
	public static boolean createInstance() {
		if(instance != null)
			return false;
		
		instance = new TerrainSystem(1.0f, false, 0.0f);
		return true;
	}
	
	/**
	 * 
	 * @return The singleton instance of TerrainSystem.
	 * @throws NullPointerException If singleton instance has not been created.
	 */
	public static TerrainSystem getInstance() throws NullPointerException {
		if(instance == null)
			throw new NullPointerException("Singleton instance not created!");
		
		return instance;
	}

}
