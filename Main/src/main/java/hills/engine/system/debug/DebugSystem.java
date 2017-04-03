package hills.engine.system.debug;

import hills.engine.display.Display;
import hills.engine.display.FrameBuffer;
import hills.engine.input.Keyboard;
import hills.engine.system.EngineSystem;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public final class DebugSystem extends EngineSystem {

	/** Singleton instance **/
	private static DebugSystem instance = null;
	
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
	
	private DebugSystem() {
		super(1.0f, false, 0.0f);
	}
	
	protected void update(double delta) {
		if(Keyboard.isPressed(GLFW.GLFW_KEY_F1))
			setWireframeMode(!wireframeMode);
		
		if(Keyboard.isPressed(GLFW.GLFW_KEY_F2))
			setCullingMode(!cullingMode);
		
		if(fpsDebugMode)
			timePassedSinceFPSTextUpdate += delta; // Update last text update timer
	}
	
	public void render() {
		
		if(fpsDebugMode){
			fps++; // Increment FPS for current render cycle
			
			if(timePassedSinceFPSTextUpdate >= 1.0f){ // If 1 second has passed
				Display.displayFPS(fps);              // Display FPS in window title bar
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
	
	public void setWireframeMode(boolean mode){
		if(mode)
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		else
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		wireframeMode = mode;
	}
	
	public void setCullingMode(boolean mode){
		if(mode)
			FrameBuffer.enableCulling();
		else
			FrameBuffer.disableCulling();
		
		cullingMode = mode;
	}
	
	public void cleanUp() {
		System.out.println("DebugSystem cleaned up!");
	}
	
	/**
	 * Creates the singleton instance of DebugSystem.
	 * @return False if an instance has already been created.
	 */
	public static boolean createInstance() {
		if(instance != null)
			return false;
		
		instance = new DebugSystem();
		return true;
	}
	
	/**
	 * 
	 * @return The singleton instance of DebugSystem.
	 * @throws NullPointerException If singleton instance has not been created.
	 */
	public static DebugSystem getInstance() throws NullPointerException {
		if(instance == null)
			throw new NullPointerException("Singleton instance not created!");
		
		return instance;
	}

}
