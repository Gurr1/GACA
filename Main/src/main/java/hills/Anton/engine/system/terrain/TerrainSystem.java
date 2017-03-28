package hills.Anton.engine.system.terrain;

import hills.Anton.engine.display.Display;
import hills.Anton.engine.math.Vec3;
import hills.Anton.engine.math.shape.Frustrum;
import hills.Anton.engine.renderer.TerrainRenderer;
import hills.Anton.engine.system.EngineSystem;
import hills.Anton.engine.system.camera.CameraSystem;
import hills.Anton.engine.system.terrain.quadtree.LODNode;

import java.util.List;

public class TerrainSystem extends EngineSystem {

	/** Singleton instance **/
	private static TerrainSystem instance = null;
	
	private final LODNode topNode;
	private List<LODNode> leafNodes;
	
	private CameraSystem cam;
	
	private TerrainSystem(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
		
		topNode = new LODNode(0.0f, 0.0f, 2048.0f, 2048.0f, 100.0f);
		
		cam = CameraSystem.getInstance();
	}

	@Override
	protected void update(double delta) {
		//16.0f, 32.0f, 
		float ranges[] = {64.0f, 128.0f, 256.0f, 512.0f, 1024.0f, 2048.0f, 4096.0f, 8192.0f};
		
		Vec3 pos = new Vec3(300.0f, 2.0f, 160.0f);
		Vec3 forward = new Vec3(1.0f, 0.0f, 1.0f);
		Vec3 up = new Vec3(0.0f, 1.0f, 0.0f);
		Vec3 right = forward.cross(up);
		
		Frustrum f = new Frustrum(0.1f, 3000.0f, (float) Display.getWidth() / (float) Display.getHeight(), 70.0f, pos, forward, up, right, true);
		
		topNode.genLODNodeTree(pos, ranges, 7, f);
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
