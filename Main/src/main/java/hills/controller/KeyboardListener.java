package hills.controller;

/**
 * Created by gustav on 2017-04-05.
 */
public interface KeyboardListener {
    void KeyPressed(int key, int mods);
    void keyReleased(int key, int mods);
}
