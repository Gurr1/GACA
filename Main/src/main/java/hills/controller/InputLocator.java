package hills.controller;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

/**
 * Created by gustav on 2017-05-05.
 */
public enum InputLocator {
    INSTANCE();
    KeyboardSubscribe keyListener;
    MouseSubscribe mouseListener;
    InputLocator(){
        keyListener = new PlayerControllerKeyboard();
        mouseListener = new PlayerControllerMouse();
    }
    public void subscribeToKeyboard(KeyboardListener listener){
        keyListener.subscribe(listener);
    }
    public void subscribeToMouse(MouseListener listener){
        mouseListener.subscribe(listener);
    }
    public GLFWKeyCallbackI getKeyCallBack(){
        return ((GLFWKeyCallbackI)(keyListener));
    }
    public GLFWMouseButtonCallbackI getMouseButtonCallback(){
        return ((GLFWMouseButtonCallbackI)(mouseListener));
    }
    public GLFWCursorPosCallbackI getCursorPositionCallback(){
        return ((GLFWCursorPosCallbackI)(mouseListener));
    }
}
