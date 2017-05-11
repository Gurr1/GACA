package hills.controller.InputControllers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustav on 2017-05-11.
 */
public class PlayerControllerMouseButton implements GLFWMouseButtonCallbackI {
    List<MouseButtonListener> subscribers = new ArrayList<>();
    @Override
    public void invoke(long window, int button, int action, int mods) {
        buttonEvent(button, action, mods);
    }

    public void subscribe(MouseButtonListener listener) {
        subscribers.add(listener);
    }

    public void buttonEvent(int button, int action, int mods){
        switch(action){
            case GLFW.GLFW_PRESS:
                mousePressed(button, mods);
                break;
            case GLFW.GLFW_RELEASE:
                mouseReleased(button, mods);
                break;
        }
    }
    private void mouseReleased(int button, int mods) {
        for(MouseButtonListener listener : subscribers){
            listener.mousePressed(button, mods);
        }
    }

    private void mousePressed(int button, int mods) {
        for(MouseButtonListener listener : subscribers){
            listener.mousePressed(button, mods);
        }
    }
}
