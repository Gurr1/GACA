package hills.model;

import hills.util.math.Quaternion;
import hills.util.math.Vec2;
import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Gustav Engsmyre, Anders Hansson
 * @RevisedBy Cornelis Sjöbeck
 *
 */
public class Player implements PlayerMovable, PlayerCollidable, IAttack {
    // Add a method that recalculates reworks the base into global base from addVelocity.
    /**
     * {@inheritDoc}
     */

    private Vec3 pos;
    private Vec3 spawnPos;
    @Getter private float pitch = 0;
    @Getter private float yaw = 0;
    @Setter private float radius = 1;
    private Vec3 velocity = new Vec3(0,0,0);
    private List<Coin> coinsCollected = new ArrayList<>();
    private List<Bug> bugsCollected = new ArrayList<>();
    private int playerHealth = 100;
    private float runModifier = 5;
    private Weapon gun = new Gun();
    private boolean attacking = false;

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
    private Vec3 velocityX = new Vec3(0,0,0);
    private float speed = 5;
    private float defaultSpeed = 5;
    private Vec3 velocityZ = new Vec3(0,0,0);
    private Vec3 velocityY = new Vec3(0,0,0);
    private Vec3 gravityVelocity = new Vec3(0,-9.82f, 0);
    private Vec3 jumpVelocity = new Vec3(0,1.0f, 0);
    private  Vec3 v = new Vec3(0, 0, 0);
    private Direction direction = Direction.DOWN;
    private Quaternion rotQuat;
    //<editor-fold desc="Constructors">

    public Player(Vec3 pos) {
        this.pos = pos;
        spawnPos = pos;
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

    public Vec3 getVelocity(){
        return new Vec3(velocity);
    }

    @Override
    public Vec3 getHeadPos() {
        return new Vec3(pos.getX(), pos.getY()+playerHeight, pos.getZ());
    }

    @Override
    public void addVelocity(Direction direction, boolean pressed) {
        int mOs = 2;
        if(!pressed){
            mOs = 0;
        }
        if(direction == Direction.FORWARD){
            velocityX = new Vec3(forwardXZ.mul(mOs));
        }
        if(direction == Direction.BACK){
            velocityX = new Vec3(forwardXZ.mul(mOs*-1));
        }
        if(direction == Direction.SPRINT){
            speed = defaultSpeed + runModifier*mOs;
        }
        if(direction == Direction.LEFT){
            velocityZ = new Vec3(right.mul(mOs*-1));
        }
        if(direction == Direction.RIGHT){
            velocityZ = new Vec3(right.mul(mOs));
        }
        if(direction == Direction.UP){
            velocityY = velocityY.add(jumpVelocity.mul(mOs));
        }
        if(direction == Direction.DOWN){
            velocityY = velocityY.add(jumpVelocity.mul(-mOs));
        }
        velocity = velocityX.add(velocityZ).normalize().mul(speed);
        this.direction = direction;
    }

    public void addVelocity(Vec3 v){

        if(!velocity.equals(new Vec3(0,0,0))) {
            if (direction == Direction.BACK) {
                rotQuat = new Quaternion(rotQuat.getAxis(), rotQuat.getAngle() + 180);
            }
            if (direction == Direction.SPRINT) {
                v = v.mul(2);
            }
            if (direction == Direction.LEFT) {
                rotQuat = new Quaternion(rotQuat.getAxis(), rotQuat.getAngle() - 90);

            }
            if (direction == Direction.RIGHT) {
                rotQuat = new Quaternion(rotQuat.getAxis(), rotQuat.getAngle() + 90);

            }
            this.v = rotQuat.mul(v);
            this.v = v.normalize().mul(velocity.getLength());
        }
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

    @Override
    public void setHeight(float y){
        velocityY = new Vec3(0,0,0);
        pos =  new Vec3(pos.getX(), y, pos.getZ());
    }

    /**
     * adds to the current yaw and corrects it to the 0 - 360 degree range
     * @param diffYaw the amount that should be added to the yaw
     */
    public void updateYaw(float diffYaw) {
        this.yaw = fixDegrees(diffYaw + this.yaw);
        updateVectors(globalUp, diffYaw);
        updateVelocity();
    }

    private void updateVelocity() {
        int yDir = 1;
        int xDir = 1;
        if((velocityZ.add(right).getLength())<2){       // Checks if velocity is backwards
            yDir = -1;
        }
        if((velocityX.add(forwardXZ).getLength())<2){     // Checks is velocity is to the left.
            xDir = -1;
        }
        float xVelocity = velocityX.getLength()*xDir;
        float yVelocity = velocityZ.getLength()*yDir;
        velocityX = forwardXZ.mul(xVelocity);
        velocityZ = right.mul(yVelocity);
        velocity = velocityX.add(velocityZ).normalize().mul(speed);
    }

    @Override
    public void updateMovable(float delta) {
        velocity = velocity.add(v);
        velocity = velocity.add(velocityY);
        pos = pos.add(velocity.mul(delta));
        velocity = velocity.sub(v);
        v = v.mul(0);
    }

    @Override
    public void addGravityVelocity(float delta) {
        velocityY = velocityY.add(gravityVelocity.mul(delta));
    }

    public void checkPlayerHealth(){
        if(playerHealth<=0){
            System.out.println("you died");
        }
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

    private void updateVectors(Vec3 axis, float angle) {
        rotQuat = new Quaternion(axis, angle);
        forward = rotQuat.mul(forward).normalize();
        up = rotQuat.mul(up).normalize();
        right = forward.cross(up);
        forwardXZ = new Vec3(forward.getX(), 0, forward.getZ()).normalize();        // to fix velocity vector so speed always is the same, no matter elevation of focus.
    }

    @Override
    public Vec3 getForwardVector() {
        return forward;
    }

    @Override
    public Vec3 getRightVector() {
        return right;
    }

    @Override
    public Vec3 getUpVector() {
        return up;
    }

    @Override
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
    
    public void setRunModifier(float mod){
    	runModifier = mod;
    }

    @Override
    public void collectCollectible(ICollectible collectible) {
        if(collectible.getClass() == Coin.class){
            coinsCollected.add((Coin)collectible);
        }
        else if(collectible.getClass() == Bug.class){
            bugsCollected.add((Bug)collectible);
        }
    }

    @Override
    public void takeDamage(int damage) {
        playerHealth -= damage;
        if (playerHealth <= 0){
            playerHealth = 100;

            pos = spawnPos;
        }
    }
}
