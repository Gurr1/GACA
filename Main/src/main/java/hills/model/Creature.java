package hills.model;

import hills.util.math.Mat4;
import hills.util.math.Quaternion;
import hills.util.math.Vec2;
import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;
import hills.util.model.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author Anders Hansson
 * @RevisedBy Gustav Engsmyre
 */
public abstract class Creature implements IWoundable, IMovable, ICollidable, IAIMovable, IRenderable{

    /**
     * {@inheritDoc}
     */

    protected Vec3 pos;
    @Setter @Getter protected float speed = 1;
    protected int healthPoints;
    protected int maxHealth;
    protected Model model;
    protected Vec3 velocity = new Vec3(0,0,0);
    protected float yaw;
    protected float pitch;
    protected Vec3 forward = new Vec3(0,0,-1.0f);
    protected Vec3 right = new Vec3(1.0f,0,0);
    protected Vec3 up = new Vec3(0,1.0f,0);
    protected Vec3 velocityX = new Vec3(1,0,0);
    protected Vec3 velocityZ = new Vec3(1,0,0);
    protected Mat4 matrix;

    @Override
    public abstract Sphere getBoundingSphere();

    public void updatePosition(Vec3 diff) {
        this.pos = this.pos.add(diff);
    }

    @Override
    public void setPosition(Vec3 pos) {
        this.pos = pos;
    }

    @Override
    public Vec2 get2DPos() {
        return new Vec2(pos.getX(), pos.getZ());
    }

    @Override
    public Vec3 get3DPos() {
        return pos;
    }

    @Override
    public int getHealth(){return healthPoints;}

    @Override
    public int getMaxHealth(){return maxHealth;}

    @Override
    public void takeDamage(int amount){
        healthPoints -= amount;
    }

    public Vec3 getVelocity(){
        return velocity;
    }

    protected float getHeight(){          // This is a Horrible implementation, should be changed.
        return pos.getZ();
    }
    @Override
    public void setHeight(float height){
        pos = new Vec3(pos.getX(), height, pos.getZ());
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = yaw;
        updateVectors(up, yaw);
        updateVelocity();
    }

    @Override
    public void updatePitch(float deltaPitch) {
        pitch += fixDegrees(pitch + deltaPitch);
        updateVectors(right, pitch);
        updateVelocity();
    }

    private void updateVelocity() {
            int yDir = 1;
            int xDir = 1;
            if((velocityZ.add(right).getLength())<1){       // Checks if velocity is backwards
                yDir = -1;
            }
            if((velocityX.add(forward).getLength())<1){     // Checks is velocity is to the left.
                xDir = -1;
            }
            float xVelocity = velocityX.getLength()*xDir;
            float yVelocity = velocityZ.getLength()*yDir;
            velocityX = forward.mul(xVelocity);
            velocityZ = right.mul(yVelocity);
            velocity = velocityX.add(velocityZ).normalize().mul(speed);
    }

    @Override
    public void updateYaw(float deltaYaw) {
        yaw = fixDegrees(yaw+deltaYaw);
        updateVectors(up, yaw);
        updateVelocity();
    }


    private float fixDegrees(float degree) {
        degree %= 360;
        if (degree <= 0)
            degree += 360;

        return degree;
    }

    @Override
    public void addGravityVelocity(float delta) {

    }
    private void updateVectors(Vec3 axis, float angle){
            Quaternion rotQuat = new Quaternion(axis, angle);
            forward = rotQuat.mul(forward).normalize();
            up = rotQuat.mul(up).normalize();
            right = forward.cross(up);
        }
    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public abstract Mat4 getMatrix();
}
