package hills.controller.InputControllers;

/**
 * @Author Gustav Engsmyre
 */
public interface KeyboardListener {
    /**
     * Called when a button is pressed.
     * @param key The key pressed
     * @param mods Modifiers e.g Shift pressed.
     */
    void keyPressed(int key, int mods);
    /**
     * Called when a button is released.
     * @param key The key released
     * @param mods Modifiers e.g Shift active.
     */
    void keyReleased(int key, int mods);
}
