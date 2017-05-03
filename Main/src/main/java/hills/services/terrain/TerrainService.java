package hills.services.terrain;

import hills.services.generation.TerrainData;
import hills.services.terrain.mesh.GridMeshData;
import hills.services.terrain.tree.LODTree;
import hills.util.loader.TerrainLoader;
import hills.util.math.STD140Formatable;
import hills.util.math.Vec2;
import hills.util.math.Vec3;
import hills.util.shader.ShaderProgram;
import hills.util.texturemap.TerrainTexture;
import hills.engine.system.EngineSystem;
import hills.view.CameraModel;
import hills.view.renderer.TerrainRenderer;

import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

// TODO Split
public class TerrainService extends EngineSystem {

	private final GridMeshData gridMeshData;
	private final TerrainTexture heightMapTexture;

	private final LODTree TREE;

	private BufferedImage heightMap, heightNormalMap;
	private float[][] heightValues;
	
	private CameraModel cam;
	
	private TerrainService(float scale, boolean isPaused, float startTime) {
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
		
		heightMapTexture = new TerrainTexture(TerrainService.HEIGHT_MAP_NAME, TerrainService.HEIGHT_MAP_NORMAL_MAP_NAME);

		loadHeightValues(heightMap);

		// Load grid mesh
		gridMeshData = TerrainLoader.loadGridMesh(GRID_WIDTH, GRID_DEPTH, TERRAIN_WIDTH, TERRAIN_HEIGHT);

		// Load terrain-shader terrain constants
		loadTerrainConstants();

		cam = CameraModel.getInstance();

		// Initiate terrain tree with a height map for it to analyze
		TREE = new LODTree(heightMap);
	}
	
	// TODO Move
	@Override
	protected void update(double delta) {
		// Vec3 pos = new Vec3(300.0f, 2.0f, 160.0f);
		// Vec3 forward = new Vec3(1.0f, 0.0f, 1.0f);
		// Vec3 up = new Vec3(0.0f, 1.0f, 0.0f);
		// Vec3 right = forward.cross(up);

		// Frustrum f = new Frustrum(0.1f, 1000.0f, (float) Display.getWidth() /
		// (float) Display.getHeight(), 70.0f, pos, forward, up, right, true);

		TREE.genLODTree(cam.getPosition(), TerrainService.RANGES, 7, cam.getFrustrum());
	}

	// TODO Move
	@Override
	public void render() {
		TerrainRenderer.INSTANCE.batchNodes(TREE.getLODNodeTree(), gridMeshData, heightMapTexture);
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
		
		instance = new TerrainService(1.0f, false, 0.0f);
		return true;
	}
	
	/**
	 * 
	 * @return The singleton instance of TerrainSystem.
	 * @throws NullPointerException If singleton instance has not been created.
	 */
	public static TerrainService getInstance() throws NullPointerException {
		if(instance == null)
			throw new NullPointerException("Singleton instance not created!");
		
		return instance;
	}

	/** Singleton instance **/
	private static TerrainService instance = null;

}
