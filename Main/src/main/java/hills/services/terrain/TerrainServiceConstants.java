package hills.services.terrain;

public class TerrainServiceConstants {

	public static final String HEIGHT_MAP_DIRECTORY = "src/main/resources/textures/";
	public static final String HEIGHT_MAP_NAME = "finalNoise.png";
	public static final String HEIGHT_MAP_NORMAL_MAP_NAME = "normal.png";

	public static final float MORPH_FACTOR = 0.8f;

	/** OBS! Not true constants! **/
	protected static final float[] RANGES = new float[10];
	public static final float FIRST_RANGE = 128.0f;

	public static final float[] SCALES = { 1.0f, 2.0f, 4.0f, 8.0f, 16.0f, 32.0f, 64.0f, 128.0f, 256.0f, 512.0f };

	public static final int GRID_WIDTH = 16;
	public static final int GRID_DEPTH = 16;

	public static final float MAX_HEIGHT = 100.0f;

	public static final int TERRAIN_WIDTH = 2056;
	public static final int TERRAIN_HEIGHT = 2056;

	public static final float WATER_HEIGHT = 30;
	
}
