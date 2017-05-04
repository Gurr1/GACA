package hills.controller;

import hills.model.Commands;
import hills.model.Player;
import hills.model.World;
import hills.util.display.Display;

import hills.util.math.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
public final class PlayerControllerKeyboard{


	/**
	 * Which keys have been pressed this cycle.
	 */
	private static boolean[] pressed = new boolean[GLFW.GLFW_KEY_LAST];
	Player player;
	/**
	 * Which keys have been released this cycle.
	 */

	PlayerControllerKeyboard(){

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
			checkInGame(key, mods, true);
			input(key);
		}
		else if(action == GLFW.GLFW_RELEASE){
			checkInGame(key,mods,  false);
		}
	}

	private void checkInGame(int key, int mods, boolean pressed) {
		if (key == GLFW.GLFW_KEY_W && mods != GLFW.GLFW_MOD_SHIFT) {
			player.instructionSent(Commands.MOVEFORWARD, pressed);
		}
		if (key == GLFW.GLFW_KEY_W && mods != GLFW.GLFW_MOD_SHIFT){
			player.instructionSent(Commands.MOVEFORWARD, pressed);
		}
		if (key == (GLFW.GLFW_KEY_S))
			player.instructionSent(Commands.MOVEBACKWARD, pressed);

		if (key == (GLFW.GLFW_KEY_A))
				player.instructionSent(Commands.MOVELEFT, pressed);

		if (key == (GLFW.GLFW_KEY_D))
				player.instructionSent(Commands.MOVERIGHT, pressed);

		if (key == (GLFW.GLFW_KEY_F3)) {
				player.instructionSent(Commands.SUPERSPEED, pressed);
		}
		Vec3 pos = player.get3DPos();
		player.setHeight(ServiceMediator.INSTANCE.getHeight(pos.getX(), pos.getZ()));
	}

	/**
	 * Interprets inputs that affects other services.
	 * @param key the Key pressed.
	 */
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


}
