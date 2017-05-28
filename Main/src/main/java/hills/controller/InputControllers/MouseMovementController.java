package hills.controller.InputControllers;

import hills.services.ServiceLocator;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Gustav Engsmyre
 */
public final class MouseMovementController implements GLFWCursorPosCallbackI{

	/**
	 * Horizontal position of cursor in pixels.
	 */
	private float xPosition;
	
	/**
	 * Vertical position of cursor in pixels.
	 */
	private float yPosition;

	private List<MouseListener> mouseListeners = new ArrayList<>();

	MouseMovementController(){} // Private constructor no instances

	/**
	 * Called by GLFW display mouse button callback.
	 * @param button - What button triggered an event.
	 * @param action - What event happened.
	 * @param mods - What modifier keys were held down.
	 */

	/**
	 * Called by GLFW display cursor position callback.
	 * @param xpos - Horizontal position of cursor.
	 * @param ypos - Vertical position of cursor.
	 */
	public void positionEvent(double xpos, double ypos){
		float xVelocity = (float) xpos - xPosition;
		float yVelocity = (float) ypos - yPosition;
		
		xPosition = (float) xpos;
		yPosition = (float) ypos;
		if(ServiceLocator.INSTANCE.getDisplayService().isMouseCaptured()) {
			if(xVelocity!=0 || yVelocity!=0) {
				mouseMoved(xVelocity, yVelocity);
			}
		}
	}

	private void mouseMoved(float xVelocity, float yVelocity) {
		for(MouseListener listener : mouseListeners){
			listener.mouseMoved(xVelocity, yVelocity);
		}
	}

	/**
	 * Called by GLFW display scroll callback.
	 * @param xOffset - Horizontal difference from before. (Horizontal velocity).
	 * @param yOffset - Vertical difference from before. (Vertical velocity).
	 */
	public static void scrollEvent(double xOffset, double yOffset){
	}

	public void subscribe(MouseListener listener) {
		mouseListeners.add(listener);
	}

	@Override
	public void invoke(long window, double xpos, double ypos) {
		positionEvent(xpos, ypos);
	}
}
