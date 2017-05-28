package hills.controller;

import hills.controller.InputControllers.InputMediator;
import hills.controller.InputControllers.KeyboardListener;
import hills.services.ServiceLocator;
import hills.services.display.DisplayServiceI;
import hills.util.display.FrameBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * @Author Gustav Engsmyre
 */
public class DebugController implements KeyboardListener{
	
    private DisplayServiceI displayService;
    
    private boolean wireMode = false;
    private boolean cullMode = false;
    
    public DebugController(){
        InputMediator.INSTANCE.subscribeToKeyboard(this);
        displayService = ServiceLocator.INSTANCE.getDisplayService();
    }
    
    @Override
    public void keyPressed(int key, int mods) {
        if(key == GLFW.GLFW_KEY_F1){
             displayService.captureMouse(!displayService.isMouseCaptured());
        }
        if(key == GLFW.GLFW_KEY_F2){
        	wireMode = !wireMode;
            if(wireMode)
            	GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            else
            	GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        }
        if(key == GLFW.GLFW_KEY_F3){
        	cullMode = !cullMode;
        	if(cullMode)
        		FrameBuffer.enableCulling();
        	else
        		FrameBuffer.disableCulling();
        }
    }

    @Override
    public void keyReleased(int key, int mods) {

    }
}
