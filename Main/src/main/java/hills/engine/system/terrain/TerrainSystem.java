package hills.engine.system.terrain;

import hills.engine.loader.TerrainLoader;
import hills.engine.loader.TextureLoader;
import hills.engine.math.STD140Formatable;
import hills.engine.math.Vec3;
import hills.engine.renderer.TerrainRenderer;
import hills.engine.renderer.shader.ShaderProgram;
import hills.engine.system.EngineSystem;
import hills.engine.system.camera.CameraSystem;
import hills.engine.system.terrain.mesh.GridMeshData;
import hills.engine.system.terrain.quadtree.LODTree;
import hills.engine.texturemap.TerrainTexture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.lwjgl.system.MemoryStack;

public class TerrainSystem extends EngineSystem {

	public static final String HEIGHT_MAP_DIRECTORY = "/textures/";
	public static final String HEIGHT_MAP_NAME = "height_map_test_3.png";
	public static final String HEIGHT_MAP_NORMAL_MAP_NAME = "height_map_test_3_normal_smooth.png";

	public static final float MORPH_FACTOR = 0.8f;

	public static final float[] RANGES = new float[10];
	public static final float FIRST_RANGE = 128.0f;

	public static final float[] SCALES = { 1.0f, 2.0f, 4.0f, 8.0f, 16.0f,
			32.0f, 64.0f, 128.0f, 256.0f, 512.0f };

	public static final int GRID_WIDTH = 16;
	public static final int GRID_DEPTH = 16;

	public static final float MAX_HEIGHT = 100.0f;

	private int TERRAIN_WIDTH = 0;
	private int TERRAIN_DEPTH = 0;

	private final GridMeshData gridMeshData;
	private final TerrainTexture heightMapTexture;

	private final LODTree TREE;

	private BufferedImage heightMap, heightNormalMap;
	private CameraSystem cam;

	private TerrainSystem(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);

		// Calculate ranges
		RANGES[0] = FIRST_RANGE;
		for (int i = 1; i < RANGES.length; i++)
			RANGES[i] = RANGES[i - 1] * 2.0f;
		
		// Get height map image
		try {
			InputStream heightMapImageInput = TerrainSystem.class.getResourceAsStream(HEIGHT_MAP_DIRECTORY + HEIGHT_MAP_NAME);//ImageIO.createImageInputStream(new File(TerrainSystem.class.getResource(HEIGHT_MAP_DIRECTORY + HEIGHT_MAP_NAME).getFile()));
			heightMap = ImageIO.read(heightMapImageInput);
			if(heightMap == null)
				heightMapImageInput.close();
			
			InputStream heightNormalMapImageInput = TerrainSystem.class.getResourceAsStream(HEIGHT_MAP_DIRECTORY + HEIGHT_MAP_NORMAL_MAP_NAME);//ImageIO.createImageInputStream(new File(TerrainSystem.class.getResource(HEIGHT_MAP_DIRECTORY + HEIGHT_MAP_NORMAL_MAP_NAME).getFile()));
			heightNormalMap = ImageIO.read(heightNormalMapImageInput);
			if(heightNormalMap == null)
				heightNormalMapImageInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		heightMapTexture = new TerrainTexture(TerrainSystem.HEIGHT_MAP_NAME, TerrainSystem.HEIGHT_MAP_NORMAL_MAP_NAME);

		TERRAIN_WIDTH = heightMap.getWidth();
		TERRAIN_DEPTH = heightMap.getHeight();

		// Load grid mesh
		gridMeshData = TerrainLoader.loadGridMesh(GRID_WIDTH, GRID_DEPTH, TERRAIN_WIDTH, TERRAIN_DEPTH);

		// Load terrain-shader terrain constants
		loadTerrainConstants();

		// Initiate terrain tree with a height map for it to analyze
		TREE = new LODTree(heightMap);

		cam = CameraSystem.getInstance();
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

	/**
	 * Load terrain-shader terrain constants.
	 */
	private void loadTerrainConstants() {
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
			dataBuffer.putFloat(TERRAIN_DEPTH);
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
	 * 
	 * @return False if an instance has already been created.
	 */
	public static boolean createInstance() {
		if (instance != null)
			return false;

		instance = new TerrainSystem(1.0f, false, 0.0f);
		return true;
	}

	/**
	 * 
	 * @return The singleton instance of TerrainSystem.
	 * @throws NullPointerException
	 *             If singleton instance has not been created.
	 */
	public static TerrainSystem getInstance() throws NullPointerException {
		if (instance == null)
			throw new NullPointerException("Singleton instance not created!");

		return instance;
	}

	/** Singleton instance **/
	private static TerrainSystem instance = null;

}
