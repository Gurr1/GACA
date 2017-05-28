package hills.util.loader;

import hills.util.compatibility.BufferUtil;
import hills.util.math.Mat4;
import hills.util.math.Vertex;
import hills.util.model.Mesh;
import hills.util.model.MeshData;
import hills.util.model.MeshTexture;
import hills.util.shader.ShaderAttribute;

import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton
 */
// TODO Move to service
public final class ModelLoader {

	/**
	 * Loaded mesh data. Used to create mesh instances which are used to create
	 * models.
	 */
	// private static List<MeshData> loadedMeshData = new ArrayList<MeshData>();

	/**
	 * Keeps track of all the allocated VAOs created through this loader<br>
	 * and makes sure they are freed when application is closed.
	 */
	private static List<Integer> allocatedVAOs = new ArrayList<Integer>();

	/**
	 * Keeps track of all the allocated VBOs created through this loader<br>
	 * and makes sure they are freed when application is closed.
	 */
	private static List<Integer> allocatedVBOs = new ArrayList<Integer>();

	private ModelLoader() {
	} // Private constructor no instances!

	/**
	 * Creates a new mesh and mesh data from the vertices and indices.
	 * 
	 * @param vertices
	 *            - Vertices of mesh.
	 * @param indices
	 *            - Indices of mesh.
	 * @param texture
	 *            - Mesh texture for mesh.
	 * @param transformation
	 *            - Mesh transformation.
	 * @return New mesh.
	 */
	public static Mesh load(Vertex[] vertices, int[] indices,
			MeshTexture texture, Mat4 transformation) {
		// Get needed data from vertices
		float[] posData = new float[vertices.length * 3]; // 3 position floats
															// per vertex
		float[] texData = new float[vertices.length * 2]; // 2 textureCoord
															// floats per vertex
		float[] norData = new float[vertices.length * 3]; // 3 normal floats per
															// vertex
		float[] tanData = new float[vertices.length * 4]; // 4 tangent floats
															// per vertex

		for (int i = 0; i < vertices.length; i++) {
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

		return new Mesh(loadMeshData(posData, texData, norData, tanData,
				indices), texture, transformation);
	}

	/**
	 * Creates new mesh data and stores it in 'loadedMeshData' for later
	 * freeing.
	 * 
	 * @param posData
	 *            - Vertex positions.
	 * @param texData
	 *            - Texture coordinates.
	 * @param norData
	 *            - Vertex normals.
	 * @param indices
	 *            - Vertex indices.
	 * @return New mesh data.
	 */
	private static MeshData loadMeshData(float[] posData, float[] texData,
			float[] norData, float[] tanData, int[] indices) {
		// Bind vao and fill with VBO:s
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);

		// Bind indices buffer
		int IBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, IBO);

