package hills.services.display;

import hills.services.Service;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @Author Gustav Engsmyre, Anton Annlöv
 */
public final class DisplayService implements Service, DisplayServiceI {

	// GLFW callback's
	
	// Error callback's
	private GLFWErrorCallback errorCallback;

	// Input callback's
	private GLFWKeyCallbackI keyCallback;
	private GLFWCharCallback charCallback;
	private GLFWCharModsCallback charModsCallback;
	private GLFWMouseButtonCallbackI mouseButtonCallback;
	private GLFWCursorPosCallbackI cursorPosCallback;
	private GLFWCursorEnterCallback cursorEnterCallback;
	private GLFWScrollCallback scrollCallback;
	private GLFWDropCallback dropCallback;
	// NOT INCLUDED YET! private static GLFWJoystickCallback joystickCallback;

	// Monitor callback's
	private GLFWMonitorCallback monitorCallback;

	// Window callback's
	private GLFWWindowPosCallback winPosCallback;
	private GLFWWindowSizeCallback winSizeCallback;
	private GLFWWindowCloseCallback winCloseCallback;
	private GLFWWindowRefreshCallback winRefreshCallback;
	private GLFWWindowFocusCallback winFocusCallback;
	private GLFWWindowIconifyCallback winIconifyCallback;
	private GLFWFramebufferSizeCallback framebufferSizeCallback;

	// Window handle
	private long HANDLE = NULL;

	// State variables
	private boolean created = false; // If window has been successfully
											// created
	private boolean vsync = false; // VSync default off
	private boolean mouseCaptured = false; // Mouse captured default
													// false
	private String title; // Title of window

	protected DisplayService() {}

	/**
	 * Create new GLFW window. <br>
	 * OBS! Error callback should be set before calling this method using
	 * setErrorCallback(GLFWErrorCallback callback).
	 * 
	 * @param width
	 *            - The desired width, in screen coordinates, of the window
	 * @param height
	 *            - The desired height, in screen coordinates, of the window
	 * @param title
	 *            - Initial, UTF-8 encoded window title
	 */
	public void create(int width, int height, String title){
		create(width, height, title, NULL, NULL);
	}
	
	/**
	 * Creates new GLFW window and sets up input handling with Keyboard and Mouse classes <br>
	 * OBS! Error callback should be set before calling this method<br>
	 * OBS! This method should only be called once unless GLFW is terminated.
	 * using setErrorCallback(GLFWErrorCallback callback).
	 * 
	 * @param width
	 *            - The desired width, in screen coordinates, of the window
	 * @param height
	 *            - The desired height, in screen coordinates, of the window
	 * @param title
	 *            - Initial, UTF-8 encoded window title
	 * @param monitor
	 *            - The monitor to use for full screen mode, or NULL to use
	 *            windowed mode
	 * @param share
	 *            - The window whose context to share resources with, or NULL to
	 *            not share resources
	 *            */
	public void create(int width, int height, String title, long monitor, long share) {
		if (created) {
			System.err.println("Display has already been created!");
			return;
		}

		if (errorCallback == null)
			System.err.println("GLFWErrorCallback has not been set!");

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		// Create new GLFW window and store window handle in HANDLE
		HANDLE = GLFW.glfwCreateWindow(width, height, title, monitor, share);
		if(HANDLE == NULL)
			throw new RuntimeException("Failed to create GLFW window");
		
		// If windowed mode get primary monitor resolution and center window
		if(monitor == NULL){
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(HANDLE, (vidMode.width() - width) / 2,
					(vidMode.height() - height) / 2);
		}
		
		// Make OpenGL context current
		glfwMakeContextCurrent(HANDLE);
		
		//V-sync | 0 - Disabled (May result in screen tearing) | 1 - Enabled (Max FPS = Monitor refresh rate)
		glfwSwapInterval(vsync ? 1 : 0);
		
		// Make window visible
		glfwShowWindow(HANDLE);
		
		// No OpenGL calls before this!
		GL.createCapabilities();
		
		// Set GLViewport to cover the new window
		GL11.glViewport(0, 0, width, height);
		
		
		this.title = title;
		created = true;
	}
	
	/**
	 * Set window title.
	 * @param title - Title of window.
	 */
	public void setTitle(String title){
		this.title = title;
		GLFW.glfwSetWindowTitle(HANDLE, title);
	}
	
	/**
	 * Update window. Swap draw buffers and poll for window events.
	 */
	public void update() {
		glfwSwapBuffers(HANDLE); // Swap draw buffers
		glfwPollEvents(); // Poll for window events. keyCallback "invoke" will
							// be called
	}
	
