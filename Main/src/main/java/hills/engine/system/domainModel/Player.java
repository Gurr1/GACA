package hills.engine.system.domainModel;

import hills.Gurra.Controllers.KeyboardListener;
import hills.Gurra.Controllers.PlayerControllerKeyboard;
import hills.Gurra.Models.CameraModel;
import hills.Gurra.Models.Commands;
import hills.engine.math.Vec2;
import hills.engine.math.Vec3;
import hills.engine.math.shape.Sphere;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 2017-03-30.
 */
public class Player implements ICollidable, IMovable, KeyboardListener{
    /**
     * {@inheritDoc}
     */

    private Vec3 pos;
    @Getter private float pitch = 0;
    @Getter private float yaw = 0;
    @Setter private float radius = 1;
    @Getter private Vec3 velocity;
    private int coinCollectedAmount;
    private int bugsCollectedAmount;
    private CameraModel camera;
    private List<OnMoveListener> moveListeners = new ArrayList<>();
    private List<Commands> directions = new ArrayList<>();

    private Vec3 forward;

    /**
     * Camera up direction.
     */
    private Vec3 up;

    /**
     * Camera right direction.
     */
    private Vec3 right;

    //<editor-fold desc="Constructors">

    public Player(Vec3 pos, float radius) {
        this.pos = pos;
        this.radius = radius;
        camera = CameraModel.getInstance();
    }

    public Player(Vec3 pos, float pitch, float yaw) {
        this.pos = pos;
        this.yaw = yaw;
        this.pitch = pitch;
        camera = CameraModel.getInstance();
    }

    public Player(Vec3 pos) {
        PlayerControllerKeyboard.addListener(this);
        this.pos = pos;
    }
    //</editor-fold>

    //<editor-fold desc="Updates">

    /**
     * adds to the current pitch and corrects it to the 0 - 360 degree range
     * @param diffPitch the amount that should be added to the pitch
     */
    public void updatePitch(float diffPitch) {
        this.pitch = fixDegrees(diffPitch + this.pitch);
        camera.setPitch(this.pitch);
    }

    /**
     * adds to the current yaw and corrects it to the 0 - 360 degree range
     * @param diffYaw the amount that should be added to the yaw
     */
    public void updateYaw(float diffYaw) {
        this.yaw = fixDegrees(diffYaw + this.yaw);
        camera.setYaw(this.yaw);
    }


    @Override
    public void updatePosition() {
        notifyListeners();
        camera.setPosition(pos);
    }

    /**
     * calls updatePitch and updateYaw
     * @param diffPitch amount to add to pitch
     * @param diffYaw amount to add to pitch
     */
    public void updateDirection(float diffPitch, float diffYaw) {
        updatePitch(diffPitch);
        updateYaw(diffYaw);
    }
   // </editor-fold>

    //<editor-fold desc = "Setters">
    public void setYaw(float yaw) {
        this.yaw = fixDegrees(yaw);
    }

    public void setPitch(float pitch) {
        this.pitch = fixDegrees(pitch);

    }

    @Override
    public void setPosition(Vec3 pos) {
        this.pos = pos;
        System.out.println(pos);
    }
    //</editor-fold>

    private float fixDegrees(float degree) {
        degree %= 360;
        if (degree <= 0)
            degree += 360;

        return degree;
    }

    @Override
    public Sphere getBoundingSphere() {
        return new Sphere(pos, radius);
    }


    @Override
    public Vec2 get2DPos() {
        return new Vec2(pos.getX(),pos.getY());
    }

    @Override
    public Vec3 get3DPos() {
        return pos;
    }
    public void addPositionObserver(OnMoveListener newListener){
        moveListeners.add(newListener);
    }
    private void notifyListeners(){
        for(OnMoveListener listener : moveListeners){
            listener.moving();
        }
    }
    public void update(Commands direction){       // This should maybe be reversed, so Keyboard sends a prompt that a key has been pressed.
       // directions = PlayerControllerKeyboard.getDirectionsSinceLastCycle();
     //   for(Commands direction : directions){
            switch (direction){
                case MOVEFORWARD:
                    velocity = new Vec3(0,0,-1);
                case MOVEBACKWARD:
                    velocity = new Vec3(0,0,1);
                    return;
                case MOVELEFT:
                    velocity = new Vec3(-1,0,0);
                    return;
                case MOVERIGHT:
                    velocity = new Vec3(1,0,0);
                    return;

            }
            for(int i = 0; i<moveListeners.size(); i++) {
                moveListeners.get(i).moving();
            }
            // Act on each of the directions.
        }
  //  }

    @Override
    public void InstructionSent(Commands command) {
        update(command);
        }
}
