package hills.services.terrain.mesh;

import lombok.Getter;

/**
 * @author Anton
 */
public class GridMeshData {

	/**
	 * VAO handle of mesh for rendering.
	 */
	@Getter
	private final int vao;

	/**
	 * VAO handle of mesh for rendering.
	 */
	@Getter
	private final int instancedVBO;

	/**
	 * Number of indices used by mesh.
	 */
	@Getter
	private final int indicesAmount;

	public GridMeshData(int vao, int instancedVBO, int indicesAmount) {
		this.vao = vao;
		this.indicesAmount = indicesAmount;
		this.instancedVBO = instancedVBO;
	}
}
