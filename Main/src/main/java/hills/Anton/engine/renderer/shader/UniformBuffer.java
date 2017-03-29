package hills.Anton.engine.renderer.shader;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

public class UniformBuffer {
	
	/**
	 * Max amount of bind points for uniform buffers.
	 */
	private static final int MAX_BIND_POINTS = GL11.glGetInteger(GL31.GL_MAX_UNIFORM_BUFFER_BINDINGS);
	
	/**
	 * Binding point counter
	 */
	private static int nextBindPoint = 0;
	
	/**
	 * Uniform buffer binding point.
	 */
	private final int bindPoint;
	
	/**
	 * Size of buffer in bytes.
	 */
	private final int size;
	
	/**
	 * Map of name and offset of buffer variable.
	 */
	private final Map<String, Integer> variableOffsets = new HashMap<String, Integer>();
	
	/**
	 * Handle for buffer to store uniform buffer data.
	 */
	private final int handle;
	
	/**
	 * Define a new uniform buffer object.
	 * @param size - Size of the buffer.
	 * @param varNames - Names of the variables inside the buffer object.
	 * @param offsets - Offsets of the variables inside the buffer object.
	 */
	protected UniformBuffer(int size, String[] varNames, int[] offsets){
		if(nextBindPoint >= MAX_BIND_POINTS)
			System.err.println("Max bind points for uniform buffers reached!");
		this.bindPoint = nextBindPoint++;
		this.size =  size;
		
		System.out.println("Bindpoints: " + bindPoint);
		
		for(int variable = 0; variable < varNames.length; variable++){
			variableOffsets.put(varNames[variable], offsets[variable]);
			
			System.out.println("    " + varNames[variable] + " " + offsets[variable]);
			
			// Check for array variables
			int nameEnd = -1;
			if((nameEnd = varNames[variable].indexOf(']')) > 0){
				String arrayVariableName = varNames[variable].substring(0, nameEnd + 1);
				Integer arrayVariableOffset = variableOffsets.get(arrayVariableName);
				if(arrayVariableOffset == null)
					arrayVariableOffset = offsets[variable] + 1; // Make sure offset gets selected from offset array.
				
				variableOffsets.put(arrayVariableName, arrayVariableOffset < offsets[variable] ? arrayVariableOffset : offsets[variable]);
			}
		}
		
		// Generate new uniform buffer handle
		handle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, handle);
		GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, size, GL15.GL_DYNAMIC_DRAW);
			
	}
	
	/**
	 * Map new values to the entire uniform buffer.
	 * @param values - Values to map to buffer.
	 */
	public void map(byte[] values){
		if(values.length != size){
			System.err.println("Values sent to uniform buffer object must be of same size as the buffer! Size of buffer: " + size + " | Size of in values: " + values.length + " | Bind point: " + bindPoint);
			return;
		}
		
		GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, bindPoint, handle);
		GL30.glMapBufferRange(GL31.GL_UNIFORM_BUFFER, 0, values.length, GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_INVALIDATE_BUFFER_BIT).put(values);
		GL15.glUnmapBuffer(GL31.GL_UNIFORM_BUFFER);
	}
	
	/**
	 * Map new values to the entire uniform buffer.
	 * @param values - Values to map to buffer.
	 */
	public void map(ByteBuffer values){
		if(values.remaining() != size){
			System.err.println("Values sent to uniform buffer object must be of same size as the buffer! Size of buffer: " + size + " | Size of in values: " + values.remaining() + " | Bind point: " + bindPoint);
			return;
		}
		
		GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, bindPoint, handle);
		GL30.glMapBufferRange(GL31.GL_UNIFORM_BUFFER, 0, values.remaining(), GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_INVALIDATE_BUFFER_BIT).put(values);
		GL15.glUnmapBuffer(GL31.GL_UNIFORM_BUFFER);
	}
	
	/**
	 * Map new values to a sub-part of the buffer.
	 * @param offset - Start point of mapping in bytes.
	 * @param values - Values to map from start point.
	 */
	public void map(int offset, byte[] values){
		if(offset + values.length > size){
			System.err.println("Values sent to uniform buffer object must be of same size as or smaller than the buffer!" + bindPoint);
			return;
		}
		
		GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, bindPoint, handle);
		GL30.glMapBufferRange(GL31.GL_UNIFORM_BUFFER, offset, values.length, GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_INVALIDATE_RANGE_BIT).put(values);
		GL15.glUnmapBuffer(GL31.GL_UNIFORM_BUFFER);
	}
	
	/**
	 * Map new values to a sub-part of the buffer.
	 * @param offset - Start point of mapping in bytes.
	 * @param values - Values to map from start point.
	 */
	public void map(int offset, ByteBuffer values){
		if(offset + values.remaining() > size){
			System.err.println("Values sent to uniform buffer object must be of same size as or smaller than the buffer!" + bindPoint);
			return;
		}
		
		GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, bindPoint, handle);
		GL30.glMapBufferRange(GL31.GL_UNIFORM_BUFFER, offset, values.remaining(), GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_INVALIDATE_RANGE_BIT).put(values);
		GL15.glUnmapBuffer(GL31.GL_UNIFORM_BUFFER);
	}
	
	/**
	 * @return Uniform buffer object binding point.
	 */
	public int getBindPoint(){
		return bindPoint;
	}
	
	/**
	 * @return Size of uniform buffer in bytes.
	 */
	public int getSize(){
		return size;
	}
	
	/**
	 * Get offset of variable in uniform buffer.
	 * @param varName - Name of variable to get offset of.
	 * @return Variable offset in buffer.
	 */
	public int getOffset(String varName){
		Integer offset = variableOffsets.get(varName);
		if(offset == null)
			throw new IllegalArgumentException("Could not find variable " + varName + " in uniform buffer!");
		
		return offset;
	}
	
	/**
	 * @return variable names.
	 */
	public String[] getVariableNames(){
		Set<String> keySet = variableOffsets.keySet();
		String[] names = new String[keySet.size()];
		
		int index = 0;
		for(String key: keySet)
			names[index++] = key;
		
		return names;
	}
	
	/**
	 * Deletes buffer.
	 */
	public void delete(){
		GL15.glDeleteBuffers(handle);
		System.out.println("Uniform buffer deleted");
	}
}
