package hills.services.terrain;

import hills.util.math.Vec3;

public interface TerrainHeightService {
	
	/**
	 *  Will return the height of the terrain at the x, z coordinate.<br>
	 *  OBS! If out of bounds will return 0.0f.
	 * @param x - The x coordinate to check height from terrain.
	 * @param z - The z coordinate to check height from terrain.
	 * @return The height of the terrain at the x, z coordinate.
	 */
	public float getHeight(float x, float z);

	public float getHeight(Vec3 pos);

}
