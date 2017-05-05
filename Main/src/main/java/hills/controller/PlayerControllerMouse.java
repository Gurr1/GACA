package hills.controller;

import hills.util.display.Display;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public final class PlayerControllerMouse implements MouseSubscibe{

	/**
	 * Horizontal position of cursor in pixels.
	 */
	private float xPosition;
	
	/**
	 * Vertical position of cursor in pixels.
	 */
	private float yPosition;

	private List<MouseListener> mouseListeners = new ArrayList<>();

	PlayerControllerMouse(){} // Private constructor no instances

	/**
	 * Called by GLFW display mouse button callback.
	 * @param button - What button triggered an event.
	 * @param action - What event happened.
	 * @param mods - What modifier keys were held down.
	 */
	public void buttonEvent(int button, int action, int mods){
		switch(action){
		case GLFW.GLFW_PRESS:
			mousePressed(button, mods);
			break;
		case GLFW.GLFW_RELEASE:
			mouseReleased(button, mods);
			break;
		}
	}

	private void mouseReleased(int button, int mods) {
		for(MouseListener listener : mouseListeners){
			listener.mousePressed(button, mods);
		}
	}

	private void mousePressed(int button, int mods) {
		for(MouseListener listener : mouseListeners){
			listener.mousePressed(button, mods);
		}
	}

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
		if(Display.isMouseCaptured()) {
			if(xVelocity!=0 || yVelocity!=0)
				mouseMoved(xVelocity, yVelocity);
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

	@Override
	public void subscribe(MouseListener listener) {
		mouseListeners.add(listener);
	}
}
