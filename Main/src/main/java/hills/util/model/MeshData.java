package hills.util.model;

import lombok.Getter;

/**
 * @Author Anton Annlöv
 */
public class MeshData {

	/**
	 * VAO handle of mesh for rendering.
	 */
	@Getter
	private final int vao;

	/**
	 * Number of indices used by mesh.
	 */
	@Getter
	private final int indicesAmount;

	public MeshData(int vao, int indicesAmount) {
		this.vao = vao;
		this.indicesAmount = indicesAmount;
	}
}
