package hills.controller;

import org.lwjgl.glfw.GLFWKeyCallbackI;

/**
 * Created by gustav on 2017-05-05.
 */
public enum InputLocator {
    INSTANCE();
    KeyboardSubscribe keyListener;
    InputLocator(){
        keyListener = new PlayerControllerKeyboard();
    }
    public void getKeyInterface(KeyboardListener listener){
        keyListener.subscribe(listener);
    }
    public void getMouseInterface(){

    }
    public GLFWKeyCallbackI getKeyCallBack(){
        return ((PlayerControllerKeyboard)(keyListener))
    }
}
