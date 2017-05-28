package hills.controller.InputControllers;

/**
 * @Author Gustav Engsmyre
 */
public interface MouseListener {
    /**
     *  Called when the mouse has been moved. Called repeatedly as mouse is moving.
     * @param dXMovement the x distance moved by the mouse since the last call.
     * @param dYMovement the x distance moved by the mouse since the last call.
     */
    void mouseMoved(float dXMovement, float dYMovement);

}
