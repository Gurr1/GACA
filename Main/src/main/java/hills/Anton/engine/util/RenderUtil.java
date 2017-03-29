package hills.Anton.engine.util;

import org.lwjgl.opengl.GL11;

public class RenderUtil {

	private RenderUtil(){} // Private constructor no instances
	
	/**
	 * Sets the depth function that OpenGL should use when depth testing.
	 */
	public static void setDepthFunction(int depthFunc){
		GL11.glDepthFunc(depthFunc);
	}
	
	/**
	 * Enables culling on back face.
	 * Front face is CCW.
	 */
	public static void enableCulling(){
		GL11.glFrontFace(GL11.GL_CCW);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	/**
	 * Disables culling.
	 */
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
}
