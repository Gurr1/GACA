package hills.engine.display;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCharModsCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorEnterCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetDropCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMonitorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowCloseCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIconifyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowRefreshCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.system.MemoryUtil.NULL;
import hills.engine.input.Keyboard;
import hills.engine.input.Mouse;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCharModsCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowRefreshCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public final class Display {

	// GLFW callback's

	// Error callback's
	private static GLFWErrorCallback errorCallback;

	// Input callback's
	private static GLFWKeyCallback keyCallback;
	private static GLFWCharCallback charCallback;
	private static GLFWCharModsCallback charModsCallback;
	private static GLFWMouseButtonCallback mouseButtonCallback;
	private static GLFWCursorPosCallback cursorPosCallback;
	private static GLFWCursorEnterCallback cursorEnterCallback;
	private static GLFWScrollCallback scrollCallback;
	private static GLFWDropCallback dropCallback;
	// NOT INCLUDED YET! private static GLFWJoystickCallback joystickCallback;

	// Monitor callback's
	private static GLFWMonitorCallback monitorCallback;

	// Window callback's
	private static GLFWWindowPosCallback winPosCallback;
	private static GLFWWindowSizeCallback winSizeCallback;
	private static GLFWWindowCloseCallback winCloseCallback;
	private static GLFWWindowRefreshCallback winRefreshCallback;
	private static GLFWWindowFocusCallback winFocusCallback;
	private static GLFWWindowIconifyCallback winIconifyCallback;
	private static GLFWFramebufferSizeCallback framebufferSizeCallback;

	// Window handle
	private static long HANDLE = NULL;

	// State variables
	private static boolean created = false; // If window has been successfully
											// created
	private static boolean vsync = false; // VSync default off
	private static boolean mouseCaptured = false; // Mouse captured default
													// false
	private static String title; // Title of window

	private Display() {
	} // Private constructor = no instances

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
	public static void create(int width, int height, String title) {
		create(width, height, title, NULL, NULL);
	}

	/**
	 * Creates new GLFW window and sets up input handling with Keyboard and
	 * Mouse classes <br>
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
	 * @param iconPaths
	 *            - Paths to different sized icon images for window and task
	 *            bar. Paths starts at "resources/textures/".
	 */
	public static void create(int width, int height, String title,
			long monitor, long share) {
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
		if (HANDLE == NULL)
			throw new RuntimeException("Failed to create GLFW window");

		// If windowed mode get primary monitor resolution and center window
		if (monitor == NULL) {
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(HANDLE, (vidMode.width() - width) / 2,
					(vidMode.height() - height) / 2);
		}

		// Make OpenGL context current
		glfwMakeContextCurrent(HANDLE);

		// V-sync | 0 - Disabled (May result in screen tearing) | 1 - Enabled
		// (Max FPS = Monitor refresh rate)
		glfwSwapInterval(vsync ? 1 : 0);

		// Make window visible
		glfwShowWindow(HANDLE);

		// No OpenGL calls before this!
		GL.createCapabilities();

		// Set GLViewport to cover the new window
		GL11.glViewport(0, 0, width, height);

		// Setup input callback's
		setKeyCallback(new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action,
					int mods) {
				Keyboard.keyEvent(key, scancode, action, mods);
			}
		});

		setMouseButtonCallback(new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				Mouse.buttonEvent(button, action, mods);
			}
		});

		setCursorPosCallback(new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				Mouse.positionEvent(xpos, ypos);
			}
		});

		setScrollCallback(new GLFWScrollCallback() {
			public void invoke(long window, double xoffset, double yoffset) {
				Mouse.scrollEvent(xoffset, yoffset);
			}
		});

		Display.title = title;
		created = true;
	}

	/**
	 * Set window title.
	 * 
	 * @param title
	 *            - Title of window.
	 */
	public static void setTitle(String title) {
		Display.title = title;
		GLFW.glfwSetWindowTitle(HANDLE, title);
	}

	/**
	 * Update window. Swap draw buffers and poll for window events.
	 */
	public static void update() {
		glfwSwapBuffers(HANDLE); // Swap draw buffers
		glfwPollEvents(); // Poll for window events. keyCallback "invoke" will
							// be called
	}

	/**
	 * Capture/Release mouse.
	 * 
	 * @param capture
	 *            - Capture/Release.
	 */
	public static void captureMouse(boolean capture) {
		if (capture)
			glfwSetInputMode(HANDLE, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		else
			glfwSetInputMode(HANDLE, GLFW_CURSOR, GLFW_CURSOR_NORMAL);

		mouseCaptured = capture;
	}

	/**
	 * Will enable/disable VSync making a call to glfwSwapInterval(int).
	 * 
	 * @param vsync
	 *            - VSync on or off.
	 */
	public static void enableVSync(boolean vsync) {
		Display.vsync = vsync;
		glfwSwapInterval(vsync ? 1 : 0);
	}

	/**
	 * Resize window.
	 * 
	 * @param width
	 *            - New width of window.
	 * @param height
	 *            - New height of window.
	 */
	public static void resize(int width, int height) {
		GLFW.glfwSetWindowSize(HANDLE, width, height);
	}

	public static boolean isMouseCaptured() {
		return mouseCaptured;
	}

	public static void setErrorCallback(GLFWErrorCallback errorCallback) {
		Display.errorCallback = errorCallback;
		GLFWErrorCallback callback = glfwSetErrorCallback(Display.errorCallback);
		if (callback != null)
			callback.free();
	}

	public static void setKeyCallback(GLFWKeyCallback keyCallback) {
		Display.keyCallback = keyCallback;
		GLFWKeyCallback callback = glfwSetKeyCallback(HANDLE,
				Display.keyCallback);
		if (callback != null)
			callback.free();
	}

	public static void setCharCallback(GLFWCharCallback charCallback) {
		Display.charCallback = charCallback;
		GLFWCharCallback callback = glfwSetCharCallback(HANDLE,
				Display.charCallback);
		if (callback != null)
			callback.free();
	}

	public static void setCharModsCallback(GLFWCharModsCallback charModsCallback) {
		Display.charModsCallback = charModsCallback;
		GLFWCharModsCallback callback = glfwSetCharModsCallback(HANDLE,
				Display.charModsCallback);
		if (callback != null)
			callback.free();
	}

	public static void setMouseButtonCallback(
			GLFWMouseButtonCallback mouseButtonCallback) {
		Display.mouseButtonCallback = mouseButtonCallback;
		GLFWMouseButtonCallback callback = glfwSetMouseButtonCallback(HANDLE,
				Display.mouseButtonCallback);
		if (callback != null)
			callback.free();
	}

	public static void setCursorPosCallback(
			GLFWCursorPosCallback cursorPosCallback) {
		Display.cursorPosCallback = cursorPosCallback;
		GLFWCursorPosCallback callback = glfwSetCursorPosCallback(HANDLE,
				Display.cursorPosCallback);
		if (callback != null)
			callback.free();
	}

	public static void setCursorEnterCallback(
			GLFWCursorEnterCallback cursorEnterCallback) {
		Display.cursorEnterCallback = cursorEnterCallback;
		GLFWCursorEnterCallback callback = glfwSetCursorEnterCallback(HANDLE,
				Display.cursorEnterCallback);
		if (callback != null)
			callback.free();
	}

	public static void setScrollCallback(GLFWScrollCallback scrollCallback) {
		Display.scrollCallback = scrollCallback;
		GLFWScrollCallback callback = glfwSetScrollCallback(HANDLE,
				Display.scrollCallback);
		if (callback != null)
			callback.free();
	}

	public static void setDropCallback(GLFWDropCallback dropCallback) {
		Display.dropCallback = dropCallback;
		GLFWDropCallback callback = glfwSetDropCallback(HANDLE,
				Display.dropCallback);
		if (callback != null)
			callback.free();
	}

	public static void setMonitorCallback(GLFWMonitorCallback monitorCallback) {
		Display.monitorCallback = monitorCallback;
		GLFWMonitorCallback callback = glfwSetMonitorCallback(Display.monitorCallback);
		if (callback != null)
			callback.free();
	}

	public static void setWindowPosCallback(GLFWWindowPosCallback winPosCallback) {
		Display.winPosCallback = winPosCallback;
		GLFWWindowPosCallback callback = glfwSetWindowPosCallback(HANDLE,
				Display.winPosCallback);
		if (callback != null)
			callback.free();
	}

	public static void setWindowSizeCallback(
			GLFWWindowSizeCallback winSizeCallback) {
		Display.winSizeCallback = winSizeCallback;
		GLFWWindowSizeCallback callback = glfwSetWindowSizeCallback(HANDLE,
				Display.winSizeCallback);
		if (callback != null)
			callback.free();
	}

	public static void setWindowCloseCallback(
			GLFWWindowCloseCallback winCloseCallback) {
		Display.winCloseCallback = winCloseCallback;
		GLFWWindowCloseCallback callback = glfwSetWindowCloseCallback(HANDLE,
				Display.winCloseCallback);
		if (callback != null)
			callback.free();
	}

	public static void setWindowRefreshCallback(
			GLFWWindowRefreshCallback winRefreshCallback) {
		Display.winRefreshCallback = winRefreshCallback;
		GLFWWindowRefreshCallback callback = glfwSetWindowRefreshCallback(
				HANDLE, Display.winRefreshCallback);
		if (callback != null)
			callback.free();
	}

	public static void setWindowFocusCallback(
			GLFWWindowFocusCallback winFocusCallback) {
		Display.winFocusCallback = winFocusCallback;
		GLFWWindowFocusCallback callback = glfwSetWindowFocusCallback(HANDLE,
				Display.winFocusCallback);
		if (callback != null)
			callback.free();
	}

	public static void setWindowIconifyCallback(
			GLFWWindowIconifyCallback winIconifyCallback) {
		Display.winIconifyCallback = winIconifyCallback;
		GLFWWindowIconifyCallback callback = glfwSetWindowIconifyCallback(
				HANDLE, Display.winIconifyCallback);
		if (callback != null)
			callback.free();
	}

	public static void setFramebufferSizeCallback(
			GLFWFramebufferSizeCallback framebufferSizeCallback) {
		Display.framebufferSizeCallback = framebufferSizeCallback;
		GLFWFramebufferSizeCallback callback = glfwSetFramebufferSizeCallback(
				HANDLE, Display.framebufferSizeCallback);
		if (callback != null)
			callback.free();
	}

	public static String getTitle() {
		return title;
	}

	/**
	 * @return Window width
	 */
	public static int getWidth() {
		IntBuffer width = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetFramebufferSize(HANDLE, width, null);
		return width.get(0);
	}

	/**
	 * @return Window height
	 */
	public static int getHeight() {
		IntBuffer height = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetFramebufferSize(HANDLE, null, height);
		return height.get(0);
	}

	// TODO Add video mode customization

	/**
	 * Get all monitor handles
	 * 
	 * @return Connected monitor handles
	 */
	public static long[] getMonitors() {
		PointerBuffer buffer = glfwGetMonitors();
		long[] handles = new long[buffer.limit()];
		for (int i = 0; i < handles.length; i++)
			handles[i] = buffer.get();

		return handles;
	}

	/**
	 * Get window ratio.
	 * 
	 * @return Window ratio. (height / width)
	 */
	public static double getWindowRatio() {
		return (double) getHeight() / (double) getWidth();
	}

	/**
	 * Get monitor ratio.
	 * 
	 * @return Monitor ratio. (height / width)
	 */
	public static double getMonitorRatio(long monitorHandle) {
		GLFWVidMode vidMode = glfwGetVideoMode(monitorHandle);
		return (double) vidMode.height() / (double) vidMode.width();
	}

	/**
	 * @return True if Display call to create(int, int, String, long, long) has
	 *         been successful.
	 */
	public static boolean hasBeenCreated() {
		return created;
	}

	/**
	 * Releases all callback's, destroys window and terminates GLFW
	 */
	public static void destroy() {
		try {
			if (errorCallback != null)
				errorCallback.free();

			if (keyCallback != null)
				keyCallback.free();
			if (charCallback != null)
				charCallback.free();
			if (charModsCallback != null)
				charModsCallback.free();
			if (mouseButtonCallback != null)
				mouseButtonCallback.free();
			if (cursorPosCallback != null)
				cursorPosCallback.free();
			if (cursorEnterCallback != null)
				cursorEnterCallback.free();
			if (scrollCallback != null)
				scrollCallback.free();
			if (dropCallback != null)
				dropCallback.free();

			if (monitorCallback != null)
				monitorCallback.free();

			if (winPosCallback != null)
				winPosCallback.free();
			if (winSizeCallback != null)
				winSizeCallback.free();
			if (winCloseCallback != null)
				winCloseCallback.free();
			if (winRefreshCallback != null)
				winRefreshCallback.free();
			if (winFocusCallback != null)
				winFocusCallback.free();
			if (winIconifyCallback != null)
				winIconifyCallback.free();
			if (framebufferSizeCallback != null)
				framebufferSizeCallback.free();

			glfwDestroyWindow(HANDLE);
		} finally {
			Callbacks.glfwFreeCallbacks(HANDLE);
			glfwTerminate();

			created = false;
		}
	}

	// DEBUG AREA //

	// Debug functions

	/**
	 * <b> DEBUG FUNCTION </b><br>
	 * Displays window title to title + FPS. Display.title will not contain FPS.
	 * 
	 * @param fps
	 *            - Amount of FPS to display.
	 */
	public static void displayFPS(int fps) {
		GLFW.glfwSetWindowTitle(HANDLE, title + " | FPS: " + fps);
	}

}
