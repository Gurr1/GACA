package hills.controller.InputControllers;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

/**
 * @Author Gustav Engsmyre
 * Handles creating dependencies for every input method.
 */
public enum InputMediator {
    INSTANCE();
    MouseMovementController mouse;
    MouseButtonController mouseButton;
    KeyboardController keyboard;
    
    InputMediator(){
       mouse = new MouseMovementController();
       keyboard = new KeyboardController();
       mouseButton = new MouseButtonController();
    }
    
    public void subscribeToKeyboard(KeyboardListener listener){
        keyboard.subscribe(listener);
    }
    public void subscribeToMouse(MouseListener listener){
       mouse.subscribe(listener);
    }

    public void subscribeToMouseButton(MouseButtonListener listener){
        mouseButton.subscribe(listener);
    }
    public GLFWKeyCallbackI getKeyCallBack(){
        return keyboard;
    }
    
    public GLFWMouseButtonCallbackI getMouseButtonCallback(){
        return mouseButton;
    }
    
    public GLFWCursorPosCallbackI getCursorPositionCallback(){
        return ((mouse));
    }
}
