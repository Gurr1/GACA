package hills.Gurra.Controllers;

import hills.Gurra.Models.Commands;
import hills.engine.display.Display;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

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
		System.out.println(key);
		if(key < 0)
			return;
		if (action == GLFW.GLFW_PRESS){
			pressed[key] = true;
			input(key);
		}
		else if(action == GLFW.GLFW_RELEASE){
			pressed[key] = false;
		}
		checkInGame(key, mods);
	}

	/**
	 * interprets actions that have in-game consequences.
	 * @param key	the Key that's pressed
	 * @param mods	if an Modifier key is pressed.
	 */
	private static void checkInGame(int key, int mods) {
		if (isPressed(GLFW.GLFW_KEY_W)){
				for (int i = 0; i < listenerList.size(); i++) {
					if(mods == GLFW.GLFW_MOD_SHIFT){
						listenerList.get(i).instructionSent(Commands.SHIFTDOWN);
					}
					else{
						listenerList.get(i).instructionSent(Commands.SHIFTUP);
					}
					listenerList.get(i).instructionSent(Commands.MOVEFORWARD);
				}
			}
		if (isPressed(GLFW.GLFW_KEY_S))
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
		}

	/**
	 * Checks if key was pressed this cycle.
	 * @param key - Key to check.
	 * @return True if key was pressed this cycle.
	 */
	public static boolean isPressed(int key){ // Returns true if key was pressed this update cycle
		return pressed[key];
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
