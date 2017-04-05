package hills.Gurra.Controllers;

import hills.engine.display.Display;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public final class PlayerControllerMouse {

	/**
	 * Which buttons are down/up.
	 */
	private static boolean[] down = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];

	/**
	 * Which buttons were pressed this cycle.
	 */
	private static boolean[] pressed = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];

	/**
	 * Which buttons were released this cycle.
	 */
	private static boolean[] released = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];

	/**
	 * Horizontal position of cursor in pixels.
	 */
	private static float xPosition;

	/**
	 * Vertical position of cursor in pixels.
	 */
	private static float yPosition;

	/**
	 * Horizontal velocity of cursor.
	 */
	private static float xVelocity;

	/**
	 * Vertical velocity of cursor.
	 */
	private static float yVelocity;

	/**
	 * Scroll wheel velocity.
	 */
	private static float scrollVelocity;

	private static List<MouseListener> mouseListeners = new ArrayList<>();

	private PlayerControllerMouse(){} // Private constructor no instances

	/**
	 * Called by GLFW display mouse button callback.
	 * @param button - What button triggered an event.
	 * @param action - What event happened.
	 * @param mods - What modifier keys were held down.
	 */
	public static void buttonEvent(int button, int action, int mods){
		switch(action){
		case GLFW.GLFW_PRESS:
			down[button] = true;
			pressed[button] = true;
			break;
		case GLFW.GLFW_RELEASE:
			down[button] = false;
			released[button] = true;
			break;
		}
	}

	/**
	 * Called by GLFW display cursor position callback.
	 * @param xpos - Horizontal position of cursor.
	 * @param ypos - Vertical position of cursor.
	 */
	public static void positionEvent(double xpos, double ypos){

		xVelocity = (float) xpos - xPosition;
		yVelocity = (float) ypos - yPosition;

		xPosition = (float) xpos;
		yPosition = (float) ypos;
		if(Display.isMouseCaptured()) {
			if(xVelocity!=0 || yVelocity!=0)
			for (MouseListener listener : mouseListeners) {
				listener.mouseMoved(xVelocity, yVelocity);
			}
		}
	}

	public static void addListener(MouseListener listener){
		mouseListeners.add(listener);
	}

	/**
	 * Called by GLFW display scroll callback.
	 * @param xOffset - Horizontal difference from before. (Horizontal velocity).
	 * @param yOffset - Vertical difference from before. (Vertical velocity).
	 */
	public static void scrollEvent(double xOffset, double yOffset){
		scrollVelocity = (float) yOffset;
	}

	/**
	 * Called from game loop!<br>
	 * Updates mouse states.
	 */
	public static void update(){
		for(int i = 0; i < GLFW.GLFW_MOUSE_BUTTON_LAST; i++){
			pressed[i] = false;
			released[i] = false;
		}

		xVelocity = 0.0f;
		yVelocity = 0.0f;
	}

	/**
	 * Checks if button is down.
	 * @param button - Button to check.
	 * @return True if button is down.
	 */
	public static boolean isDown(int button){
		return down[button];
	}

	/**
	 * Checks if button was pressed this cycle.
	 * @param button - Button to check.
	 * @return True if button was pressed this cycle.
	 */
	public static boolean isPressed(int button){
		return pressed[button];
	}

	/**
	 * Checks if button was released this cycle.
	 * @param button - Button to check.
	 * @return True if button was checked.
	 */
	public static boolean isReleased(int button){
		return released[button];
	}

	/**
	 * @return Horizontal position of cursor.
	 */
	public static float getX(){
		return xPosition;
	}

	/**
	 * @return Horizontal velocity of cursor.
	 */
	public static float getXVelocity(){
		return xVelocity;
	}

	/**
	 * @return Vertical position of cursor.
	 */
	public static float getY(){
		return yPosition;
	}

	/**
	 * @return Vertical velocity of cursor.
	 */
	public static float getYVelocity(){
		return yVelocity;
	}

	/**
	 * @return Vertical velocity of scroll wheel.
	 */
	public static float getScrollVelocity(){
		return scrollVelocity;
	}
}
