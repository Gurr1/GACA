package hills.controller.InputControllers;

/**
 * Created by gustav on 2017-04-05.
 */
public interface KeyboardListener {
    /**
     * Called when a button is pressed.
     * @param key The key pressed
     * @param mods Modifiers e.g Shift pressed.
     */
    void KeyPressed(int key, int mods);
    /**
     * Called when a button is released.
     * @param key The key released
     * @param mods Modifiers e.g Shift active.
     */
    void keyReleased(int key, int mods);
}
