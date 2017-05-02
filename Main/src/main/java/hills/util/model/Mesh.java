package hills.util.model;

import hills.util.math.Mat4;

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

	public int getVao() {
		return data.getVao();
	}
}
