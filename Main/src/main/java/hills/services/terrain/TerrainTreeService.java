package hills.services.terrain;

import java.util.List;

import hills.services.terrain.tree.LODNode;
import hills.util.math.Vec3;
import hills.util.math.shape.Frustrum;

public interface TerrainTreeService {
	
	/**
	 * Will construct a new tree of terrain nodes and return the visible ones in a list.
	 * @param position - The view position.
	 * @param frustrum - The view frustrum.
	 * @return A list of all visible terrain nodes. OBS! Returns a direct reference to the list. Use with caution!
	 */
	public List<LODNode> getLODNodeTree(Vec3 position, Frustrum frustrum);

}
