package hills.controller.ModelInterfaceControllers;

import hills.controller.InputControllers.InputMediator;
import hills.controller.InputControllers.KeyboardListener;
import hills.controller.InputControllers.MouseListener;
import hills.model.IMovable;
import hills.model.PlayerMovable;
import hills.services.ServiceLocator;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Gustav Engsmyre
 * @RevisedBy Cornelis Sjöbeck
 */
public class MovableController implements KeyboardListener, MouseListener{
    private PlayerMovable player;
    private List<IMovable> movableList = new ArrayList<>();
    int update = 1;
    public MovableController(){
        InputMediator.INSTANCE.subscribeToKeyboard(this);
        InputMediator.INSTANCE.subscribeToMouse(this);
    }
    public void addAIMovable(IMovable movable){
        movableList.add(movable);
    }
    public void setPlayer(PlayerMovable movable){
        player = movable;
    }
    public void updateMovables(float delta, double runtime, boolean test){
        boolean updateDir = false;
        player.updateMovable(delta);
        float h = ServiceLocator.INSTANCE.getTerrainHeightService(test)
                .getHeight(player.get3DPos().getX(), player.get3DPos().getZ());
        if(player.get3DPos().getY()<=h) {
           player.setHeight(h);
        }
        else{
            player.addGravityVelocity(delta);
        }
        if(runtime > update){
            updateDir = true;
            update++;
        }
        for(IMovable movable : movableList){
            movable.updateMovable(delta);
            movable.setHeight(ServiceLocator.INSTANCE.getTerrainHeightService(test).getHeight(movable.get3DPos()));
            if(updateDir){
                double dir = ServiceLocator.INSTANCE.getDirectionGenerationService().generateDirection((float) runtime*1000)*360;
                movable.setYaw((float) dir);
            }
        }
        updateCamera();
        // Send updates to all saved objects.
    }

    private void updateCamera(){
        ServiceLocator.INSTANCE.getCameraDataService().setPosition(player.getHeadPos());
        ServiceLocator.INSTANCE.getCameraDataService().setOrientation
                (player.getRightVector(), player.getUpVector(), player.getForwardVector(), false);
    }
    @Override
    public void mouseMoved(float xVelocity, float yVelocity) {
        player.updateYaw(xVelocity*-0.3f);
        player.updatePitch(yVelocity*-0.3f);
    }

    @Override
    public void keyPressed(int key, int mods) {
        setDirection(key, mods, true);
    }

    @Override
    public void keyReleased(int key, int mods) {
        setDirection(key, mods, false);
    }


    private void setDirection(int key, int mods, boolean pressed){
        if(key == GLFW.GLFW_KEY_LEFT_SHIFT){
            player.addVelocity(PlayerMovable.Direction.SPRINT, pressed);
        }
        switch (key){
            case GLFW.GLFW_KEY_W:
                player.addVelocity(PlayerMovable.Direction.FORWARD, pressed);       // Forward Velocity
                break;
            case GLFW.GLFW_KEY_A:
                player.addVelocity(PlayerMovable.Direction.LEFT, pressed);       // RightVelocity
                break;
            case GLFW.GLFW_KEY_S:
                player.addVelocity(PlayerMovable.Direction.BACK, pressed);       //Backward Velocity
                break;
            case GLFW.GLFW_KEY_D:
                player.addVelocity(PlayerMovable.Direction.RIGHT, pressed);       // Left Velocity
                break;
            case  GLFW.GLFW_KEY_SPACE:
                if(player.getVelocity().getY()<=0 &&
                        player.get3DPos().getY()<=ServiceLocator.
                                INSTANCE.getTerrainHeightService(false).getHeight(player.get3DPos())+0.1) {
                    player.addVelocity(PlayerMovable.Direction.UP, pressed);
                }
                break;
                default: break;
        }
    }
}
