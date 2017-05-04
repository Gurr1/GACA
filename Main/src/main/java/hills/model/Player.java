package hills.model;

import hills.controller.KeyboardListener;
import hills.controller.MouseListener;
import hills.controller.PlayerControllerKeyboard;
import hills.controller.PlayerControllerMouse;
import hills.util.math.Quaternion;
import hills.util.math.Vec2;
import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;
import lombok.Getter;
import lombok.Setter;

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
    private float runModifier = 2;
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
        updateYaw(0);
        updatePitch(0);

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
            listener.moving(this);
        }
    }

    @Override
    public void instructionSent(Commands direction) {
        toUpdate = true;
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
            case SUPERSPEED:
                speed = 1000000;
                break;
            case SHIFTDOWN:
                speed = runModifier;
                return;
            case SHIFTUP:
                speed = 1;
                return;
        }
        for(int i = 0; i<moveListeners.size(); i++) {
            moveListeners.get(i).moving(this);
        }
        // Act on each of the directions.
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
        forwardXZ = new Vec3(forward.getX(), 0, forward.getZ()).normalize();        // to fix velocity vector so speed always is the same, no matter elevation of focus.
    }

    public void collected(ICollectible collectible) {
        if(collectible.getClass() == Coin.class){
            coinsCollected.add((Coin) collectible);
        }
    }
}
