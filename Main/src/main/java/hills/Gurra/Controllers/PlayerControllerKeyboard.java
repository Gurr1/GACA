package hills.Gurra.Controllers;

import hills.Gurra.Models.Commands;
import hills.engine.display.Display;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
public final class PlayerControllerKeyboard{



	/**
	 * Which keys are down/up.
	 */
	private static boolean[] down = new boolean[GLFW.GLFW_KEY_LAST];
	
	/**
	 * Which keys have been pressed this cycle.
	 */
	private static List<KeyboardListener> listenerList = new ArrayList<>();
	private static boolean[] pressed = new boolean[GLFW.GLFW_KEY_LAST];
	
	/**
	 * Which keys have been released this cycle.
	 */
	private static boolean[] released = new boolean[GLFW.GLFW_KEY_LAST];
	private static List<Commands> directions = new ArrayList<>();

	private PlayerControllerKeyboard(){} // Private constructor no instances
	
	/**
	 * Called by GLFW display key callback.
	 * @param key - What key triggered an event.
	 * @param scancode - System-specific scan-code of key.
	 * @param action - What event happened.
	 * @param mods - What modifier keys were held down.
	 */
	public static void keyEvent(int key, int scancode, int action, int mods){ // Handle key events
		if(key < 0)
			return;
		if (action == GLFW.GLFW_PRESS){
			input(key);
		}
		checkInGame(key);
	}

	private static void checkInGame(int key) {
		if (key == GLFW.GLFW_KEY_W)
			for(int i = 0; i<listenerList.size(); i++){
				listenerList.get(i).instructionSent(Commands.MOVEFORWARD);
			}

		if (key == GLFW.GLFW_KEY_S)
			for(int i = 0; i<listenerList.size(); i++){
				listenerList.get(i).instructionSent(Commands.MOVEBACKWARD);
			}

		if (key == GLFW.GLFW_KEY_A)
			for(int i = 0; i<listenerList.size(); i++){
				listenerList.get(i).instructionSent(Commands.MOVELEFT);
			}

		if (key == GLFW.GLFW_KEY_D)
			for(int i = 0; i<listenerList.size(); i++){
				listenerList.get(i).instructionSent(Commands.MOVERIGHT);
			}
		if (key == GLFW.GLFW_KEY_LEFT_SHIFT){
			for(KeyboardListener listener : listenerList){
				listener.instructionSent(Commands.SHIFTMOD);
			}
		}
		if (key == GLFW.GLFW_KEY_LEFT_CONTROL){
			for(KeyboardListener listener : listenerList){
				listener.instructionSent(Commands.CONROLMOD);
			}
		}
	}

	/**
	 * Called from game loop!<br>
	 * Updates key states.
	 */
	public static void update(){ // Must be called just before display is updated
		for(int i = 0; i < GLFW.GLFW_KEY_LAST; i++){
			pressed[i] = false;
			released[i] = false;
		}
	}
	
	/**
	 * Checks if key is down or not.
	 * @param key - Key to check.
	 * @return True if key is down.
	 */
	public static boolean isDown(int key){ // Returns true if key is down
		return down[key];
	}


	public static List<Commands> getDirectionsSinceLastCycle(){
		List<Commands> cycleDirections = new ArrayList<>(directions);
		directions.clear();
		return cycleDirections;
	}
	/**
	 * Checks if key was pressed this cycle.
	 * @param key - Key to check.
	 * @return True if key was pressed this cycle.
	 */
	public static boolean isPressed(int key){ // Returns true if key was pressed this update cycle
		return pressed[key];
	}
	
	/**
	 * Checks if key was released this cycle.
	 * @param key - Key to check.
	 * @return True if key was released this cycle.
	 */
	public static boolean isReleased(int key){ // Returns true if key was released this update cycle
		return released[key];
	}

	public static void input(int key) {

		if (key  == GLFW.GLFW_KEY_SPACE)
			if (Display.isMouseCaptured())
				Display.captureMouse(false);
			else
				Display.captureMouse(true);
		if(key == GLFW.GLFW_KEY_F1){
			pressed[key] = true;
		}
		if(key == GLFW.GLFW_KEY_F2){
			pressed[key] = true;
		}
	}

	public static void addListener(KeyboardListener listener){
		listenerList.add(listener);
	}



}
