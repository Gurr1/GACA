package hills.controller;

import hills.controller.InputControllers.InputLocator;
import hills.controller.InputControllers.KeyboardListener;
import hills.controller.InputControllers.MouseListener;
import hills.model.IMovable;
import hills.util.math.Vec2;
import hills.util.math.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustav on 2017-05-06.
 */
public class MovableController{
    private IMovable player;
    private List<IMovable> movableList = new ArrayList<>();

    protected MovableController(){
        InputLocator.INSTANCE.subscribeToKeyboard(this);
        InputLocator.INSTANCE.subscribeToMouse(this);
    }
    void addAIMovable(IMovable movable){
        movableList.add(movable);
    }
    void setPlayer(IMovable movable){
        player = movable;
    }
    void updateMovables(){
        // Send updates to all saved objects.
    }

}
