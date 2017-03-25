package hills.Anton.engine.renderer.shader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.system.MemoryUtil;

import hills.Anton.engine.renderer.shader.ShaderProgram;
import hills.Anton.engine.math.Mat4;
import hills.Anton.engine.math.Vec3;
import hills.Anton.engine.renderer.shader.SamplerUniform;
import hills.Anton.engine.util.BufferUtil;
import hills.Anton.engine.renderer.shader.ShaderAttribute;
import hills.Anton.engine.renderer.shader.UniformBuffer;
import hills.Anton.engine.loader.ShaderLoader;

public enum ShaderProgram {
	
	STATIC("static.ver", "static.fra", null, null);
	
	/**
	 * Map of all uniform buffer objects.
	 */
	static final class UBOs {
		static final Map<String, UniformBuffer> map = new HashMap<String, UniformBuffer>();
	}
	
	/**
	 * Map of all uniform locations in this shader.
	 */
	private Map<String, Integer> uniforms = new HashMap<String, Integer>();
	
	
	/**
	 * Shader program handle.
	 */
	private final int handle;
	
	private ShaderProgram(String vertexPath, String fragmentPath, String geometryPath, String tessellationPath){
		if(vertexPath == null || fragmentPath == null)
			throw new IllegalArgumentException("A shader program must have a vertex and fragment shader!");
		
		int vertexShader = ShaderLoader.load(vertexPath, GL20.GL_VERTEX_SHADER);
		int fragmentShader = ShaderLoader.load(fragmentPath, GL20.GL_FRAGMENT_SHADER);
		int geometryShader = 0;
		int tessellationShader = 0;
		
		if(geometryPath != null)
			geometryShader = 0; //TODO Add geometry shader support
		
		if(tessellationPath != null)
			tessellationShader = 0; //TODO Add tessellation shader support
		
		// Create a new shader program
		handle = GL20.glCreateProgram();
				
		// Attach shaders to the program
		GL20.glAttachShader(handle, vertexShader);
		GL20.glAttachShader(handle, fragmentShader);
		
		if(geometryShader != 0)
			GL20.glAttachShader(handle, geometryShader);
		
		if(tessellationShader != 0)
			GL20.glAttachShader(handle, tessellationShader);
		
		// Bind attribute locations
		bindAttributes();
				
		// Link program
		GL20.glLinkProgram(handle);
		if(GL20.glGetProgrami(handle, GL20.GL_LINK_STATUS) == GL_FALSE){
			System.err.println("Error! Unable to link shader program:");
			System.err.println(glGetProgramInfoLog(handle));
		}
		
		// Delete shaders
		GL20.glDetachShader(handle, vertexShader);
		GL20.glDeleteShader(vertexShader);
		
		GL20.glDetachShader(handle, fragmentShader);
		GL20.glDeleteShader(fragmentShader);
		
		if(geometryShader != 0){
			GL20.glDetachShader(handle, geometryShader);
			GL20.glDeleteShader(geometryShader);
		}
		
		if(tessellationShader != 0){
			GL20.glDetachShader(handle, tessellationShader);
			GL20.glDeleteShader(tessellationShader);
		}
		
		// Set uniform buffer bindings
		mapUniformBlocks();
		
		// Bind texture uniforms.
		bindTextureUniforms();
		
		// Map uniforms
		mapUniforms();
	}
	
	/**
	 * Bind attribute locations.
	 */
	private void bindAttributes(){
		for(ShaderAttribute attrib: ShaderAttribute.values())
			GL20.glBindAttribLocation(handle, attrib.getLocation(), attrib.getName());
	}
	
	/**
	 * Set uniform buffer bindings. (std140 layout)
	 */
	private void mapUniformBlocks(){
		// Get number of uniform blocks in shader
		int uniformBlocks = GL20.glGetProgrami(handle, GL31.GL_ACTIVE_UNIFORM_BLOCKS);
		
		for(int blockIndex = 0; blockIndex < uniformBlocks; blockIndex++){
			// Get name of uniform block
			String name = GL31.glGetActiveUniformBlockName(handle, blockIndex);
			
			// Skip uniform blocks already created
			if(ShaderProgram.UBOs.map.get(name) != null){
				GL31.glUniformBlockBinding(handle, blockIndex, ShaderProgram.UBOs.map.get(name).getBindPoint());
				continue;
			}
			
			// Get size of uniform block
			int size = GL31.glGetActiveUniformBlocki(handle, blockIndex, GL31.GL_UNIFORM_BLOCK_DATA_SIZE);
			
			// Get number of variables in uniform block
			int variables = GL31.glGetActiveUniformBlocki(handle, blockIndex, GL31.GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS);
			
			// Get indices of variables in uniform block
			IntBuffer variableIndices = MemoryUtil.memAllocInt(variables);
			GL31.glGetActiveUniformBlockiv(handle, blockIndex, GL31.GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES, variableIndices);
			
			// Create arrays and store variable names and offsets
			String[] variableNames = new String[variables];
			int[] variableOffsets = new int[variables];
			
			for(int variable = 0; variable < variables; variable++){
				variableNames[variable] = GL31.glGetActiveUniformName(handle, variableIndices.get(variable));
				variableOffsets[variable] = GL31.glGetActiveUniformsi(handle, variableIndices.get(variable), GL31.GL_UNIFORM_OFFSET);
			}
			
			// Free variable indices buffer
			MemoryUtil.memFree(variableIndices);
			
			// Create binding point
			GL31.glUniformBlockBinding(handle, blockIndex, blockIndex);
			
			// Map new uniform buffer object
			ShaderProgram.UBOs.map.put(name, new UniformBuffer(size, variableNames, variableOffsets));
		}
	}
	
