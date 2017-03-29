package hills.Anton.engine.system.terrain;

import hills.Anton.engine.display.Display;
import hills.Anton.engine.math.STD140Formatable;
import hills.Anton.engine.math.Vec3;
import hills.Anton.engine.math.shape.Frustrum;
import hills.Anton.engine.renderer.TerrainRenderer;
import hills.Anton.engine.renderer.shader.ShaderProgram;
import hills.Anton.engine.system.EngineSystem;
import hills.Anton.engine.system.camera.CameraSystem;
import hills.Anton.engine.system.terrain.quadtree.LODNode;

import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class TerrainSystem extends EngineSystem {

	/** Singleton instance **/
	private static TerrainSystem instance = null;
	
	public static final float MORPH_FACTOR = 0.9f;
	public static final float[] RANGES = {16.0f, 32.0f, 64.0f, 128.0f, 256.0f, 512.0f, 1024.0f, 2048.0f, 4096.0f, 8192.0f};
	public static final float[] SCALES = {1.0f, 2.0f, 4.0f, 8.0f, 16.0f, 32.0f, 64.0f, 128.0f, 256.0f, 512.0f};
	public static final int GRID_WIDTH = 16;
	public static final int GRID_DEPTH = 16;
	public static final int TERRAIN_WIDTH = 2048;
	public static final int TERRAIN_DEPTH = 2048;
	public static final float MAX_HEIGHT = 100.0f;
	
	private final LODNode topNode;
	private List<LODNode> leafNodes;
	
	private CameraSystem cam;
	
	private TerrainSystem(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
		
		// Load terrain-shader terrain constants
		loadTerrainConstants();
		
		topNode = new LODNode(0.0f, 0.0f, TerrainSystem.TERRAIN_WIDTH, TerrainSystem.TERRAIN_DEPTH, 100.0f);
		
		cam = CameraSystem.getInstance();
	}
	
	@Override
	protected void update(double delta) {
		
		Vec3 pos = new Vec3(300.0f, 2.0f, 160.0f);
		Vec3 forward = new Vec3(1.0f, 0.0f, 1.0f);
		Vec3 up = new Vec3(0.0f, 1.0f, 0.0f);
		Vec3 right = forward.cross(up);
		
		Frustrum f = new Frustrum(0.1f, 3000.0f, (float) Display.getWidth() / (float) Display.getHeight(), 70.0f, pos, forward, up, right, true);
		
		topNode.genLODNodeTree(cam.getPosition(), TerrainSystem.RANGES, 7, cam.getFrustrum());
		leafNodes = topNode.getLeafNodes();
	}

	@Override
	public void render() {
		TerrainRenderer.batchNodes(leafNodes);
	}
	
	/**
	 * Load terrain-shader terrain constants.
	 */
	private void loadTerrainConstants(){
		loadRangesSquaredConstant();
		loadScalesConstant();
		loadGridSizeConstant();
		loadTerrainSizeConstant();
		loadMaxHeightConstant();
		loadStartRangeConstant(0);
	}

	/**
	 * Load ranges squared constant array
	 */
	private void loadRangesSquaredConstant(){
		try(MemoryStack stack = MemoryStack.stackPush()){
			ByteBuffer dataBuffer = stack.calloc(STD140Formatable.ARRAY_ALIGNMENT * TerrainSystem.RANGES.length);
			for(int i = 0; i < TerrainSystem.RANGES.length; i++){
				dataBuffer.putFloat((TerrainSystem.RANGES[i] * TerrainSystem.MORPH_FACTOR) * (TerrainSystem.RANGES[i] * TerrainSystem.MORPH_FACTOR));
				dataBuffer.putFloat(TerrainSystem.RANGES[i] * TerrainSystem.RANGES[i]);
				dataBuffer.putFloat(0.0f);
				dataBuffer.putFloat(0.0f);
			}
			dataBuffer.flip();
			
			ShaderProgram.map("TERRAIN_CONSTANTS", "RANGES_SQUARED[0]", dataBuffer);
		}
	}
	
	/**
	 * Load scales constant array
	 */
	private void loadScalesConstant(){
		try(MemoryStack stack = MemoryStack.stackPush()){
			ByteBuffer dataBuffer = stack.calloc(STD140Formatable.ARRAY_ALIGNMENT * TerrainSystem.SCALES.length);
			for(int i = 0; i < TerrainSystem.SCALES.length; i++){
				dataBuffer.putFloat(TerrainSystem.SCALES[i]);
				dataBuffer.putFloat(0.0f);
				dataBuffer.putFloat(0.0f);
				dataBuffer.putFloat(0.0f);
			}
			dataBuffer.flip();
			
			ShaderProgram.map("TERRAIN_CONSTANTS", "SCALES[0]", dataBuffer);
		}
	}
	
	/**
	 * Load grid size constant
	 */
	private void loadGridSizeConstant(){
		try(MemoryStack stack = MemoryStack.stackPush()){
			ByteBuffer dataBuffer = stack.calloc(STD140Formatable.VECTOR_2_ALIGNMENT);
			dataBuffer.putFloat(TerrainSystem.GRID_WIDTH);
			dataBuffer.putFloat(TerrainSystem.GRID_DEPTH);
			dataBuffer.flip();
			
			ShaderProgram.map("TERRAIN_CONSTANTS", "GRID_SIZE", dataBuffer);
		}
	}
	
	/**
	 * Load terrain size constant
	 */
	private void loadTerrainSizeConstant(){
		try(MemoryStack stack = MemoryStack.stackPush()){
			ByteBuffer dataBuffer = stack.calloc(STD140Formatable.VECTOR_2_ALIGNMENT);
			dataBuffer.putFloat(TerrainSystem.TERRAIN_WIDTH);
			dataBuffer.putFloat(TerrainSystem.TERRAIN_DEPTH);
			dataBuffer.flip();
			
			ShaderProgram.map("TERRAIN_CONSTANTS", "TERRAIN_SIZE", dataBuffer);
		}
	}
	
	/**
	 * Load max height constant
	 */
	private void loadMaxHeightConstant(){
		try(MemoryStack stack = MemoryStack.stackPush()){
			ByteBuffer dataBuffer = stack.calloc(STD140Formatable.SCALAR_ALIGNMENT);
			dataBuffer.putFloat(TerrainSystem.MAX_HEIGHT);
			dataBuffer.flip();
			
			ShaderProgram.map("TERRAIN_CONSTANTS", "MAX_HEIGHT", dataBuffer);
		}
	}
	
	/**
	 * Load start range constant
	 */
	public void loadStartRangeConstant(int startRange){
		try(MemoryStack stack = MemoryStack.stackPush()){
			ByteBuffer dataBuffer = stack.calloc(STD140Formatable.SCALAR_ALIGNMENT);
			dataBuffer.putInt(startRange);
			dataBuffer.flip();
			
			ShaderProgram.map("TERRAIN_CONSTANTS", "START_RANGE", dataBuffer);
		}
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
