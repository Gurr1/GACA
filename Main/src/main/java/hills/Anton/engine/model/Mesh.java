package hills.Anton.engine.model;

import hills.Anton.engine.math.Mat4;
import hills.Anton.engine.model.MeshData;
import hills.Anton.engine.model.MeshTexture;

public class Mesh {

	/**
	 * Mesh data of mesh. (id, VAO, VBO)
	 */
	private final MeshData data;
	
	/**
	 * Mesh textures.
	 */
	private final MeshTexture texture;
	
	/**
	 * Transformation matrix of this mesh.
	 */
	private final Mat4 transformation;
	
	public Mesh(MeshData data, MeshTexture texture, Mat4 transformation){
		this.data = data;
		this.texture = texture;
		this.transformation = transformation;
	}
	
	public MeshData getMeshData(){
		return data;
	}
	
	public MeshTexture getTexture(){
		return texture;
	}
	
	public Mat4 getTransformation(){
		return transformation;
	}
	
	public int getId() {
		return data.getId();
	}

	public int getVao() {
		return data.getVao();
	}
}
