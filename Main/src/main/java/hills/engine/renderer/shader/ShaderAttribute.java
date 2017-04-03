package hills.engine.renderer.shader;

public enum ShaderAttribute {

	POSITION("pos", 0),
	TEXTURECOORD("tex", 1),
	NORMAL("nor", 2),
	TANGENT("tangent", 3),
	CLIP_PLANE1("clipPlane1", 4),
	CLIP_PLANE2("clipPlane2", 5),
	TERRAIN_ACTIVE_CLIP_MODES("terrainActiveClipModes", 6),
	TERRAIN_POS_AND_LOD("terrainPosAndLOD", 7);

	/**
	 * Name of attribute.
	 */
	private final String name;

	/**
	 * Attribute location.
	 */
	private final int location;

	/**
	 * Define a new shader attribute.
	 * 
	 * @param name
	 *            - Name of attribute in shader.
	 * @param location
	 *            - Attribute location.
	 */
	private ShaderAttribute(String name, int location) {
		this.name = name;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public int getLocation() {
		return location;
	}

}
