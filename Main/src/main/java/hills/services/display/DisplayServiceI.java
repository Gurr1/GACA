package hills.services.display;

import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCharModsCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowRefreshCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

/**
 * @author Anton
 */
public interface DisplayServiceI {
	
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
	public void create(int width, int height, String title);
	
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
	public void create(int width, int height, String title, long monitor, long share);
	
	/**
	 * Set window title.
	 * @param title - Title of window.
	 */
	public void setTitle(String title);
	
	/**
	 * Update window. Swap draw buffers and poll for window events.
	 */
	public void update();
	
	/**
	 * Capture/Release mouse.
	 * @param capture - Capture/Release.
	 */
	public void captureMouse(boolean capture);
	
	/**
	 * Will enable/disable VSync making a call to glfwSwapInterval(int).
	 * @param vsync - VSync on or off.
	 */
	public void enableVSync(boolean vsync);
	
	/**
	 * Resize window.
	 * @param width - New width of window.
	 * @param height  - New height of window.
	 */
	public void resize(int width, int height);
	
	public boolean isMouseCaptured();
	
	public void setErrorCallback(GLFWErrorCallback errorCallback);

	public void setKeyCallback(GLFWKeyCallbackI keyCallback);

	public void setCharCallback(GLFWCharCallback charCallback);

	public  void setCharModsCallback(GLFWCharModsCallback charModsCallback);

	public  void setMouseButtonCallback(GLFWMouseButtonCallbackI mouseButtonCallback);

	public  void setCursorPosCallback(GLFWCursorPosCallbackI cursorPosCallback);

	public  void setCursorEnterCallback(GLFWCursorEnterCallback cursorEnterCallback);

	public  void setScrollCallback(GLFWScrollCallback scrollCallback);

	public  void setDropCallback(GLFWDropCallback dropCallback);

	public  void setMonitorCallback(GLFWMonitorCallback monitorCallback);

	public  void setWindowPosCallback(GLFWWindowPosCallback winPosCallback);

	public  void setWindowSizeCallback(GLFWWindowSizeCallback winSizeCallback);

	public  void setWindowCloseCallback(GLFWWindowCloseCallback winCloseCallback);

	public  void setWindowRefreshCallback(GLFWWindowRefreshCallback winRefreshCallback);

	public  void setWindowFocusCallback(GLFWWindowFocusCallback winFocusCallback);

	public  void setWindowIconifyCallback(GLFWWindowIconifyCallback winIconifyCallback);

	public void setFramebufferSizeCallback(GLFWFramebufferSizeCallback framebufferSizeCallback);
	
	public String getTitle();
	
	/**
	 * @return Window width
	 */
	public int getWidth();
	
	/**
	 * @return Window height
	 */
	public int getHeight();
	
	// TODO Add video mode customization
	
	/**
	 * Get all monitor handles
	 * @return Connected monitor handles
	 */
	public long[] getMonitors();
	
	/**
	 * Get window ratio.
	 * @return Window ratio. (height / width)
	 */
	public double getWindowRatio();
	
	/**
	 * Get monitor ratio.
	 * @return Monitor ratio. (height / width)
	 */
	public double getMonitorRatio(long monitorHandle);
	
	/**
	 * @return True if Display call to create(int, int, String, long, long) has been successful.
	 */
	public boolean hasBeenCreated();

	/** 
	 * Releases all callback's, destroys window and terminates GLFW
	 */
	public void destroy();
	
	// DEBUG AREA //
	
	/**
	 * <b> DEBUG FUNCTION </b><br>
	 * Displays window title to title + FPS. Display.title will not contain FPS.
	 * @param fps - Amount of FPS to display.
	 */
	public  void displayFPS(int fps);
}
