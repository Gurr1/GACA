package hills.controller;

import hills.controller.InputControllers.InputLocator;
import hills.controller.InputControllers.KeyboardListener;
import hills.controller.InputControllers.MouseListener;
import hills.model.IMovable;
import hills.util.math.Vec2;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustav on 2017-05-06.
 */
public class MovableController implements KeyboardListener, MouseListener{
    private IMovable player;
    private List<IMovable> movableList = new ArrayList<>();

    public MovableController(){
        InputLocator.INSTANCE.subscribeToKeyboard(this);
        InputLocator.INSTANCE.subscribeToMouse(this);
    }
    public void addAIMovable(IMovable movable){
        movableList.add(movable);
    }
    public void setPlayer(IMovable movable){
        player = movable;
    }
    public void updateMovables(){
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
        changeVelocity(key, mods, true);
    }

    @Override
    public void keyReleased(int key, int mods) {
        changeVelocity(key, mods, false);
    }
    private void changeVelocity(int key, int mods, boolean pressed){
        float speed = 1;
        if(!pressed){
            speed *= -1;
        }
        Vec2 forward = player.getForward();
        Vec2 right = player.getRight();
        if(key == GLFW.GLFW_KEY_LEFT_SHIFT || mods == GLFW.GLFW_MOD_SHIFT){
            speed *= 2;
        }
        switch (key){
            case GLFW.GLFW_KEY_W:
                player.addVelocity(forward.mul(speed));
                break;
            case GLFW.GLFW_KEY_A:
                player.addVelocity(right.mul(speed*-1));
                break;
            case GLFW.GLFW_KEY_S:
                player.addVelocity(forward.mul(speed*-1));
                break;
            case GLFW.GLFW_KEY_D:
                player.addVelocity(right.mul(speed));
                break;
        }
    }
}
