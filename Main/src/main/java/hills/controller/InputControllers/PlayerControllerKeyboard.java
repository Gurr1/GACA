package hills.controller.InputControllers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Gustav Engsmyre
 */
public final class PlayerControllerKeyboard implements GLFWKeyCallbackI{

	/**
	 * List of Subscribers.
	 */
	private List<KeyboardListener> subscribers = new ArrayList<>();

	public PlayerControllerKeyboard(){
	}
	
	/**
	 * Called by GLFW display key callback.
	 * @param key - What key triggered an event.
	 * @param scancode - System-specific scan-code of key.
	 * @param action - What event happened.
	 * @param mods - What modifier keys were held down.
	 */
	public void keyEvent(int key, int scancode, int action, int mods){ // Handle key events
		if(key < 0)
			return;
		
		if (action == GLFW.GLFW_PRESS){
			keyPressed(key, mods);
		}
		else if(action == GLFW.GLFW_RELEASE){
			keyReleased(key, mods);
		}
	}

	private void keyPressed(int key, int mods) {
		for(KeyboardListener listener : subscribers){
			listener.KeyPressed(key, mods);
		}
	}
	private void keyReleased(int key, int mods){
		for (KeyboardListener listener : subscribers){
			listener.keyReleased(key, mods);
		}
	}
	public void subscribe(KeyboardListener listener) {
		subscribers.add(listener);
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keyEvent(key, scancode, action, mods);
	}
}
