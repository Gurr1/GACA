package hills.controller;

import hills.controller.InputControllers.InputLocator;
import hills.controller.InputControllers.KeyboardListener;
import hills.controller.InputControllers.MouseListener;
import hills.model.IMovable;

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

    }

    @Override
    public void keyReleased(int key, int mods) {

    }

}
