package hills.controller.InputControllers;

/**
 * Created by gustav on 2017-05-11.
 */
public interface MouseButtonListener {

    /**
     * called when a mouse button has been pressed.
     * @param button which button has been pressed.
     * @param mods Which mods has been held down.
     */
    void mousePressed(int button, int mods);

    /**
     * called when a mouse button has been released.
     * @param button which button has been released.
     * @param mods Which mods was active when mouse button was released.
     */
    void mouseReleased(int button, int mods);
}