		// Fill indices buffer
		IntBuffer iBuffer = BufferUtil.storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, iBuffer,
				GL15.GL_STATIC_DRAW);

		// Unbind indices buffer
		// GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		// Bind vertices buffer for position data
		int VBOPositions = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOPositions);

		// Fill vertices buffer
		FloatBuffer fBuffer = BufferUtil.storeDataInFloatBuffer(posData);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuffer, GL15.GL_STATIC_DRAW);

		// Map buffer data
		GL20.glVertexAttribPointer(ShaderAttribute.POSITION.getLocation(), 3,
				GL11.GL_FLOAT, false, 0, 0); // Attribute 0 - 3 floats per
												// attribute

		// Bind vertices buffer for texture data
		int VBOTextureCoords = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTextureCoords);

		// Fill vertices buffer
		fBuffer = BufferUtil.storeDataInFloatBuffer(texData);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuffer, GL15.GL_STATIC_DRAW);

		// Map buffer data
		GL20.glVertexAttribPointer(ShaderAttribute.TEXTURECOORD.getLocation(),
				2, GL11.GL_FLOAT, false, 0, 0); // Attribute 1 - 2 floats per
												// attribute

		// Bind vertices buffer for normal data
		int VBONormals = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBONormals);

		// Fill vertices buffer
		fBuffer = BufferUtil.storeDataInFloatBuffer(norData);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuffer, GL15.GL_STATIC_DRAW);

		// Map buffer data
		GL20.glVertexAttribPointer(ShaderAttribute.NORMAL.getLocation(), 3,
				GL11.GL_FLOAT, false, 0, 0); // Attribute 2 - 3 floats per
												// attribute

		// Bind vertices buffer for tangent data
		int VBOTangents = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTangents);

		// Fill vertices buffer
		fBuffer = BufferUtil.storeDataInFloatBuffer(tanData);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuffer, GL15.GL_STATIC_DRAW);

		// Map buffer data
		GL20.glVertexAttribPointer(ShaderAttribute.TANGENT.getLocation(), 3,
				GL11.GL_FLOAT, false, 0, 0); // Attribute 3 - 4 floats per
												// attribute

		// Unbind vertices buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Unbind VAO
		GL30.glBindVertexArray(0);

		allocatedVBOs.add(IBO);
		allocatedVBOs.add(VBOPositions);
		allocatedVBOs.add(VBOTextureCoords);
		allocatedVBOs.add(VBONormals);
		allocatedVBOs.add(VBOTangents);

		allocatedVAOs.add(vao);

		return new MeshData(vao, indices.length);
	}

	/**
	 * Declare a new empty VBO of byte size byteCount.
	 * 
	 * @param target
	 *            - Buffer target. GL_ARRAY_BUFFER, etc.
	 * @param byteCount
	 *            - Size of declared VBO.
	 * @param usage
	 *            - How the VBO will be used STREAM, STATIC or DYNAMIC.
	 * @return The VBO handle
	 */
	public static int createEmptyVBO(int target, long byteCount, int usage) {
		int vbo = GL15.glGenBuffers();
		allocatedVBOs.add(vbo);

		GL15.glBindBuffer(target, vbo);
		GL15.glBufferData(target, byteCount, usage);
		GL15.glBindBuffer(target, 0);

		return vbo;
	}

	/**
	 * Add a vertex attribute pointer to a VAO.
	 * 
	 * @param VAO
	 *            - The VAO to add a vertex attribute pointer to.
	 * @param VBO
	 *            - The VBO with the data that is to be pointed to.
	 * @param attribute
	 *            - The index of the vertex attribute to be modified.
	 * @param dataSize
	 *            - The number of values that are stored in the array per
	 *            vertex. (1, 2, 3 or 4)
	 * @param type
	 *            - The data type of each component in the array. GL_FLOAT, etc.
	 * @param normalize
	 *            - whether fixed-point data values should be normalized or
	 *            converted directly as fixed-point values when they are
	 *            accessed.
	 * @param stride
	 *            - The byte offset between consecutive generic vertex
	 *            attributes. If stride is 0, the generic vertex attributes are
	 *            understood to be tightly packed in the array. The initial
	 *            value is 0.
	 * @param offset
	 *            - The offset of the first component of the first generic
	 *            vertex attribute in the array.
	 * @param attributeDivisor
	 *            - The number of instances that will pass between updates of
	 *            the generic attribute at slot index.
	 */
	public static void addVAOAttribute(int VAO, int VBO, int attribute,
			int dataSize, int type, boolean normalize, int stride, long offset,
			int attributeDivisor) {
		GL30.glBindVertexArray(VAO);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		GL20.glVertexAttribPointer(attribute, dataSize, type, normalize, stride, offset);
		GL33.glVertexAttribDivisor(attribute, attributeDivisor);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	/**
	 * Will delete a previously declared VAO and<br>
	 * also remove it from the list of allocated VAOs if<br>
	 * it's found.
	 * 
	 * @param vao
	 *            - The VAO to delete.
	 */
	public static void deleteVAO(int vao) {
		GL30.glDeleteVertexArrays(vao);

		for (int i = 0; i < allocatedVAOs.size(); i++)
			if (allocatedVAOs.get(i) == vao)
				allocatedVAOs.remove(i);
	}

	/**
	 * Will delete a previously declared VBO and<br>
	 * also remove it from the list of allocated VBOs if<br>
	 * it's found.
	 * 
	 * @param vbo
	 *            - The VBO to delete.
	 */
	public static void deleteVBO(int vbo) {
		GL15.glDeleteBuffers(vbo);

		for (int i = 0; i < allocatedVBOs.size(); i++)
			if (allocatedVBOs.get(i) == vbo)
				allocatedVBOs.remove(i);
	}

	/**
	 * Remove all model data and VBOs loaded.
	 */
	public static void cleanUp() {
		for (Integer vbo : allocatedVBOs)
			GL15.glDeleteBuffers(vbo);
		allocatedVBOs.clear();

		for (Integer vao : allocatedVAOs)
			GL30.glDeleteVertexArrays(vao);
		allocatedVAOs.clear();

		System.out.println("ModelLoader cleaned up!");
	}
}
