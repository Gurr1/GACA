package hills.controller.InputControllers;

/**
 * Created by gustav on 2017-04-05.
 */
public interface MouseListener {
    /**
     *  Called when the mouse has been moved. Called repeatedly as mouse is moving.
     * @param dXMovement the x distance moved by the mouse since the last call.
     * @param dYMovement the x distance moved by the mouse since the last call.
     */
    void mouseMoved(float dXMovement, float dYMovement);

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
