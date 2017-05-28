package hills.util.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class ShaderLoader {

	public static final String SHADER_DIRECTORY = "/shaders/";
	
	/**
	 * Load shader from source file. Will print error and terminate application if 
	 * file not found or shader compilation fails.
	 * @param fileName - Source file name located in shaders folder.
	 * @param type - Type of shader. (GL_VERTEX_SHADER, GL_FRAGMENT_SHADER, GL_GEOMETRY_SHADER, GL_TESSELLATION_SHADER) (GL20)
	 * @return Shader handle.
	 */
	public static int load(String fileName, int type){
		StringBuilder source = new StringBuilder(); // String to contain source code
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(new File(ShaderLoader.class.getResource(SHADER_DIRECTORY + fileName).getPath()))); // Reader to read source code from file
			
			String line;
			while((line = reader.readLine()) != null)
				source.append(line).append("\n");
			
			reader.close();
		} catch(IOException e) {
			System.err.println("Error! Could not read shader file: " + fileName);
			e.printStackTrace();
			System.exit(-1);
		}
		
		// Create and compile shader
		int shader = glCreateShader(type);
		glShaderSource(shader, source);
		glCompileShader(shader);
		
		// Check if compilation was successful
		int status;
		status = glGetShaderi(shader, GL_COMPILE_STATUS);
		if(status == GL_FALSE){
			int infoLogLength;
			infoLogLength = glGetShaderi(shader, GL_INFO_LOG_LENGTH);
			
			String strInfoLog = glGetShaderInfoLog(shader, infoLogLength);
			
			String strShaderType = "";
			switch(type){
				case GL_VERTEX_SHADER: strShaderType = "vertex"; break;
				case GL_GEOMETRY_SHADER: strShaderType = "geometry"; break;
				case GL_FRAGMENT_SHADER: strShaderType = "fragment"; break;
				default: break;
			}
			
			System.err.println("Compile failure in " + strShaderType + " shader:\n" + strInfoLog + "\n");
			System.exit(-1);
		}
		
		return shader;
	}
}
