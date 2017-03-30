package hills.engine.system.terrain.quadtree;

import hills.engine.math.STD140Formatable;
import hills.engine.math.Vec3;
import hills.engine.math.shape.AABox;
import hills.engine.math.shape.Frustrum;
import hills.engine.system.terrain.TerrainSystem;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class LODNode implements STD140Formatable {
		
	private int lodLevel = -1;					// LOD Level
	private final float x, z;					// Position
	private final float width, depth, height;	// Size		
	private boolean[] subSectionsToHandle;		// Partial selection info
	
	private final AABox aaBox;
	
	private LODNode[] childNodes = null;
	
	public LODNode(float x, float z, float width, float depth, float height){
		this.x = x;
		this.z = z;
		this.width = width;
		this.depth = depth;
		this.height = height;
		
		aaBox = new AABox(x, 0.0f, z, width, height, depth);
		
		subSectionsToHandle = new boolean[4];
	}
	
	public boolean genLODNodeTree(Vec3 pos, float[] ranges, int lodLevel, Frustrum viewFrustrum) {
		this.lodLevel = -1; // Assume not a leaf node.
		
		// Check if node is within it's LOD range from position.
		// If not then return false and let parent node handle this subsection.
		if(!withinLODRange(pos, ranges[lodLevel] / TerrainSystem.MORPH_FACTOR)){
			//return false;
			this.lodLevel = lodLevel;	// TODO Fix sub area rendering
			return true;
		}
		
		// If the node is not within the view frustrum, mark as handled.
		if(!viewFrustrum.intersects(aaBox)){
			this.lodLevel = lodLevel;	// TODO Fix no render nodes
			return true;
		}
		
		// If node is in the last LOD level (most detailed),
		// set this node as leaf node.
		if(lodLevel == 0)
			this.lodLevel = 0; // Add node as leaf node
		else
			// If node not in lowest LOD range,
			// but entire node within its own LOD range
			// add node to be drawn as is.
			// Else check the four children nodes to see
			// which ones are in a lower LOD range.
			if(!withinLODRange(pos, ranges[lodLevel - 1] / TerrainSystem.MORPH_FACTOR))
				this.lodLevel = lodLevel; // Add node as leaf node.
			else {
				childNodes = getChildNodes();
				
				for(int i = 0; i < childNodes.length; i++)
					if(!childNodes[i].genLODNodeTree(pos, ranges, lodLevel - 1, viewFrustrum)){
						// If child node is outside of its LOD range,
						// let this node handle that subsection.
						subSectionsToHandle[i] = true; 
					}
			}
		
		return true;
	}
	
	/**
	 * GO through this nodes tree and fetch all leaf nodes.
	 * @return A list of all the leaf nodes.
	 */
	public List<LODNode> getLeafNodes(){
		if(lodLevel >= 0){
			List<LODNode> node = new ArrayList<LODNode>();
			node.add(this);
			return node;
		}
		
		List<LODNode> nodes = new ArrayList<LODNode>();
		for(LODNode node: childNodes)
			nodes.addAll(node.getLeafNodes());
		
		return nodes;
	}
	
	private boolean withinLODRange(Vec3 pos, float range){
		float cubeXPlane1 = x;
		float cubeXPlane2 = x + width;
		
		float cubeYPlane1 = 0.0f;
		float cubeYPlane2 = height;
		
		float cubeZPlane1 = z;
		float cubeZPlane2 = z + depth;
		
		float posX = pos.getX();
		float posY = pos.getY();
		float posZ = pos.getZ();
		
		float rangeSquared = range * range;
		
		if(posX < cubeXPlane1)
			rangeSquared -= (posX - cubeXPlane1) * (posX - cubeXPlane1);
		else if(posX > cubeXPlane2)
			rangeSquared -= (posX - cubeXPlane2) * (posX - cubeXPlane2);
		
		if(posY < cubeYPlane1)
			rangeSquared -= (posY - cubeYPlane1) * (posY - cubeYPlane1);
		else if(posY > cubeYPlane2)
			rangeSquared -= (posY - cubeYPlane2) * (posY - cubeYPlane2);
		
		if(posZ < cubeZPlane1)
			rangeSquared -= (posZ - cubeZPlane1) * (posZ - cubeZPlane1);
		else if(posZ > cubeZPlane2)
			rangeSquared -= (posZ - cubeZPlane2) * (posZ - cubeZPlane2);
		
		return rangeSquared > 0;
	}
	
	/**
	 * Create and get this nodes 4 child nodes.
	 * @return This nodes 4 child nodes.
	 */
	private LODNode[] getChildNodes(){
		LODNode[] nodes = new LODNode[4];
		
		float width = this.width / 2.0f;
		float depth = this.depth / 2.0f;
		
		nodes[0] = new LODNode(x + width, z + depth, width, depth, height);
		nodes[1] = new LODNode(x, z + depth, width, depth, height);
		nodes[2] = new LODNode(x, z, width, depth, height);
		nodes[3] = new LODNode(x + width, z, width, depth, height);
		
		return nodes;
	}
	
	@Override
	public void get140Data(ByteBuffer buffer) {
		buffer.putInt(lodLevel);
		buffer.putInt(0);
		buffer.putFloat(x);
		buffer.putFloat(z);
	}
	
	@Override
	public int get140DataSize(){
		return 16;				// TODO Create more generic solution?
	}
}
