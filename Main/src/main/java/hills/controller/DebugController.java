package hills.controller;

import hills.controller.InputControllers.InputMediator;
import hills.controller.InputControllers.KeyboardListener;
import hills.services.ServiceLocator;
import hills.services.display.DisplayServiceI;
import org.lwjgl.glfw.GLFW;

/**
 * Created by gustav on 2017-05-12.
 */
public class DebugController implements KeyboardListener{
    private DisplayServiceI displayService;
    public DebugController(){
        InputMediator.INSTANCE.subscribeToKeyboard(this);
        displayService = ServiceLocator.INSTANCE.getDisplayService();
    }
    @Override
    public void KeyPressed(int key, int mods) {
        if(key == GLFW.GLFW_KEY_F1){
             displayService.captureMouse(!displayService.isMouseCaptured());
        }
        if(key == GLFW.GLFW_KEY_F2){
            // set wireframeMode
        }
        if(key == GLFW.GLFW_KEY_F3){
            // Culling service.
        }
    }

    @Override
    public void keyReleased(int key, int mods) {

    }
}
