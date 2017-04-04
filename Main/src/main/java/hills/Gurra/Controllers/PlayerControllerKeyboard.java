package hills.Gurra.Controllers;

import hills.Gurra.Models.Direction;
import hills.Gurra.View.CameraSystem;
import hills.engine.display.Display;
import lombok.Getter;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

public final class PlayerControllerKeyboard {



	/**
	 * Which keys are down/up.
	 */
	private static boolean[] down = new boolean[GLFW.GLFW_KEY_LAST];
	
	/**
	 * Which keys have been pressed this cycle.
	 */
	private static boolean[] pressed = new boolean[GLFW.GLFW_KEY_LAST];
	
	/**
	 * Which keys have been released this cycle.
	 */
	private static boolean[] released = new boolean[GLFW.GLFW_KEY_LAST];
	private static List<Direction> directions = new ArrayList<>();

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
			
		switch(action){
		case GLFW.GLFW_PRESS:
			down[key] = true;
			pressed[key] = true;
			break;
		case GLFW.GLFW_RELEASE:
			down[key] = false;
			released[key] = true;
			break;
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


	public static List<Direction> getDirectionsSinceLastCycle(){
		List<Direction> cycleDirections = new ArrayList<>(directions);
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

	public void input() {
		if (PlayerControllerKeyboard.isDown(GLFW.GLFW_KEY_W))
			directions.add(Direction.FORWARD);

		if (PlayerControllerKeyboard.isDown(GLFW.GLFW_KEY_S))
			directions.add(Direction.BACKWARD);

		if (PlayerControllerKeyboard.isDown(GLFW.GLFW_KEY_A))
			directions.add(Direction.LEFT);

		if (PlayerControllerKeyboard.isDown(GLFW.GLFW_KEY_D))
			directions.add(Direction.RIGHT);

	/*Ä	if (PlayerControllerKeyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			medialSpeed *= 2.0f;
			lateralSpeed *= 2.0f;
		}

		if (PlayerControllerKeyboard.isPressed(GLFW.GLFW_KEY_LEFT_CONTROL)) {
			medialSpeed *= 0.5f;
			lateralSpeed *= 0.5f;
		}*/

		if (PlayerControllerKeyboard.isPressed(GLFW.GLFW_KEY_SPACE))
			if (Display.isMouseCaptured())
				Display.captureMouse(false);
			else
				Display.captureMouse(true);
	}




}
