package hills.controller.ModelInterfaceControllers;

import hills.controller.InputControllers.InputLocator;
import hills.controller.InputControllers.KeyboardListener;
import hills.controller.InputControllers.MouseListener;
import hills.model.IMovable;
import hills.model.PlayerMovable;
import hills.services.ServiceLocator;
import hills.util.math.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustav on 2017-05-06.
 */
public class MovableController implements KeyboardListener, MouseListener{
    private PlayerMovable player;
    private List<IMovable> movableList = new ArrayList<>();

    public MovableController(){
        InputLocator.INSTANCE.subscribeToKeyboard(this);
        InputLocator.INSTANCE.subscribeToMouse(this);
    }
    public void addAIMovable(IMovable movable){
        movableList.add(movable);
    }
    public void setPlayer(PlayerMovable movable){
        player = movable;
    }
    public void updateMovables(float delta, double runtime){
        player.updateMovable(delta);
        for(IMovable movable : movableList){
            movable.updateMovable(delta);
            if(runtime % 1000 == 0){

            }
        }
        ServiceLocator.INSTANCE.getCameraDataService().setOrientation(
                player.getForwardVector(), player.getUpVector(), player.getRightVector(), false);
        ServiceLocator.INSTANCE.getCameraUpdateService().updateGPUCameraMatrix();
        // Send updates to all saved objects.
    }

    @Override
    public void mouseMoved(float xVelocity, float yVelocity) {
        player.updateYaw(xVelocity);
        player.updatePitch(yVelocity);
    }

    @Override
    public void mousePressed(int button, int mods) {
    }

    @Override
    public void mouseReleased(int button, int mods) {

    }

    @Override
    public void KeyPressed(int key, int mods) {
        setDirection(key, mods, true);
    }

    @Override
    public void keyReleased(int key, int mods) {
        setDirection(key, mods, false);
    }
    private void setDirection(int key, int mods, boolean pressed){
        Vec3 forw = player.getForwardVector();
        Vec3 forward = new Vec3(forw.getX(), 0, forw.getZ());
        Vec3 right = player.getRightVector();
        int posOrNeg = 1;
        int speed = 1;
        if(!pressed){
            posOrNeg = -1;
        }
        if(key == GLFW.GLFW_KEY_LEFT_SHIFT || mods == GLFW.GLFW_MOD_SHIFT){
            speed = 2;
        }
        switch (key){
            case GLFW.GLFW_KEY_W:
                player.addVelocity(forward.mul(posOrNeg).mul(speed));       // Forward Velocity
                break;
            case GLFW.GLFW_KEY_A:
                player.addVelocity(right.mul(posOrNeg).mul(-1));       // RightVelocity
                break;
            case GLFW.GLFW_KEY_S:
                player.addVelocity(forward.mul(posOrNeg).mul(-1));       //Backward Velocity
                break;
            case GLFW.GLFW_KEY_D:
                player.addVelocity(right.mul(posOrNeg));       // Left Velocity
                break;
        }
    }
}
