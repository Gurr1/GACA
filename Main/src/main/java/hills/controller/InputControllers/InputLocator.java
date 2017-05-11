package hills.controller.InputControllers;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

/**
 * Created by gustav on 2017-05-05.
 */
public enum InputLocator {
    INSTANCE();
    MouseSubscribe mouseListener;
    
    PlayerControllerKeyboard keyboard;
    
    InputLocator(){
       // keyListener = new PlayerControllerKeyboard();
       mouseListener = new PlayerControllerMouse();
       keyboard = new PlayerControllerKeyboard();
    }
    
    // TODO Ska ändras så att den retunerar subscribe
    public void subscribeToKeyboard(KeyboardListener listener){
        //keyListener.subscribe(listener);
        
        keyboard.subscribe(listener);
    }
    // TODO Samma sak här
    public void subscribeToMouse(MouseListener listener){
       mouseListener.subscribe(listener);
    }
    
    public GLFWKeyCallbackI getKeyCallBack(){
        return keyboard;
    }
    
    public GLFWMouseButtonCallbackI getMouseButtonCallback(){
        return ((GLFWMouseButtonCallbackI)(mouseListener));
    }
    
    public GLFWCursorPosCallbackI getCursorPositionCallback(){
        return ((GLFWCursorPosCallbackI)(mouseListener));
    }
}
