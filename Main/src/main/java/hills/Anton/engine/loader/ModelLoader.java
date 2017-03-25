package hills.Anton.engine.loader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import hills.Anton.engine.model.MeshData;
import hills.Anton.engine.renderer.shader.ShaderAttribute;
import hills.Anton.engine.util.BufferUtil;
import hills.Anton.engine.math.Mat4;
import hills.Anton.engine.math.Vertex;
import hills.Anton.engine.model.Mesh;
import hills.Anton.engine.model.MeshTexture;

public final class ModelLoader {

	/**
	 * Loaded mesh data. Used to create mesh instances which are used to create models.
	 */
	private static List<MeshData> loadedMeshData = new ArrayList<MeshData>();
	
	private ModelLoader(){} // Private constructor no instances!
	
	/**
	 * Creates a new mesh and mesh data from the vertices and indices.
	 * @param vertices - Vertices of mesh.
	 * @param indices - Indices of mesh.
	 * @param texture - Mesh texture for mesh.
	 * @param transformation - Mesh transformation.
	 * @return New mesh.
	 */
	public static Mesh load(Vertex[] vertices, int[] indices, MeshTexture texture, Mat4 transformation){
		// Get needed data from vertices
		float[] posData = new float[vertices.length * 3]; // 3 position floats per vertex
		float[] texData = new float[vertices.length * 2]; // 2 textureCoord floats per vertex
		float[] norData = new float[vertices.length * 3]; // 3 normal floats per vertex
		float[] tanData = new float[vertices.length * 4]; // 4 tangent floats per vertex
		
		for(int i = 0; i < vertices.length; i++){
			// Fill posData
			posData[i * 3 + 0] = vertices[i].getPosition().getX();
			posData[i * 3 + 1] = vertices[i].getPosition().getY();
			posData[i * 3 + 2] = vertices[i].getPosition().getZ();
			
			// Fill texData
			texData[i * 2 + 0] = vertices[i].getTextureCoordinate().getX();
			texData[i * 2 + 1] = vertices[i].getTextureCoordinate().getY();
			
			// Fill norData
			norData[i * 3 + 0] = vertices[i].getNormal().getX();
			norData[i * 3 + 1] = vertices[i].getNormal().getY();
			norData[i * 3 + 2] = vertices[i].getNormal().getZ();
			
			// Fill tanData
			tanData[i * 4 + 0] = vertices[i].getTangent().getX();
			tanData[i * 4 + 1] = vertices[i].getTangent().getY();
			tanData[i * 4 + 2] = vertices[i].getTangent().getZ();
			tanData[i * 4 + 3] = vertices[i].getTangent().getW();
		}
		
		return new Mesh(loadMeshData(posData, texData, norData, tanData, indices), texture, transformation);
	}
	
	/**
	 * Creates new mesh data and stores it in 'loadedMeshData' for later freeing.
	 * @param posData - Vertex positions.
	 * @param texData - Texture coordinates.
	 * @param norData - Vertex normals.
	 * @param indices - Vertex indices.
	 * @return New mesh data.
	 */
	private static MeshData loadMeshData(float[] posData, float[] texData, float[] norData, float[] tanData, int[] indices){
		// Bind vao and fill with VBO:s
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		
		// Bind indices buffer
		int IBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, IBO);
		
		// Fill indices buffer
		IntBuffer iBuffer = BufferUtil.storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL15.GL_STATIC_DRAW);
		
		// Unbind indices buffer
		//GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		// Bind vertices buffer for position data
		int VBOPositions = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOPositions);
		
		// Fill vertices buffer
		FloatBuffer fBuffer = BufferUtil.storeDataInFloatBuffer(posData);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuffer, GL15.GL_STATIC_DRAW);
		
		// Map buffer data
		GL20.glVertexAttribPointer(ShaderAttribute.POSITION.getLocation(), 3, GL11.GL_FLOAT, false, 0, 0); // Attribute 0 - 3 floats per attribute
		
		// Bind vertices buffer for texture data
		int VBOTextureCoords = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTextureCoords);
		
		// Fill vertices buffer
		fBuffer = BufferUtil.storeDataInFloatBuffer(texData);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuffer, GL15.GL_STATIC_DRAW);
		
		// Map buffer data
		GL20.glVertexAttribPointer(ShaderAttribute.TEXTURECOORD.getLocation(), 2, GL11.GL_FLOAT, false, 0, 0); // Attribute 1 - 2 floats per attribute
		
		// Bind vertices buffer for normal data
		int VBONormals = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBONormals);
		
		// Fill vertices buffer
		fBuffer = BufferUtil.storeDataInFloatBuffer(norData);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuffer, GL15.GL_STATIC_DRAW);
		
		// Map buffer data
		GL20.glVertexAttribPointer(ShaderAttribute.NORMAL.getLocation(), 3, GL11.GL_FLOAT, false, 0, 0); // Attribute 2 - 3 floats per attribute
		
		// Bind vertices buffer for tangent data
		int VBOTangents = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTangents);
		
		// Fill vertices buffer
		fBuffer = BufferUtil.storeDataInFloatBuffer(tanData);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuffer, GL15.GL_STATIC_DRAW);
		
		// Map buffer data
		GL20.glVertexAttribPointer(ShaderAttribute.TANGENT.getLocation(), 3, GL11.GL_FLOAT, false, 0, 0); // Attribute 3 - 4 floats per attribute
		
		// Unbind vertices buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		// Unbind VAO
		GL30.glBindVertexArray(0);
		
		int[] vbos = {IBO, VBOPositions, VBOTextureCoords, VBONormals, VBOTangents};
		
		MeshData meshData = new MeshData(loadedMeshData.size(), vao, vbos, indices.length);
		loadedMeshData.add(meshData);
		return meshData;
	}
	
	public static MeshData getMeshData(int meshDataId) throws IllegalArgumentException {
		for(MeshData m: loadedMeshData)
			if(meshDataId == m.getId())
				return m;
		
		throw new IllegalArgumentException("No loaded mesh data with id: " + meshDataId + "!");
	}
	
	/**
	 * Will unbind VAOs and VBOs associated with the mesh data.
	 * @param id - Id of mesh data.
	 */
	public static void freeMeshData(int id){
		loadedMeshData.get(id).free();
	}
	
	/**
	 * Remove all model data loaded.
	 */
	public static void cleanUp(){
		for(MeshData meshData: loadedMeshData)
			meshData.free();
		
		for(int i = 0; i < loadedMeshData.size(); i++)
			loadedMeshData.remove(i);
		
		System.out.println("ModelLoader cleaned up!");
	}
}
