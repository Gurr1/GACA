package hills;

import hills.controller.ServiceMediator;
import hills.services.debug.DebugService;
import hills.services.generation.MapFactory;
import hills.services.generation.IMapFactory;
import hills.util.display.AspectRatios;
import hills.util.display.Display;
import hills.util.display.FrameBuffer;
import hills.view.CameraModel;
import hills.services.terrain.TerrainService;
import hills.controller.GameLoop;
import hills.controller.manager.GameManager;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.opengl.GL11;

import java.util.Random;

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
	
	// Absolute first thing that gets done. (Hacky?)
	// Initialize GLFW, OpenGL and create a new window.
	// This allows OpenGL calls when initializing static final variables such as shader programs.

	public void init(){
		//TerrainNormalMapCreator.createSmoothNormals("height_map_test_3.png");

		//TerrainNormalMapCreator.createFlatNormals("height_map_test_3.png");
		Random rand = new Random();


		System.setProperty("org.lwjgl.util.Debug", "true");
		System.setProperty("org.lwjgl.util.DebugAllocator", "true");
		
		Display.setErrorCallback(GLFWErrorCallback.createPrint(System.err));
		Display.create(WIDTH, HEIGHT, TITLE);

		FrameBuffer.setClearColor(0.55f, 0.55f, 1.0f, 1.0f);	// Set clear color
		FrameBuffer.enableDepthTesting(0.0f, 1.0f);				// Enable depth testing
		FrameBuffer.setClearDepth(1.0f);						// Clear depth buffer to 1.0
		
		FrameBuffer.setDepthFunction(GL11.GL_LEQUAL);		// Set OpenGL depth function.
		
//		DebugService.createInstance();						// Create DebugSystem instance
//		DebugService.getInstance().setFPSDebugMode(true);	// Activate FPS debug mode
		CameraModel.createInstance(1.0f, false, 0.0f);
		ServiceMediator.INSTANCE.generateMap();
				CameraModel cameraModel = CameraModel.getInstance();// Get the CameraSystem instance
		cameraModel.updatePerspective(0.1f, 3000.0f, (float) Display.getWidth() / (float) Display.getHeight(), 70.0f);	// Update the perspective matrix
		initDisplayCallbacks();
		
		//TerrainService.createInstance();						// Create TerrainSystem instance
		GameManager.createInstance(1.0f, false, 0.0f);		// Create GameSystem instance
		GameLoop.start();                            		// Start engine game loop
		Display.destroy();                           		// Terminate GLFW window and GLFW when program ends
	}
	
	/**
	 * Initialize GLFW callback methods.
	 */
	public void initDisplayCallbacks(){
		CameraModel cameraSystem = CameraModel.getInstance();
		
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
				
				// Set perspective matrix according to new width and height
				cameraSystem.updatePerspective(0.1f, 3000.0f, (float) Display.getWidth() / (float) Display.getHeight(), 70.0f);
			}
		});
	}
	
}
