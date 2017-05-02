package hills.view.renderer;

import hills.util.math.Mat4;
import hills.util.model.Mesh;
import hills.util.model.MeshData;
import hills.util.model.MeshTexture;
import hills.util.model.Model;
import hills.util.shader.ShaderAttribute;
import hills.util.shader.ShaderProgram;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

public final class ModelRenderer {
	
	/**
	 * Holds all rendering data for next round of rendering.
	 */
	private static Map<ShaderProgram, Map<MeshData, Map<MeshTexture, List<Mat4>>>> batch = new HashMap<ShaderProgram, Map<MeshData, Map<MeshTexture, List<Mat4>>>>();
	
	private ModelRenderer(){} // Private constructor no instances
	
	/**
	 * Add model to render batch. All batched models will be rendered automatically and then removed from batch.
	 * @param program - Shader program to use.
	 * @param model - model to render.
	 * @param transformation - Transformation matrix to use when rendering.
	 */
	public static void batch(ShaderProgram program, Model model, Mat4 transformation){
		Mesh[] meshes = model.getMeshes();
		
		if(!batch.containsKey(program))
			batch.put(program, new HashMap<MeshData, Map<MeshTexture, List<Mat4>>>());
		
		Map<MeshData, Map<MeshTexture, List<Mat4>>> meshDataMap = batch.get(program);
		
		for(Mesh mesh: meshes)
			try	{
				MeshData meshData = mesh.getMeshData();
				if(!meshDataMap.containsKey(meshData))
					meshDataMap.put(meshData, new HashMap<MeshTexture, List<Mat4>>());
				
				Map<MeshTexture, List<Mat4>> meshTextureMap = meshDataMap.get(meshData);
				MeshTexture meshTexture = mesh.getTexture();
				if(!meshTextureMap.containsKey(meshTexture))
					meshTextureMap.put(meshTexture, new ArrayList<Mat4>());
				
				Mat4 renderMatrix = transformation.mul(mesh.getTransformation());
				meshTextureMap.get(meshTexture).add(renderMatrix);
			} catch (Exception e){
				System.err.println("Unable to batch mesh (" + mesh + ") in model (" + model + ")");
				e.printStackTrace();
			}
	}
	
	/**
	 * Clears the current batch list. Should be called every draw loop.
	 */
	public static void clearBatch(){
		// Clear batch
		batch.clear();
	}
	
	/**
	 * OBS! Should only be called from GameLoop!
	 * Renders all batched models (from mesh map).
	 */
	public static void render(){
		// TODO More customized rendering
		
		for(ShaderProgram shaderProgram: batch.keySet()){
			// Activate shader program
			shaderProgram.enable();
			
			Map<MeshData, Map<MeshTexture, List<Mat4>>> meshDataMap = batch.get(shaderProgram);
			for(MeshData meshData: meshDataMap.keySet()){
				// Render mesh data for all models
				// Bind mesh VAO
				GL30.glBindVertexArray(meshData.getVao());
				
				// Enable attributes
				GL20.glEnableVertexAttribArray(ShaderAttribute.POSITION.getLocation());		// Position attribute
				GL20.glEnableVertexAttribArray(ShaderAttribute.TEXTURECOORD.getLocation());	// Texture coordinate attribute
				GL20.glEnableVertexAttribArray(ShaderAttribute.NORMAL.getLocation());		// Normal attribute
				GL20.glEnableVertexAttribArray(ShaderAttribute.TANGENT.getLocation());		// Tangent attribute
				
				Map<MeshTexture, List<Mat4>> meshTextureMap = meshDataMap.get(meshData);
				for(MeshTexture texture: meshTextureMap.keySet()){
					// TODO Render all meshes with same texture after each other
					// Bind mesh texture
					texture.bind();
					
					for(Mat4 transformation: meshTextureMap.get(texture)){
						// Load WORLD matrix to the MODEL uniform buffer.
						try(MemoryStack stack = MemoryStack.stackPush()){
							ByteBuffer dataBuffer = stack.calloc(transformation.get140DataSize());
							transformation.get140Data(dataBuffer);
							dataBuffer.flip();
							
							ShaderProgram.map("MODEL", "WORLD", dataBuffer);
							
							// Render mesh
							GL11.glDrawElements(GL11.GL_TRIANGLES, meshData.getIndicesAmount(), GL11.GL_UNSIGNED_INT, 0);
						}
					}
				}
				
				// Disable attributes
				GL20.glDisableVertexAttribArray(ShaderAttribute.POSITION.getLocation()); 		// Position attribute
				GL20.glDisableVertexAttribArray(ShaderAttribute.TEXTURECOORD.getLocation()); 	// Texture coordinate attribute
				GL20.glDisableVertexAttribArray(ShaderAttribute.NORMAL.getLocation()); 			// Normal attribute
				GL20.glDisableVertexAttribArray(ShaderAttribute.TANGENT.getLocation());			// Tangent attribute
				
				// Unbind mesh VAO
				GL30.glBindVertexArray(0);
			}
			
			// No need to deactivate shader since another shader will replace the current one next.
		}
		
		// Deactivate shaders.
		GL20.glUseProgram(0);
	}
}
