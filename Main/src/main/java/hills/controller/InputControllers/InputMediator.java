package hills.controller.InputControllers;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

/**
 * @Author Gustav Engsmyre
 */
public enum InputMediator {
    INSTANCE();
    PlayerControllerMouse mouse;
    PlayerControllerMouseButton mouseButton;
    PlayerControllerKeyboard keyboard;
    
    InputMediator(){
       // keyListener = new PlayerControllerKeyboard();
       mouse = new PlayerControllerMouse();
       keyboard = new PlayerControllerKeyboard();
       mouseButton = new PlayerControllerMouseButton();
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
