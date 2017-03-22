package hills.Anton;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.opengl.GL11;

import hills.Anton.engine.GameLoop;
import hills.Anton.engine.display.Display;
import hills.Anton.engine.display.AspectRatios;
import hills.Anton.engine.system.debug.DebugSystem;

/**
 * Created by gustav on 2017-03-21.
 */
public class Init {
	
	/**
	 * Window title.
	 */
	private static final String TITLE = "Hills";
	
	/**
	 * Window initial width.
	 */
	private static final int WIDTH = 720;
	
	/**
	 * Window initial height.
	 */
	private static final int HEIGHT = (int) (WIDTH * AspectRatios.SIXTEEN_TO_NINE);
	
	public void init(){
		Display.setErrorCallback(GLFWErrorCallback.createPrint(System.err));
		Display.create(WIDTH, HEIGHT, TITLE);
		initDisplayCallbacks();
		
		Display.setClearColor(1.0f, 0.2f, 0.2f, 1.0f);	// Set clear color
		Display.enableDepthTesting(0.0f, 1.0f);			// Enable depth testing
		Display.setClearDepth(1.0f);					// Clear depth buffer to 1.0
		
		DebugSystem.createInstance();					// Create DebugSystem instance
		DebugSystem.getInstance().setFPSDebugMode(true);// Activate FPS debug mode
		
		GameLoop.start();                            	// Start engine game loop
		
		Display.destroy();                           	// Terminate GLFW window and GLFW when program ends
	
	}
	
	/**
	 * Initialize GLFW callback methods.
	 */
	public void initDisplayCallbacks(){
		// Window close callback
		Display.setWindowCloseCallback(new GLFWWindowCloseCallback(){
			public void invoke(long window) {
				GameLoop.stop();
			}
		});
		// Frame buffer resize callback
		Display.setFramebufferSizeCallback(new GLFWFramebufferSizeCallback(){
			public void invoke(long window, int width, int height) {
				GL11.glViewport(0, 0, width, height);
				
				// TODO Set perspective matrix according to new width and height
			}
		});
	}
	
}
