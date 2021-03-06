package hills.services.terrain;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryStack;

import hills.util.math.STD140Formatable;
import hills.util.shader.ShaderProgram;

/**
 * @Author Anton Annlöv
 */
public enum TerrainServiceLoader {
	INSTANCE;
	
	private TerrainServiceLoader(){
	}
	
	protected void loadRangesConstant(float startRange){
		TerrainServiceConstants.RANGES[0] = TerrainServiceConstants.FIRST_RANGE;
		for (int i = 1; i < TerrainServiceConstants.RANGES.length; i++)
			TerrainServiceConstants.RANGES[i] = TerrainServiceConstants.RANGES[i - 1] * 2.0f;
	}
	
	/**
	 * Load terrain height values from height map image
	 * @param heightMap
	 */
	protected float[][] loadHeightValues(BufferedImage heightMap){
		float[][] heightValues = new float[heightMap.getWidth()][heightMap.getHeight()];

		float heightStep = TerrainServiceConstants.MAX_HEIGHT / 0xFFFFFF;

		for(int z = 0; z < heightMap.getHeight(); z++)
			for(int x = 0; x < heightMap.getWidth(); x++)
				heightValues[x][z] = (heightMap.getRGB(x, z) & 0xFFFFFF) * heightStep;
		
		return heightValues;
	}

	/**
	 * Load terrain-shader terrain constants.
	 */
	protected void uploadTerrainShaderConstants(){
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
			ByteBuffer dataBuffer = stack.calloc(STD140Formatable.ARRAY_ALIGNMENT * TerrainServiceConstants.RANGES.length);
			for (int i = 0; i < TerrainServiceConstants.RANGES.length; i++) {
				dataBuffer.putFloat((TerrainServiceConstants.RANGES[i] * TerrainServiceConstants.MORPH_FACTOR));
				dataBuffer.putFloat(TerrainServiceConstants.RANGES[i]);
				dataBuffer.putFloat(0.0f);
				dataBuffer.putFloat(0.0f);
			}
			dataBuffer.flip();

			ShaderProgram.map("TERRAIN_CONSTANTS", "RANGES_SQUARED[0]", dataBuffer);
		}
	}
	
	/**
	 * Load scales constant array
	 */
	private void loadScalesConstant() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer dataBuffer = stack.calloc(STD140Formatable.ARRAY_ALIGNMENT * TerrainServiceConstants.SCALES.length);
			for (int i = 0; i < TerrainServiceConstants.SCALES.length; i++) {
				dataBuffer.putFloat(TerrainServiceConstants.SCALES[i]);
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
			ByteBuffer dataBuffer = stack.calloc(STD140Formatable.VECTOR_2_ALIGNMENT);
			dataBuffer.putFloat(TerrainServiceConstants.GRID_WIDTH);
			dataBuffer.putFloat(TerrainServiceConstants.GRID_DEPTH);
			dataBuffer.flip();
			
			ShaderProgram.map("TERRAIN_CONSTANTS", "GRID_SIZE", dataBuffer);
		}
	}
	
	/**
	 * Load terrain size constant
	 */
	private void loadTerrainSizeConstant() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer dataBuffer = stack.calloc(STD140Formatable.VECTOR_2_ALIGNMENT);
			dataBuffer.putFloat(TerrainServiceConstants.TERRAIN_WIDTH);
			dataBuffer.putFloat(TerrainServiceConstants.TERRAIN_HEIGHT);
			dataBuffer.flip();
			
			ShaderProgram.map("TERRAIN_CONSTANTS", "TERRAIN_SIZE", dataBuffer);
		}
	}
	
	/**
	 * Load max height constant
	 */
	private void loadMaxHeightConstant() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer dataBuffer = stack.calloc(STD140Formatable.SCALAR_ALIGNMENT);
			dataBuffer.putFloat(TerrainServiceConstants.MAX_HEIGHT);
			dataBuffer.flip();
			
			ShaderProgram.map("TERRAIN_CONSTANTS", "MAX_HEIGHT", dataBuffer);
		}
	}
	
	/**
	 * Load start range constant
	 */
	protected void loadStartRangeConstant(int startRange) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer dataBuffer = stack.calloc(STD140Formatable.SCALAR_ALIGNMENT);
			dataBuffer.putInt(startRange);
			dataBuffer.flip();
			
			ShaderProgram.map("TERRAIN_CONSTANTS", "START_RANGE", dataBuffer);
		}
	}
}
