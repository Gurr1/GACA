package hills.services.debug;

import hills.services.IService;
import hills.util.display.FrameBuffer;

import org.lwjgl.opengl.GL11;

public class DebugService implements IService {
	
	// FPS debug variables
	/**
	 * If FPS debug should be activated
	 */
	private boolean fpsDebugMode = false;			
	
	/**
	 * Amount of FPS passed last second
	 */
	private int fps = 0;
	
	/**
	 * Amount of time passed since FPS text was updated
	 */
	private float timePassedSinceFPSTextUpdate = 0;
	
	// Wireframe mode variables
	/**
	 * If wireframe mode is activated
	 */
	private boolean wireframeMode = false;
	
	// Culling mode variables
	/**
	 * If culling mode is activated
	 */
	private boolean cullingMode = false;			
	
	// TODO Move
//	private DebugService() {
//		super(1.0f, false, 0.0f);
//	}
	
	// TODO Move
	protected void update(double delta) {
//		if(PlayerControllerKeyboard.isPressed(GLFW.GLFW_KEY_F1))
//			setWireframeMode(!wireframeMode);
//		
//		if(PlayerControllerKeyboard.isPressed(GLFW.GLFW_KEY_F2))
//			setCullingMode(!cullingMode);
		
		if(fpsDebugMode)
			timePassedSinceFPSTextUpdate += delta; // Update last text update timer
	
		if(fpsDebugMode){
			fps++; // Increment FPS for current render cycle
			
			if(timePassedSinceFPSTextUpdate >= 1.0f){ // If 1 second has passed
				// TODO Fix new function calls DisplayService.displayFPS(fps);              // Display FPS in window title bar
				fps = 0;                              // Reset FPS counter
				timePassedSinceFPSTextUpdate -= 1.0f; // Remove 1 second from last text update
			}
		}
	}
	
	/**
	 * Activates / Deactivates FPS debug mode.<br>
	 * While in FPS debug mode FPS is displayed in window title bar.
	 * @param mode - On or off.
	 */
	public void setFPSDebugMode(boolean mode){
		fpsDebugMode = mode;
	}
	
	protected void setWireframeMode(boolean mode){
		if(mode)
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		else
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		wireframeMode = mode;
	}
	
	protected void setCullingMode(boolean mode){
		if(mode)
			FrameBuffer.enableCulling();
		else
			FrameBuffer.disableCulling();
		
		cullingMode = mode;
	}
	
	public void cleanUp() {
		System.out.println("DebugSystem cleaned up!");
	}

}
