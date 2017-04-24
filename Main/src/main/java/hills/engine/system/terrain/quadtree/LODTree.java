package hills.engine.system.terrain.quadtree;

import hills.engine.math.Vec3;
import hills.engine.math.shape.Frustrum;
import hills.engine.system.terrain.TerrainSystem;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class LODTree {

	private final int MAP_WIDTH, MAP_DEPTH;
	private float[][][][] nodeMinMaxHeights;

	private List<LODNode> tree = new ArrayList<>();

	public LODTree(BufferedImage heightMapImage) {
		MAP_WIDTH = heightMapImage.getWidth();
		MAP_DEPTH = heightMapImage.getHeight();

		calcNodesMinMaxHeights(heightMapImage);
	}

	/**
	 * Will create a quad-tree from top down starting at the LOD level inputed
	 * until reaching<br>
	 * the most detailed layer. All the nodes that are to be rendered will get
	 * put into the list 'tree'<br>
	 * which will be cleared first.
	 * 
	 * @param pos
	 *            - Position to calculate LOD from, the closer to this point the
	 *            higher LOD.
	 * @param ranges
	 *            - The ranges that specify where the different LOD levels start
	 *            and stop.
	 * @param lodLevel
	 *            - The top LOD level. The lower this is the fewer LOD levels
	 *            will be created and <br>
	 *            consequently their will be less view distance.
	 * @param viewFrustrum
	 *            - The frustrum to use for culling. Any nodes outside of this
	 *            frustrum will not be rendered.
	 */
	public void genLODTree(Vec3 pos, float[] ranges, int lodLevel,
			Frustrum viewFrustrum) {
		tree.clear();

		float topNodeScale = TerrainSystem.SCALES[lodLevel];
		float topNodeWidth = TerrainSystem.GRID_WIDTH * topNodeScale;
		float topNodeDepth = TerrainSystem.GRID_DEPTH * topNodeScale;
		float topNodeX = TerrainSystem.GRID_WIDTH
				* ((int) pos.getX() / TerrainSystem.GRID_WIDTH)
				- (topNodeWidth / 2);
		float topNodeZ = TerrainSystem.GRID_DEPTH
				* ((int) pos.getZ() / TerrainSystem.GRID_DEPTH)
				- (topNodeDepth / 2);

		topNodeX = Math.min(MAP_WIDTH - topNodeWidth, Math.max(0, topNodeX));
		topNodeZ = Math.min(MAP_DEPTH - topNodeDepth, Math.max(0, topNodeZ));

		float topNodeY = nodeMinMaxHeights[lodLevel][(int) topNodeX
				/ TerrainSystem.GRID_WIDTH][(int) topNodeZ
				/ TerrainSystem.GRID_DEPTH][0];
		float topNodeHeight = nodeMinMaxHeights[lodLevel][(int) topNodeX
				/ TerrainSystem.GRID_WIDTH][(int) topNodeZ
				/ TerrainSystem.GRID_DEPTH][0]
				- topNodeY;

		genLODTree(new LODNode(topNodeX, topNodeY, topNodeZ, topNodeWidth,
				topNodeHeight, topNodeDepth, lodLevel), pos, ranges,
				viewFrustrum);
	}

	private boolean genLODTree(LODNode node, Vec3 pos, float[] ranges,
			Frustrum viewFrustrum) {
		int lodLevel = node.getLodLevel();

		// Check if node is within it's LOD range from position.
		// If not then return false and let parent node handle this subsection.
		if (!node.withinRange(pos, ranges[lodLevel]))
			return false;

		// If the node is not within the view frustrum, mark as handled.
		if (!viewFrustrum.intersects(node.getAaBox()))
			return true;

		// If node is in the last LOD level (most detailed),
		// set this node as leaf node.
		if (lodLevel == 0)
			tree.add(node); // Add node as leaf node
		else
		// If node not in lowest LOD range,
		// but entire node within its own LOD range
		// add node to be drawn as is.
		// Else check the four children nodes to see
		// which ones are in a lower LOD range.
		if (!node.withinRange(pos, ranges[lodLevel - 1]))
			tree.add(node); // Add node as leaf node.
		else {
			LODNode[] childNodes = node
					.getChildNodes(nodeMinMaxHeights[lodLevel - 1]);
			boolean[] clippableChildNodes = new boolean[] { true, true, true,
					true };
			boolean handleSubsection = false;
			for (int i = 0; i < childNodes.length; i++)
				if (!genLODTree(childNodes[i], pos, ranges, viewFrustrum)) {
					// If child node is outside of its LOD range,
					// let it's parent handle it's subsection.
					clippableChildNodes[i] = false;
					handleSubsection = true;
				}

			if (handleSubsection) {
				node.setClipMode(clippableChildNodes[0],
						clippableChildNodes[1], clippableChildNodes[2],
						clippableChildNodes[3]);
				tree.add(node);
			}
		}

		return true;
	}

	/**
	 * Calculate nodes min/max heights and store in nodeMinMaxHeights.
	 */
	private void calcNodesMinMaxHeights(BufferedImage heightMapImage) {
		// First calculate highest detailed LOD heights
		int gridWidth = TerrainSystem.GRID_WIDTH;
		int gridDepth = TerrainSystem.GRID_DEPTH;
		int gridTilesX = MAP_WIDTH / gridWidth;
		int gridTilesZ = MAP_DEPTH / gridDepth;
		float[][][] nodeMinMaxHeightsLOD0 = new float[gridTilesX][gridTilesZ][2];

		float heightStep = TerrainSystem.MAX_HEIGHT / 0xFFFFFF;

		for (int gridTileZ = 0; gridTileZ < gridTilesZ; gridTileZ++)
			for (int gridTileX = 0; gridTileX < gridTilesX; gridTileX++) {
				float tileMax = 0;
				float tileMin = TerrainSystem.MAX_HEIGHT;

				for (int y = 0; y < TerrainSystem.GRID_DEPTH; y++)
					for (int x = 0; x < TerrainSystem.GRID_WIDTH; x++) {
						int heightRGB = heightMapImage.getRGB(gridTileX
								* gridWidth + x, gridTileZ * gridDepth + y) & 0x00FFFFFF;

						tileMax = Math.max(tileMax, heightRGB * heightStep);
						tileMin = Math.min(tileMin, heightRGB * heightStep);
					}

				nodeMinMaxHeightsLOD0[gridTileX][gridTileZ][0] = tileMin;
				nodeMinMaxHeightsLOD0[gridTileX][gridTileZ][1] = tileMax;
			}

		// Use calculated LOD heights of highest detail to get lower detailed
		// heights
		int topLODLevel = 0;
		for (int i = 0; TerrainSystem.SCALES[i] <= gridTilesX; i++)
			topLODLevel++;

		System.out.println(topLODLevel);
		nodeMinMaxHeights = new float[topLODLevel][][][];
		nodeMinMaxHeights[0] = nodeMinMaxHeightsLOD0;

		for (int LOD = 1; LOD < topLODLevel; LOD++) {
			nodeMinMaxHeights[LOD] = calcSingleLODNodesMinMaxHeights(nodeMinMaxHeights[LOD - 1]);
		}
	}

	private float[][][] calcSingleLODNodesMinMaxHeights(
			float[][][] minMaxHeightNextLOD) {
		int tilesX = minMaxHeightNextLOD.length;
		int tilesY = minMaxHeightNextLOD[0].length;

		float[][][] result = new float[tilesX / 2][tilesY / 2][2];

		for (int tileY = 0; tileY < result[0].length; tileY++)
			for (int tileX = 0; tileX < result.length; tileX++) {
				float tileMin = TerrainSystem.MAX_HEIGHT;
				float tileMax = 0;

				for (int y = 0; y < 2; y++)
					for (int x = 0; x < 2; x++) {
						float heightMin = minMaxHeightNextLOD[tileX * 2 + x][tileY
								* 2 + y][0];
						float heightMax = minMaxHeightNextLOD[tileX * 2 + x][tileY
								* 2 + y][1];
						tileMin = Math.min(tileMin, heightMin);
						tileMax = Math.max(tileMax, heightMax);
					}

				// System.out.println("X/4: " + tilesX / 4 + " | Y/4: " + tilesY
				// / 4 + " | tileX: " + tileX + " | tileY: " + tileY +
				// " | Min: " + tileMin + " | Max: " + tileMax);
				result[tileX][tileY][0] = tileMin;
				result[tileX][tileY][1] = tileMax;
			}

		return result;
	}

	/**
	 * Will return a direct reference to the LOD node tree.
	 * 
	 * @return A reference to the LOD node tree.
	 */
	public List<LODNode> getLODNodeTree() {
		return tree;
	}
}
