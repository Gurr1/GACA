package hills.engine.system.terrain.quadtree;

import hills.util.Math.STD140Formatable;
import hills.util.Math.Vec3;
import hills.util.Math.Vec4;
import hills.util.Math.shape.AABox;
import hills.util.Math.shape.Plane;
import hills.util.Math.shape.Sphere;
import hills.model.TerrainSystem;
import lombok.Getter;

import java.nio.ByteBuffer;

public class LODNode implements STD140Formatable {

	/**
	 * Size of an LODNodes instance data in bytes.
	 */
	public static final int INSTANCED_DATA_SIZE = (3 * Vec4.SIZE + Vec3.SIZE) * Float.BYTES;

	@Getter private final AABox aaBox;
	@Getter private final int lodLevel;

	@Getter private LODNodeClipMode clipMode = LODNodeClipMode.NONE;
	@Getter private Plane clipPlane1 = new Plane(new Vec3(0.0f, 0.0f, 0.0f), new Vec3(0.0f, 0.0f, 0.0f));
	@Getter private Plane clipPlane2 = new Plane(new Vec3(0.0f, 0.0f, 0.0f), new Vec3(0.0f, 0.0f, 0.0f));

	public LODNode(float x, float y, float z, float width, float height,
			float depth, int lodLevel) {
		aaBox = new AABox(x, y, z, width, height, depth);
		this.lodLevel = lodLevel;
	}

	protected boolean withinRange(Vec3 pos, float range) {
		return aaBox.intersects(new Sphere(pos, range));
	}

	/**
	 * Creates and returns this nodes 4 child nodes. in order from top right
	 * corner going CCW.
	 * 
	 * @param nodeMinMaxHeight
	 *            - An array of all the nodes min and max heights for this nodes
	 *            child nodes LOD level.
	 * @return This nodes 4 child nodes.
	 */
	protected LODNode[] getChildNodes(float[][][] nodeMinMaxHeight) {
		LODNode[] nodes = new LODNode[4];

		Vec3 pos = aaBox.getPos();
		Vec3 size = aaBox.getSize();

		float x = pos.getX();
		float z = pos.getZ();
		float width = size.getX() / 2.0f;
		float depth = size.getZ() / 2.0f;

		int tileX = (int) x / (int) (TerrainSystem.GRID_WIDTH * TerrainSystem.SCALES[lodLevel]) * 2;
		int tileZ = (int) z / (int) (TerrainSystem.GRID_DEPTH * TerrainSystem.SCALES[lodLevel]) * 2;

		float[] minMaxHeight = nodeMinMaxHeight[tileX + 1][tileZ + 1];
		nodes[0] = new LODNode(x + width, minMaxHeight[0], z + depth, width,
				minMaxHeight[1] - minMaxHeight[0], depth, lodLevel - 1);

		minMaxHeight = nodeMinMaxHeight[tileX][tileZ + 1];
		nodes[1] = new LODNode(x, minMaxHeight[0], z + depth, width,
				minMaxHeight[1] - minMaxHeight[0], depth, lodLevel - 1);

		minMaxHeight = nodeMinMaxHeight[tileX][tileZ];
		nodes[2] = new LODNode(x, minMaxHeight[0], z, width, minMaxHeight[1]
				- minMaxHeight[0], depth, lodLevel - 1);

		minMaxHeight = nodeMinMaxHeight[tileX + 1][tileZ];
		nodes[3] = new LODNode(x + width, minMaxHeight[0], z, width,
				minMaxHeight[1] - minMaxHeight[0], depth, lodLevel - 1);

		return nodes;
	}

