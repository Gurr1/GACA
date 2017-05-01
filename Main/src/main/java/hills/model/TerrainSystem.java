package hills.model;

import hills.services.generation.TerrainData;
import hills.engine.loader.TerrainLoader;
import hills.util.Math.STD140Formatable;
import hills.util.Math.Vec2;
import hills.util.Math.Vec3;
import hills.engine.renderer.TerrainRenderer;
import hills.engine.renderer.shader.ShaderProgram;
import hills.engine.system.EngineSystem;
import hills.engine.system.terrain.mesh.GridMeshData;
import hills.engine.system.terrain.quadtree.LODTree;
import hills.engine.texturemap.TerrainTexture;
import hills.view.CameraModel;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class TerrainSystem extends EngineSystem {

	public static final String HEIGHT_MAP_DIRECTORY = "src/main/resources/textures/";
	public static final String HEIGHT_MAP_NAME = "finalNoise.png";
	public static final String HEIGHT_MAP_NORMAL_MAP_NAME = "normal.png";

	public static final float MORPH_FACTOR = 0.8f;

	public static final float[] RANGES = new float[10];
	public static final float FIRST_RANGE = 128.0f;

	public static final float[] SCALES = { 1.0f, 2.0f, 4.0f, 8.0f, 16.0f,
			32.0f, 64.0f, 128.0f, 256.0f, 512.0f };

	public static final int GRID_WIDTH = 16;
	public static final int GRID_DEPTH = 16;

	public static final float MAX_HEIGHT = 100.0f;

	public static int TERRAIN_WIDTH = 2056;
	public static int TERRAIN_HEIGHT = 2056;

	private final GridMeshData gridMeshData;
	private final TerrainTexture heightMapTexture;

	private final LODTree TREE;

	private BufferedImage heightMap, heightNormalMap;
	private float[][] heightValues;
	
	private CameraModel cam;
	
	private TerrainSystem(float scale, boolean isPaused, float startTime) {		// td was included to minimize loadtimes and to create more accurate hightdata.
		super(scale, isPaused, startTime);
		
		// Calculate ranges
		RANGES[0] = FIRST_RANGE;
		for (int i = 1; i < RANGES.length; i++)
			RANGES[i] = RANGES[i - 1] * 2.0f;

		// Get height map image
		try {
			heightMap = ImageIO.read(new File(HEIGHT_MAP_DIRECTORY + HEIGHT_MAP_NAME));
			heightNormalMap = ImageIO.read(new File(HEIGHT_MAP_DIRECTORY + HEIGHT_MAP_NORMAL_MAP_NAME));

			TERRAIN_WIDTH = heightMap.getWidth();
			TERRAIN_HEIGHT = heightMap.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		heightMapTexture = new TerrainTexture(TerrainSystem.HEIGHT_MAP_NAME, TerrainSystem.HEIGHT_MAP_NORMAL_MAP_NAME);

		loadHeightValues(heightMap);

		// Load grid mesh
		gridMeshData = TerrainLoader.loadGridMesh(GRID_WIDTH, GRID_DEPTH, TERRAIN_WIDTH, TERRAIN_HEIGHT);

		// Load terrain-shader terrain constants
		loadTerrainConstants();

		cam = CameraModel.getInstance();

		// Initiate terrain tree with a height map for it to analyze
		TREE = new LODTree(heightMap);
	}
	
	@Override
	protected void update(double delta) {
		// Vec3 pos = new Vec3(300.0f, 2.0f, 160.0f);
		// Vec3 forward = new Vec3(1.0f, 0.0f, 1.0f);
		// Vec3 up = new Vec3(0.0f, 1.0f, 0.0f);
		// Vec3 right = forward.cross(up);

		// Frustrum f = new Frustrum(0.1f, 1000.0f, (float) Display.getWidth() /
		// (float) Display.getHeight(), 70.0f, pos, forward, up, right, true);

		TREE.genLODTree(cam.getPosition(), TerrainSystem.RANGES, 7, cam.getFrustrum());
	}

	@Override
	public void render() {
		TerrainRenderer.INSTANCE.batchNodes(TREE.getLODNodeTree(), gridMeshData, heightMapTexture);
	}

	private void loadHeightValues(BufferedImage heightMap){
		heightValues = new float[TERRAIN_WIDTH][TERRAIN_HEIGHT];

		float heightStep = MAX_HEIGHT / 0xFFFFFF;

		for(int z = 0; z < TERRAIN_HEIGHT; z++)
			for(int x = 0; x < TERRAIN_WIDTH; x++)
				heightValues[x][z] = (heightMap.getRGB(x, z) & 0xFFFFFF) * heightStep;
	}

	/**
	 *  Will return the height of the terrain at the x, z coordinate.<br>
	 *  OBS! If out of bounds will return 0.0f.
	 * @param x - The x coordinate to check height from terrain.
	 * @param z - The z coordinate to check height from terrain.
	 * @return The height of the terrain at the x, z coordinate.
	 */
	public float getHeight(float x, float z){
		// Handle edge cases
		if(x < 0.0f || x > TERRAIN_WIDTH - 1 || z < 0.0f || z > TERRAIN_HEIGHT - 1)
			return 0.0f;


		int intX = (int) x;
		int intZ = (int) z;

		boolean xGreater = x - intX > z - intZ;

		float heightA = heightValues[intX][intZ],
			  heightB = xGreater ? heightValues[intX + 1][intZ] : heightValues[intX][intZ + 1],
			  heightC = heightValues[intX + 1][intZ + 1];

		// Calculate barycentric coordinates of terrain triangle at x, 0.0, z.
		Vec2 A = new Vec2(intX, intZ);
		Vec2 v0 = (xGreater ? new Vec2(intX + 1, intZ) : new Vec2(intX, intZ + 1)).sub(A),
			 v1 = new Vec2(intX + 1, intZ + 1).sub(A),
			 v2 = new Vec2(x, z).sub(A);

	    float d00 = v0.getLengthSqr();
	    float d01 = v0.dot(v1);
	    float d11 = v1.getLengthSqr();
	    float d20 = v2.dot(v0);
	    float d21 = v2.dot(v1);

	    float denom = d00 * d11 - d01 * d01;

	    // Barycentric coordinates
	    float a = (d11 * d20 - d01 * d21) / denom;
	    float b = (d00 * d21 - d01 * d20) / denom;
	    float c = 1.0f - a - b;

		return heightA * a + heightB * b + heightC * c;
	}

	public float getHeight(Vec3 pos){
		return getHeight(pos.getX(), pos.getZ());
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
	private void loadRangesSquaredConstant() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer dataBuffer = stack
					.calloc(STD140Formatable.ARRAY_ALIGNMENT
							* TerrainSystem.RANGES.length);
			for (int i = 0; i < TerrainSystem.RANGES.length; i++) {
				dataBuffer.putFloat((TerrainSystem.RANGES[i] * TerrainSystem.MORPH_FACTOR));
				dataBuffer.putFloat(TerrainSystem.RANGES[i]);
				dataBuffer.putFloat(0.0f);
				dataBuffer.putFloat(0.0f);
			}
			dataBuffer.flip();

			ShaderProgram.map("TERRAIN_CONSTANTS", "RANGES_SQUARED[0]",
					dataBuffer);
		}
	}
	
	/**
	 * Load scales constant array
	 */
	private void loadScalesConstant() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer dataBuffer = stack
					.calloc(STD140Formatable.ARRAY_ALIGNMENT
							* TerrainSystem.SCALES.length);
			for (int i = 0; i < TerrainSystem.SCALES.length; i++) {
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
	private void loadGridSizeConstant() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer dataBuffer = stack
					.calloc(STD140Formatable.VECTOR_2_ALIGNMENT);
			dataBuffer.putFloat(TerrainSystem.GRID_WIDTH);
			dataBuffer.putFloat(TerrainSystem.GRID_DEPTH);
			dataBuffer.flip();
			
			ShaderProgram.map("TERRAIN_CONSTANTS", "GRID_SIZE", dataBuffer);
		}
	}
	
	/**
	 * Load terrain size constant
	 */
	private void loadTerrainSizeConstant() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer dataBuffer = stack
					.calloc(STD140Formatable.VECTOR_2_ALIGNMENT);
			dataBuffer.putFloat(TERRAIN_WIDTH);
			dataBuffer.putFloat(TERRAIN_HEIGHT);
			dataBuffer.flip();
			
			ShaderProgram.map("TERRAIN_CONSTANTS", "TERRAIN_SIZE", dataBuffer);
		}
	}
	
	/**
	 * Load max height constant
	 */
	private void loadMaxHeightConstant() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer dataBuffer = stack
					.calloc(STD140Formatable.SCALAR_ALIGNMENT);
			dataBuffer.putFloat(TerrainSystem.MAX_HEIGHT);
			dataBuffer.flip();
			
			ShaderProgram.map("TERRAIN_CONSTANTS", "MAX_HEIGHT", dataBuffer);
		}
	}
	
	/**
	 * Load start range constant
	 */
	public void loadStartRangeConstant(int startRange) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer dataBuffer = stack
					.calloc(STD140Formatable.SCALAR_ALIGNMENT);
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

	/** Singleton instance **/
	private static TerrainSystem instance = null;

}
