package hills.engine.system.domainModel;

import hills.engine.math.Vec3;
import hills.engine.math.shape.Sphere;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Anders on 2017-03-30.
 */
public class Player implements Collideable {

    @Setter @Getter private Vec3 pos;
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
    public void updatePitch(float pitch) {
        this.pitch = fixDegrees(pitch + this.pitch);
    }

    public void updateYaw(float yaw) {
        this.yaw = fixDegrees(yaw + this.yaw);
    }

    public void updatePosition(Vec3 pos) {
        this.pos = this.pos.add(pos);
    }

    public void updateDirection(float pitch, float yaw) {
        updatePitch(pitch);
        updateYaw(yaw);
    }
   // </editor-fold>

    //<editor-fold desc = "Setters">
    public void setYaw(float yaw) {
        this.yaw = fixDegrees(yaw);
    }

    public void setPitch(float pitch) {
        this.pitch = fixDegrees(pitch);
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
}
