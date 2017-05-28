package hills.services.terrain;

import hills.services.Service;
import hills.services.terrain.mesh.GridMeshData;
import hills.services.terrain.tree.LODNode;
import hills.services.terrain.tree.LODTree;
import hills.util.loader.TerrainLoader;
import hills.util.math.Vec2;
import hills.util.math.Vec3;
import hills.util.math.shape.Frustrum;
import hills.util.texturemap.TerrainTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Anton
 */
public class TerrainService implements Service, ITerrainTreeService, ITerrainHeightService, ITerrainRenderDataService {
	
	private final LODTree tree;
	private final GridMeshData gridMeshData;
	private final TerrainTexture heightMapTexture;

	private final float[][] heightValues;
	
	public TerrainService() {
		// Load the ranges 'constant'
		TerrainServiceLoader.INSTANCE.loadRangesConstant(TerrainServiceConstants.FIRST_RANGE);
		
		// Get height map and normal map images for reading terrain data that should be cached.
		BufferedImage heightMap = null;
		BufferedImage heightNormalMap = null;
		try {
			heightMap = ImageIO.read(new File(TerrainServiceConstants.HEIGHT_MAP_DIRECTORY + TerrainServiceConstants.HEIGHT_MAP_NAME));
			heightNormalMap = ImageIO.read(new File(TerrainServiceConstants.HEIGHT_MAP_DIRECTORY + TerrainServiceConstants.HEIGHT_MAP_NORMAL_MAP_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Cache height and normal values of the height/normal map
		heightValues = TerrainServiceLoader.INSTANCE.loadHeightValues(heightMap);
		// TODO Cache normal values
		
		// Load grid mesh
		gridMeshData = TerrainLoader.loadGridMesh(TerrainServiceConstants.GRID_WIDTH, TerrainServiceConstants.GRID_DEPTH, TerrainServiceConstants.TERRAIN_WIDTH, TerrainServiceConstants.TERRAIN_HEIGHT);
		
		// Load height map texture ready for GPU usage
		heightMapTexture = new TerrainTexture(TerrainServiceConstants.HEIGHT_MAP_NAME, TerrainServiceConstants.HEIGHT_MAP_NORMAL_MAP_NAME);
		
		// TODO Move? Upload terrain-shader terrain constants to the GPU
		TerrainServiceLoader.INSTANCE.uploadTerrainShaderConstants();
		
		// Initiate terrain tree with a height map for it to analyze
		tree = new LODTree(heightMap);
	}

	/**
	 * Will construct a new tree of terrain nodes and return the visible ones in a list.
	 * @param position - The view position.
	 * @param frustrum - The view frustrum.
	 * @return A list of all visible terrain nodes. OBS! Returns a direct reference to the list. Use with caution!
	 */
	public List<LODNode> getLODNodeTree(Vec3 position, Frustrum frustrum){
		tree.genLODTree(position, TerrainServiceConstants.RANGES, 7, frustrum); // TODO Customize top LOD level
		return tree.getLODNodeTree();
	}
	
	public GridMeshData getGridMeshData(){
		return gridMeshData;
	}
	
	public TerrainTexture getHeightMapTexture(){
		return heightMapTexture;
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
		if(x < 0.0f || x > TerrainServiceConstants.TERRAIN_WIDTH - 1 || z < 0.0f || z > TerrainServiceConstants.TERRAIN_HEIGHT - 1)
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
		System.out.println("Terrain service cleaned up!");
	}

}
