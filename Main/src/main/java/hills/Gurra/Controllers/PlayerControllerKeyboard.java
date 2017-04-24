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
	private static int nPressed = 0;

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
			nPressed++;
			input(key);
		}
		else if(action == GLFW.GLFW_RELEASE){
			nPressed--;
			pressed[key] = false;
		}
	}

	public static void update(){
		checkInGame();
	}
	/**
	 * interprets actions that have in-game consequences.
	 */
	private static void checkInGame() {
		if (nPressed > 0) {
			if (isPressed(GLFW.GLFW_KEY_W)) {
				for (int i = 0; i < listenerList.size(); i++) {
					listenerList.get(i).instructionSent(Commands.MOVEFORWARD);
				}
			}
			if (isPressed(GLFW.GLFW_KEY_S))
				for (int i = 0; i < listenerList.size(); i++) {
					listenerList.get(i).instructionSent(Commands.MOVEBACKWARD);
				}

			if (isPressed(GLFW.GLFW_KEY_A))
				for (int i = 0; i < listenerList.size(); i++) {
					listenerList.get(i).instructionSent(Commands.MOVELEFT);
				}

			if (isPressed(GLFW.GLFW_KEY_D))
				for (int i = 0; i < listenerList.size(); i++) {
					listenerList.get(i).instructionSent(Commands.MOVERIGHT);
				}
			if (isPressed(GLFW.GLFW_KEY_F3)) {
				for (KeyboardListener listener : listenerList) {
					listener.instructionSent(Commands.SUPERSPEED);
				}
			}
			if (isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				for (KeyboardListener listener : listenerList) {
					System.out.println("fast");
					listener.instructionSent(Commands.SHIFTDOWN);
				}
			} else {
				for (KeyboardListener listener : listenerList) {
					listener.instructionSent(Commands.SHIFTUP);
				}
			}
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
