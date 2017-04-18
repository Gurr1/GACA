package hills.engine.system.domainModel;

import hills.Gurra.Controllers.KeyboardListener;
import hills.Gurra.Controllers.MouseListener;
import hills.Gurra.Controllers.PlayerControllerKeyboard;
import hills.Gurra.Controllers.PlayerControllerMouse;
import hills.Gurra.Models.Commands;
import hills.engine.math.Quaternion;
import hills.engine.math.Vec2;
import hills.engine.math.Vec3;
import hills.engine.math.shape.Sphere;
import javafx.print.PageLayout;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.system.CallbackI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 2017-03-30.
 */
public class Player implements ICollidable, IMovable, KeyboardListener, MouseListener {
    /**
     * {@inheritDoc}
     */

    private Vec3 pos;
    @Getter private float pitch = 0;
    @Getter private float yaw = 0;
    @Setter private float radius = 1;
    @Getter private Vec3 velocity;
    private List<Coin> coinsCollected = new ArrayList<>();
    private List/*<>*/ bugsCollected = new ArrayList();
    private double playerHealth;

    private List<OnMoveListener> moveListeners = new ArrayList<>();
    private float speed = 1;
    private float runModifier = 1;
    @Getter @Setter private boolean toUpdate = true;

    /**
     * Camera up direction.
     */
    @Getter private Vec3 up;
    private final Vec3 globalUp;
    /**
     * Camera right direction.
     */
    @Getter private Vec3 right;

    @Getter private Vec3 forward;
    private float lastPitch = 0;
    @Getter private float playerHeight = 3;
    private Vec3 forwardXZ;

    //<editor-fold desc="Constructors">

    public Player(Vec3 pos) {
        PlayerControllerKeyboard.addListener(this);
        PlayerControllerMouse.addListener(this);
        this.pos = pos;
        forward = new Vec3(0.0f, 0.0f, -1.0f);
        up = new Vec3(0.0f, 1.0f, 0.0f);
        globalUp = up;
        right = new Vec3(1.0f, 0.0f, 0.0f);
        forward = forward.normalize();
        up = up.normalize();
        right = forward.cross(up);
    }
    //</editor-fold>

    //<editor-fold desc="Updates">

    /**
     * adds to the current pitch and corrects it to the 0 - 360 degree range
     * @param diffPitch the amount that should be added to the pitch
     */
    public void updatePitch(float diffPitch) {
        float pitch = fixDegrees(diffPitch + this.pitch);
        if(!(pitch>90 && pitch<270)){
            this.pitch = pitch;
            updateVectors(right, diffPitch);
        }
    }

    /**
     * adds to the current yaw and corrects it to the 0 - 360 degree range
     * @param diffYaw the amount that should be added to the yaw
     */
    public void updateYaw(float diffYaw) {
        this.yaw = fixDegrees(diffYaw + this.yaw);
        updateVectors(globalUp, diffYaw);
    }

    public void checkPlayerHealth(){
        if(playerHealth<=0){
            System.out.println("you died");
        }
    }
    @Override
    public void updatePosition() {
        notifyListeners();
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
        System.out.println(pitch);
        if(pitch>180){
            return;
        }
        if(pitch<0){
            return;
        }
        this.pitch = fixDegrees(pitch);
    }

    @Override
    public void setPosition(Vec3 pos) {
        this.pos = pos;

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
        return new Vec2(pos.getX(),pos.getZ());
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
            switch (direction){
                case MOVEFORWARD:
                    velocity = forwardXZ.mul(speed);
                    break;
                case MOVEBACKWARD:
                    velocity = forwardXZ.mul(-1*speed);
                    break;
                case MOVELEFT:
                    velocity = right.mul(-1*speed);
                    break;
                case MOVERIGHT:
                    velocity = right.mul(speed);
                    break;
                case SHIFTDOWN:
                    speed = runModifier;
                    return;
                case SHIFTUP:
                    speed = 1;
                    return;
            }
            for(int i = 0; i<moveListeners.size(); i++) {
                moveListeners.get(i).moving();
            }
            // Act on each of the directions.
        }
  //  }

    @Override
    public void instructionSent(Commands command) {
        update(command);
        toUpdate = true;
        }

    @Override
    public void mouseMoved(float xVelocity, float yVelocity) {
        updatePitch(yVelocity*-0.3f);
        updateYaw(xVelocity*-0.3f);
        toUpdate = true;
    }

    public void updateVectors(Vec3 axis, float angle) {
        Quaternion rotQuat = new Quaternion(axis, angle);
        forward = rotQuat.mul(forward).normalize();
        up = rotQuat.mul(up).normalize();
        right = forward.cross(up);
        forwardXZ = new Vec3(forward.getX(), 0, forward.getZ()).normalize();        // to fix speed vector
    }

    public void collected(ICollectible collectible) {
        if(collectible.getClass() == Coin.class){
            coinsCollected.add((Coin) collectible);
        }
    }
}