	/**
	 * Bind texture uniforms if existent.
	 */
	private void bindTextureUniforms(){
		enable();
		
		int location;
		for(SamplerUniform sampler: SamplerUniform.values())
			if((location = GL20.glGetUniformLocation(handle, sampler.getName())) != -1)
				loadInt(location, sampler.getSlot());
		
		disable();
	}
	
	/**
	 * Map uniforms in shader program not associated to a uniform buffer block or a sampler.
	 */
	private void mapUniforms(){
		IntBuffer size = MemoryUtil.memAllocInt(1);
		IntBuffer type = MemoryUtil.memAllocInt(1);
		
		int count = GL20.glGetProgrami(handle, GL20.GL_ACTIVE_UNIFORMS);
		for (int i = 0; i < count; i++){
			String name = GL20.glGetActiveUniform(handle, i, size, type);
			if(!(isSampler(name) || isUniformBufferVariable(name)))
				uniforms.put(name, GL20.glGetUniformLocation(handle, name));
		}
		
		MemoryUtil.memFree(size);
		MemoryUtil.memFree(type);
	}
	
	/**
	 * @param name - Name of sampler to look for.
	 * @return True if sampler with specified name exists in this shader program.
	 */
	public boolean isSampler(String name){
		for(SamplerUniform sampler: SamplerUniform.values())
			if(name.equals(sampler.getName()))
				return true;
		
		return false;
	}
	
	/**
	 * @param name - Name of UBO to look for.
	 * @return True if UBO with specified name already exists
	 */
	public boolean isUniformBufferVariable(String name){
		for (Entry<String, UniformBuffer> entry : ShaderProgram.UBOs.map.entrySet())
		    for(String varName: entry.getValue().getVariableNames()) 
		    	if(name.equals(varName))
		    		return true;
		
		return false;
	}
	
	/**
	 * Activates this shader program for rendering.
	 */
	public void enable(){
		GL20.glUseProgram(handle);
	}
	
	/**
	 * Disables this shader programs. (Sets active shader program to '0' AKA no shader program active).
	 */
	public void disable(){
		GL20.glUseProgram(0);
	}
	
	/**
	 * Load shader uniform with a float.
	 * @param location - Location of uniform to load to.
	 * @param value - Value to load uniform with.
	 */
	public void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}
	
	/**
	 * Load shader uniform with an integer.
	 * @param location - Location of uniform to load to.
	 * @param value - Value to load uniform with.
	 */
	public void loadInt(int location, int value){
		GL20.glUniform1i(location, value);
	}
	
	/**
	 * Load shader uniform with a vector.
	 * @param location - Location of uniform to load to.
	 * @param value - Value to load uniform with.
	 */
	public void loadVector(int location, Vec3 vector){
		GL20.glUniform3f(location, vector.getX(), vector.getY(), vector.getZ());
	}
	
	/**
	 * Load shader uniform with a boolean.
	 * @param location - Location of uniform to load to.
	 * @param value - Value to load uniform with.
	 */
	public void loadBoolean(int location, boolean value){
		GL20.glUniform1f(location, value ? 1.0f : 0.0f);
	}
	
	/**
	 * Load shader uniform with a matrix.
	 * @param location - Location of uniform to load to.
	 * @param value - Value to load uniform with.
	 */
	public void loadMatrix(int location, Mat4 matrix){
		GL20.glUniformMatrix4fv(location, false, BufferUtil.storeDataInFloatBuffer(matrix.getValues()));
	}

	/**
	 * Map new values to UBO.
	 * @param uboName - Name of UBO.
	 * @param values - Values to map to the UBO.
	 */
	public static void map(String uboName, byte[] values){
		ShaderProgram.UBOs.map.get(uboName).map(values);
	}
	
	/**
	 * Map new values to UBO variable.
	 * @param uboName - Name of UBO.
	 * @param varName - Name of variable.
	 * @param values - Values to map to the UBO.
	 */
	public static void map(String uboName, String varName, byte[] values){
		UniformBuffer ubo = ShaderProgram.UBOs.map.get(uboName);
		ubo.map(ubo.getOffset(varName), values);
	}
	
	/**
	 * Get uniform handle/location. Will throw IllegalArgumentException if the uniform doesn't exist.
	 * @param name - Name of uniform.
	 * @return Uniform handle/location.
	 */
	public int getUniformLocation(String name) {
		Integer location = uniforms.get(name);
		if(location == null)
			throw new IllegalArgumentException("No uniform " + name);
		
		return location;
	}
	
	/**
	 * Deletes this shader program. Should be called before the application terminates.
	 */
	private void delete(){
		disable();
		GL20.glDeleteProgram(handle);
	}
	
	/**
	 * Called from cleanUp in engine game loop after loop has terminated.
	 * Deletes all shader programs and uniform buffer objects.
	 */
	public static void cleanUp(){
		for(ShaderProgram program: values())
			program.delete();
		
		for (Entry<String, UniformBuffer> entry : ShaderProgram.UBOs.map.entrySet())
		     entry.getValue().delete();
		ShaderProgram.UBOs.map.clear();
	}
}
