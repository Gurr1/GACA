package hills.controller.InputControllers;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

/**
 * Created by gustav on 2017-05-05.
 */
public enum InputLocator {
    INSTANCE();
    PlayerControllerMouse mouse;
    
    PlayerControllerKeyboard keyboard;
    
    InputLocator(){
       // keyListener = new PlayerControllerKeyboard();
       mouse = new PlayerControllerMouse();
       keyboard = new PlayerControllerKeyboard();
    }
    
    // TODO Ska �ndras s� att den retunerar subscribe
    public void subscribeToKeyboard(KeyboardListener listener){
        //keyListener.subscribe(listener);
        
        keyboard.subscribe(listener);
    }
    // TODO Samma sak h�r
    public void subscribeToMouse(MouseListener listener){
       mouse.subscribe(listener);
    }
    
    public GLFWKeyCallbackI getKeyCallBack(){
        return keyboard;
    }
    
    public GLFWMouseButtonCallbackI getMouseButtonCallback(){
        return ((mouse));
    }
    
    public GLFWCursorPosCallbackI getCursorPositionCallback(){
        return ((mouse));
    }
}
