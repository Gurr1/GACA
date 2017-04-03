package hills.engine.system.domainModel;

import hills.engine.math.Vec2;
import hills.engine.math.Vec3;
import hills.engine.math.shape.Sphere;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Anders on 2017-03-30.
 */
public class Player implements ICollidable, IMovable {

    /**
     * {@inheritDoc}
     */

    private Vec3 pos;
    @Getter private float pitch = 0;
    @Getter private float yaw = 0;
    @Setter private float radius = 1;

    //<editor-fold desc="Constructors">

    public Player(Vec3 pos, float radius) {
        this.pos = pos;
        this.radius = radius;
    }

    public Player(Vec3 pos, float pitch, float yaw) {
        this.pos = pos;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Player(Vec3 pos) {
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
    }

    /**
     * adds to the current yaw and corrects it to the 0 - 360 degree range
     * @param diffYaw the amount that should be added to the yaw
     */
    public void updateYaw(float diffYaw) {
        this.yaw = fixDegrees(diffYaw + this.yaw);
    }

    @Override
    public void updatePosition(Vec3 pos) {
        this.pos = this.pos.add(pos);
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
    public void setPosition(Vec3 pos) {this.pos = pos;}
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
}
