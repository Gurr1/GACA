package hills;

import hills.controller.GameLoop;
import hills.controller.InputControllers.InputMediator;
import hills.controller.manager.CameraManager;
import hills.controller.manager.GameManager;
import hills.controller.manager.TerrainManager;
import hills.services.ServiceLocator;
import hills.services.display.DisplayServiceI;
import hills.util.display.AspectRatios;
import hills.util.display.FrameBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.opengl.GL11;

/**
 * @Author Anton Annlöv, Gustav Engsmyre
 * @RevisedBy Anders Hansson
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
	
    public static void main(String [] args){
    	//TerrainNormalMapCreator.createSmoothNormals("height_map_test_3.png");
    	//TerrainNormalMapCreator.createFlatNormals("height_map_test_3.png");

		System.setProperty("org.lwjgl.util.Debug", "true");
		System.setProperty("org.lwjgl.util.DebugAllocator", "true");

		DisplayServiceI displayService = ServiceLocator.INSTANCE.getDisplayService();
		displayService.setErrorCallback(GLFWErrorCallback.createPrint(System.err));
		displayService.create(WIDTH, HEIGHT, TITLE);
		setDisplayCallbacks(displayService);
    	FrameBuffer.setClearColor(0.55f, 0.55f, 1.0f, 1.0f);		// Set clear color
    	FrameBuffer.enableDepthTesting(0.0f, 1.0f);					// Enable depth testing
    	FrameBuffer.setClearDepth(1.0f);							// Clear depth buffer to 1.0
    	FrameBuffer.setDepthFunction(GL11.GL_LEQUAL);				// Set OpenGL depth function.

    	ServiceLocator.INSTANCE.getTerrainGenerationService().generateWorldImage();

    	GameLoop gameLoop = new GameLoop();							// Create a new game loop

    	initDisplayCallbacks(gameLoop);

    	gameLoop.addSystem(new CameraManager(1.0f, false, 0.0f));	// Add camera controller to loop
    	gameLoop.addSystem(new TerrainManager(1.0f, false, 0.0f));	// Add terrain controller to loop
    	gameLoop.addSystem(new GameManager(1.0f, false, 0.0f));		// Add game controller to loop

    	gameLoop.start();                            				// Start engine game loop

    	ServiceLocator.INSTANCE.getDisplayService().destroy();		// Terminate GLFW window and GLFW when program ends
    }

	private static void setDisplayCallbacks(DisplayServiceI displayService) {
		displayService.setCursorPosCallback(InputMediator.INSTANCE.getCursorPositionCallback());
		displayService.setKeyCallback(InputMediator.INSTANCE.getKeyCallBack());
		displayService.setMouseButtonCallback(InputMediator.INSTANCE.getMouseButtonCallback());

	}

	/**
	 * Initialize GLFW callback methods.
	 */
	public static void initDisplayCallbacks(GameLoop gameLoop){
		DisplayServiceI displayService = ServiceLocator.INSTANCE.getDisplayService();
		
		// Window close callback
		displayService.setWindowCloseCallback(new GLFWWindowCloseCallback(){
			public void invoke(long window) {
				gameLoop.stop();
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
