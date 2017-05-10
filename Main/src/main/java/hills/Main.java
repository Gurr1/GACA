package hills;

import hills.controller.GameLoop;
import hills.controller.ServiceMediator;
import hills.controller.manager.GameManager;
import hills.services.ServiceLocator;
import hills.services.display.DisplayServiceI;
import hills.util.display.AspectRatios;
import hills.util.display.FrameBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by gustav on 2017-03-21.
 */
public class Main {

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
	
	// Absolute first thing that gets done. (Hacky?)
	// Initialize GLFW, OpenGL and create a new window.
	// This allows OpenGL calls when initializing static final variables such as shader programs.
	static{

		System.setProperty("org.lwjgl.util.Debug", "true");
		System.setProperty("org.lwjgl.util.DebugAllocator", "true");
		System.out.println("launch");
		DisplayServiceI displayService = ServiceLocator.INSTANCE.getDisplayService();
		displayService.setErrorCallback(GLFWErrorCallback.createPrint(System.err));
		displayService.create(WIDTH, HEIGHT, TITLE);
	}
    public static void main(String [] args){
    	//TerrainNormalMapCreator.createSmoothNormals("height_map_test_3.png");

    	//TerrainNormalMapCreator.createFlatNormals("height_map_test_3.png");
    	Random rand = new Random();
		FrameBuffer.setClearColor(0.55f, 0.55f, 1.0f, 1.0f);	// Set clear color
    	FrameBuffer.enableDepthTesting(0.0f, 1.0f);				// Enable depth testing
    	FrameBuffer.setClearDepth(1.0f);						// Clear depth buffer to 1.0
    	FrameBuffer.setDepthFunction(GL11.GL_LEQUAL);		// Set OpenGL depth function.

    	//    			DebugService.createInstance();						// Create DebugSystem instance
    	//    			DebugService.getInstance().setFPSDebugMode(true);	// Activate FPS debug mode
    	//CameraModel.createInstance(1.0f, false, 0.0f);
    	ServiceMediator.INSTANCE.generateMap();
    	//CameraModel cameraModel = CameraModel.getInstance();// Get the CameraSystem instance
    	//cameraModel.updatePerspective(0.1f, 3000.0f, (float) displayService.getWidth() / (float) displayService.getHeight(), 70.0f);	// Update the perspective matrix
    	initDisplayCallbacks();

    	//TerrainService.createInstance();					// Create TerrainSystem instance
    	GameManager.createInstance(1.0f, false, 0.0f);		// Create GameSystem instance
    	GameLoop.start();                            		// Start engine game loop

    	ServiceLocator.INSTANCE.getDisplayService().destroy();                           // Terminate GLFW window and GLFW when program ends

    }
	
	/**
	 * Initialize GLFW callback methods.
	 */
	public static void initDisplayCallbacks(){
		DisplayServiceI displayService = ServiceLocator.INSTANCE.getDisplayService();
		
		// Window close callback
		displayService.setWindowCloseCallback(new GLFWWindowCloseCallback(){
			public void invoke(long window) {
				GameLoop.stop();
			}
		});
		
		// Frame buffer resize callback
		displayService.setFramebufferSizeCallback(new GLFWFramebufferSizeCallback(){
			public void invoke(long window, int width, int height) {
				GL11.glViewport(0, 0, width, height);
				
				// Set perspective matrix according to new width and height
				ServiceLocator.INSTANCE.getCameraDataService().setPerspective(0.1f, 3000.0f, (float) displayService.getWidth() / (float) displayService.getHeight(), 70.0f);
				ServiceLocator.INSTANCE.getCameraUpdateService().updateGPUPerspectiveMatrix();
			}
		});
	}

}
