package hills.controller;

/**
 * Created by gustav on 2017-04-05.
 */
public interface MouseListener {
    void mouseMoved(float xVelocity, float yVelocity);
    void mousePressed(int button, int mods);
    void mouseReleased(int button, int mods);

}
