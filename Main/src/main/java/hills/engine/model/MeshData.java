package hills.engine.model;

import lombok.Getter;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class MeshData {

	/**
	 * Unique id for mesh. Set on creation.
	 */
	@Getter private final int id;
	
	/**
	 * VAO handle of mesh for rendering.
	 */
	@Getter private final int vao;
	
	/**
	 * VBO handle of mesh for rendering.
	 */
	private final int[] vbos;
	
	/**
	 * Number of indices used by mesh.
	 */
	@Getter private final int indicesAmount;
	
	public MeshData(int id, int vao, int[] vbos, int indicesAmount){
		this.id = id;
		this.vao = vao;
		this.vbos = vbos;
		this.indicesAmount = indicesAmount;
	}
	
	/**
	 * Free memory associated with this mesh VAO and VBO
	 */
	public void free(){
		// Delete VAO
		GL30.glDeleteVertexArrays(vao);
		
		// Delete associated VBOs
		for(Integer vbo: vbos)
			GL15.glDeleteBuffers(vbo);
	}
}