	/**
	 * Calculates what clip function this node needs to use when rendering.
	 * 
	 * @param clipTR
	 *            - Boolean array where true means clip. each index<br>
	 *            represents a child node starting from top right going CCW.
	 */
	protected void setClipMode(boolean clipTR, boolean clipTL, boolean clipBL,
			boolean clipBR) {
		Vec3 nodeCenter = aaBox.getPos().add(aaBox.getSize().mul(0.5f));

		// Calculated with karnaugh diagram to give 5 different combinations
		// depending on input
		boolean a = clipTR;
		boolean b = clipTL;
		boolean c = clipBL;
		boolean d = clipBR;
		boolean f3 = (!a & b & c & d) | (a & !b & c & d) | (a & b & !c & d)
				| (a & b & c & !d);
		boolean f2 = (!a & !b & c & d) | (!a & b & !c & d) | (!a & b & c & !d)
				| (a & !b & !c & d) | (a & !b & c & !d) | (a & b & !c & !d);
		boolean f1 = (!a & !c & d) | (!b & c & !d) | (!a & b & !c)
				| (a & !b & !d);

		if (f3) {
			clipMode = LODNodeClipMode.TRIPPLE;

			Vec3 nx = !clipTL | !clipBL ? new Vec3(-1.0f, 0.0f, 0.0f)
					: new Vec3(1.0f, 0.0f, 0.0f);
			clipPlane1 = new Plane(nodeCenter, nx);

			Vec3 nz = !clipBL | !clipBR ? new Vec3(0.0f, 0.0f, -1.0f)
					: new Vec3(0.0f, 0.0f, 1.0f);
			clipPlane2 = new Plane(nodeCenter, nz);
		} else if (f2 & f1) {
			clipMode = LODNodeClipMode.DIAGONAL;

			Vec3 nx = !clipTR ? new Vec3(1.0f, 0.0f, 0.0f) : new Vec3(-1.0f,
					0.0f, 0.0f);
			clipPlane1 = new Plane(nodeCenter, nx);
			clipPlane2 = new Plane(nodeCenter, new Vec3(0.0f, 0.0f, 1.0f));
		} else if (f2 & !f1) {
			clipMode = LODNodeClipMode.NEIGHBOUR;

			boolean y2 = (!a & !b) | (!c & !d);
			boolean y1 = !a;

			Vec3 v = new Vec3(-1.0f, 0.0f, 0.0f);
			if (!y2 & y1)
				v = new Vec3(1.0f, 0.0f, 0.0f);
			else if (y2 & !y1)
				v = new Vec3(0.0f, 0.0f, -1.0f);
			else if (y2 & y1)
				v = new Vec3(0.0f, 0.0f, 1.0f);

			clipPlane1 = new Plane(nodeCenter, v);
			clipPlane2 = new Plane(nodeCenter, new Vec3(0.0f, 0.0f, 0.0f));
		} else if (!f2 & f1) {
			clipMode = LODNodeClipMode.SINGLE;

			Vec3 nx = clipTR | clipBR ? new Vec3(-1.0f, 0.0f, 0.0f) : new Vec3(
					1.0f, 0.0f, 0.0f);
			clipPlane1 = new Plane(nodeCenter, nx);

			Vec3 nz = clipTR | clipTL ? new Vec3(0.0f, 0.0f, -1.0f) : new Vec3(
					0.0f, 0.0f, 1.0f);
			clipPlane2 = new Plane(nodeCenter, nz);
		} else {
			clipMode = LODNodeClipMode.NONE;
		}
	}

	/**
	 * Fills the buffer with the data of this LODNode according<br>
	 * to the attribute layout in the terrain shader.
	 * 
	 * @param buffer
	 *            - the buffer to fill with the data.
	 */
	public void getInstanceData(ByteBuffer buffer) {
		// Fill clipPlane1 attribute
		clipPlane1.inEquationForm().get140Data(buffer);

		// Fill clipPlane2 attribute
		clipPlane2.inEquationForm().get140Data(buffer);

		// Fill terrainClipModes attribute
		buffer.putFloat(clipMode == LODNodeClipMode.NEIGHBOUR || clipMode == LODNodeClipMode.TRIPPLE ? 1.0f : 0.0f);
		buffer.putFloat(clipMode == LODNodeClipMode.TRIPPLE ? 1.0f : 0.0f);
		buffer.putFloat(clipMode == LODNodeClipMode.DIAGONAL ? 1.0f : 0.0f);
		buffer.putFloat(clipMode == LODNodeClipMode.SINGLE ? 1.0f : 0.0f);

		// Fill terrainPosAndLOD attribute
		buffer.putFloat(aaBox.getPos().getX());
		buffer.putFloat(aaBox.getPos().getZ());
		buffer.putFloat(lodLevel);
	}

	@Override
	public void get140Data(ByteBuffer buffer) {
		buffer.putInt(lodLevel);
		buffer.putInt(0);
		buffer.putFloat(aaBox.getPos().getX());
		buffer.putFloat(aaBox.getPos().getZ());
	}

	@Override
	public int get140DataSize() {
		return 16; // TODO Create more generic solution?
	}
}