	/**
	 * Capture/Release mouse.
	 * @param capture - Capture/Release.
	 */
	public void captureMouse(boolean capture){
		if(capture)
			glfwSetInputMode(HANDLE, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		else
			glfwSetInputMode(HANDLE, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		
		mouseCaptured = capture;
	}
	
	/**
	 * Will enable/disable VSync making a call to glfwSwapInterval(int).
	 * @param vsync - VSync on or off.
	 */
	public void enableVSync(boolean vsync){
		this.vsync = vsync;
		glfwSwapInterval(vsync ? 1 : 0);
	}
	
	/**
	 * Resize window.
	 * @param width - New width of window.
	 * @param height  - New height of window.
	 */
	public void resize(int width, int height){
		GLFW.glfwSetWindowSize(HANDLE, width, height);
	}
	
	public boolean isMouseCaptured(){
		return mouseCaptured;
	}
	
	public void setErrorCallback(GLFWErrorCallback errorCallback) {
		this.errorCallback = errorCallback;
		GLFWErrorCallback callback = glfwSetErrorCallback(this.errorCallback);
		if(callback != null)
			callback.free();
	}

	public void setKeyCallback(GLFWKeyCallbackI keyCallback) {
		this.keyCallback = keyCallback;
		GLFWKeyCallback callback = glfwSetKeyCallback(HANDLE, this.keyCallback);
		if(callback != null)
			callback.free();
	}

	public void setCharCallback(GLFWCharCallback charCallback) {
		this.charCallback = charCallback;
		GLFWCharCallback callback = glfwSetCharCallback(HANDLE, this.charCallback);
		if(callback != null)
			callback.free();
	}

	public  void setCharModsCallback(GLFWCharModsCallback charModsCallback) {
		this.charModsCallback = charModsCallback;
		GLFWCharModsCallback callback = glfwSetCharModsCallback(HANDLE, this.charModsCallback);
		if(callback != null)
			callback.free();
	}

	public  void setMouseButtonCallback(
			GLFWMouseButtonCallbackI mouseButtonCallback) {
		this.mouseButtonCallback = mouseButtonCallback;
		GLFWMouseButtonCallback callback = glfwSetMouseButtonCallback(HANDLE, this.mouseButtonCallback);
		if(callback != null)
			callback.free();
	}

	public  void setCursorPosCallback(GLFWCursorPosCallbackI cursorPosCallback) {
		this.cursorPosCallback = cursorPosCallback;
		GLFWCursorPosCallback callback = glfwSetCursorPosCallback(HANDLE, this.cursorPosCallback);
		if(callback != null)
			callback.free();
	}

	public  void setCursorEnterCallback(
			GLFWCursorEnterCallback cursorEnterCallback) {
		this.cursorEnterCallback = cursorEnterCallback;
		GLFWCursorEnterCallback callback = glfwSetCursorEnterCallback(HANDLE, this.cursorEnterCallback);
		if(callback != null)
			callback.free();
	}

	public  void setScrollCallback(GLFWScrollCallback scrollCallback) {
		this.scrollCallback = scrollCallback;
		GLFWScrollCallback callback = glfwSetScrollCallback(HANDLE, this.scrollCallback);
		if(callback != null)
			callback.free();
	}

	public  void setDropCallback(GLFWDropCallback dropCallback) {
		this.dropCallback = dropCallback;
		GLFWDropCallback callback = glfwSetDropCallback(HANDLE, this.dropCallback);
		if(callback != null)
			callback.free();
	}

	public  void setMonitorCallback(GLFWMonitorCallback monitorCallback) {
		this.monitorCallback = monitorCallback;
		GLFWMonitorCallback callback = glfwSetMonitorCallback(this.monitorCallback);
		if(callback != null)
			callback.free();
	}

	public  void setWindowPosCallback(GLFWWindowPosCallback winPosCallback) {
		this.winPosCallback = winPosCallback;
		GLFWWindowPosCallback callback = glfwSetWindowPosCallback(HANDLE, this.winPosCallback);
		if(callback != null)
			callback.free();
	}

	public  void setWindowSizeCallback(GLFWWindowSizeCallback winSizeCallback) {
		this.winSizeCallback = winSizeCallback;
		GLFWWindowSizeCallback callback = glfwSetWindowSizeCallback(HANDLE, this.winSizeCallback);
		if(callback != null)
			callback.free();
	}

	public  void setWindowCloseCallback(GLFWWindowCloseCallback winCloseCallback) {
		this.winCloseCallback = winCloseCallback;
		GLFWWindowCloseCallback callback = glfwSetWindowCloseCallback(HANDLE, this.winCloseCallback);
		if(callback != null)
			callback.free();
	}

	public  void setWindowRefreshCallback(
			GLFWWindowRefreshCallback winRefreshCallback) {
		this.winRefreshCallback = winRefreshCallback;
		GLFWWindowRefreshCallback callback = glfwSetWindowRefreshCallback(HANDLE, this.winRefreshCallback);
		if(callback != null)
			callback.free();
	}

	public  void setWindowFocusCallback(GLFWWindowFocusCallback winFocusCallback) {
		this.winFocusCallback = winFocusCallback;
		GLFWWindowFocusCallback callback = glfwSetWindowFocusCallback(HANDLE, this.winFocusCallback);
		if(callback != null)
			callback.free();
	}

	public  void setWindowIconifyCallback(
			GLFWWindowIconifyCallback winIconifyCallback) {
		this.winIconifyCallback = winIconifyCallback;
		GLFWWindowIconifyCallback callback = glfwSetWindowIconifyCallback(HANDLE, this.winIconifyCallback);
		if(callback != null)
			callback.free();
	}

	public  void setFramebufferSizeCallback(
			GLFWFramebufferSizeCallback framebufferSizeCallback) {
		this.framebufferSizeCallback = framebufferSizeCallback;
		GLFWFramebufferSizeCallback callback = glfwSetFramebufferSizeCallback(HANDLE, this.framebufferSizeCallback);
		if(callback != null)
			callback.free();
	}
	
	public  String getTitle(){
		return title;
	}
	
	/**
	 * @return Window width
	 */
	public  int getWidth(){
		IntBuffer width = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetFramebufferSize(HANDLE, width, null);
		System.out.println("DEBUG: " + width.get(0));

		return width.get(0);
	}
	
	/**
	 * @return Window height
	 */
	public  int getHeight(){
		IntBuffer height = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetFramebufferSize(HANDLE, null, height);
		System.out.println("DEBUG: " + height.get(0));
		return height.get(0);
	}
	
	// TODO Add video mode customization
	
	/**
	 * Get all monitor handles
	 * @return Connected monitor handles
	 */
	public  long[] getMonitors(){
		PointerBuffer buffer = glfwGetMonitors();
		long[] handles = new long[buffer.limit()];
		for(int i = 0; i < handles.length; i++)
			handles[i] = buffer.get();
		
		return handles;
	}
	
	/**
	 * Get window ratio.
	 * @return Window ratio. (height / width)
	 */
	public  double getWindowRatio(){
		return (double) getHeight() / (double) getWidth();
	}
	
	/**
	 * Get monitor ratio.
	 * @return Monitor ratio. (height / width)
	 */
	public  double getMonitorRatio(long monitorHandle){
		GLFWVidMode vidMode = glfwGetVideoMode(monitorHandle);
		return (double) vidMode.height() / (double) vidMode.width();
	}
	
	/**
	 * @return True if Display call to create(int, int, String, long, long) has been successful.
	 */
	public  boolean hasBeenCreated(){
		return created;
	}

	/** 
	 * Releases all callback's, destroys window and terminates GLFW
	 */
	public  void destroy(){
		try {
			if(errorCallback != null)
				errorCallback.free();
			
			setKeyCallback(null);
			setCharCallback(null);
			setCharModsCallback(null);
			setMouseButtonCallback(null);
			setCursorPosCallback(null);
			setCursorEnterCallback(null);
			setScrollCallback(null);
			setDropCallback(null);
			setMonitorCallback(null);
			setWindowPosCallback(null);
			setWindowSizeCallback(null);
			setWindowCloseCallback(null);
			setWindowRefreshCallback(null);
			setWindowFocusCallback(null);
			setWindowIconifyCallback(null);
			setFramebufferSizeCallback(null);
			
			glfwDestroyWindow(HANDLE);
		} finally {
			Callbacks.glfwFreeCallbacks(HANDLE);
			glfwTerminate();
			
			created = false;
		}
	}
	
	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}
	
	// DEBUG AREA //
	
	// Debug functions 
	
	/**
	 * <b> DEBUG FUNCTION </b><br>
	 * Displays window title to title + FPS. Display.title will not contain FPS.
	 * @param fps - Amount of FPS to display.
	 */
	public  void displayFPS(int fps){
		GLFW.glfwSetWindowTitle(HANDLE, title + " | FPS: " + fps);
	}
}
