package hills.engine.system.domainModel;

import hills.engine.math.Vec3;
import hills.engine.math.shape.Sphere;
import lombok.Getter;

/**
 * Created by Anders on 2017-03-30.
 */
public class Player implements Colideable {

    @Getter private Vec3 pos;
    @Getter private float pitch = 0;
    @Getter private float yaw = 0;
    private float radius = 1;

    public Player (Vec3 pos, float radius){
        this.pos = pos;
        this.radius = radius;
    }

    public Player (Vec3 pos){
        this.pos = pos;
    }


    public void updatePitch(float pitch) {
        this.pitch += pitch;
        this.pitch %= 360;
    }

    public void updateYaw(float yaw) {
        this.yaw += yaw;
        this.yaw %= 360;
    }

    public void setYaw(float yaw) {
        this.yaw = (yaw % 360);
    }

    public void setPitch(float pitch) {
        this.pitch = (pitch % 360);
    }

    public void updateDirection(float pitch, float yaw){
        updatePitch(pitch);
        updateYaw(yaw);
    }

    @Override
    public Sphere getBoundingSphere() {
        return new Sphere(pos, radius);
    }


}
