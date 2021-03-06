package hills.model;

import hills.util.math.Vec2;
import hills.util.math.Vec3;

/**
 *
 */
public interface IMovable {


    /**
     * sets the objects position to that of the parameter
     * @param pos position to be set
     */
    void setPosition(Vec3 pos);


    /**
     * gives the x,y coordinates in form of a Vec2
     * @return the vector with the x,y coordinates
     */
    Vec2 get2DPos();

    /**
     * gives the x,y,z coordinates in form of a Vec3
     * @return the vector with the x,y,z coordinates
     */
    Vec3 get3DPos();

    Vec3 getVelocity();

    void setHeight(float height);

    void setPitch(float pitch);

    void setYaw(float yaw);

    void updatePitch(float deltaPitch);

    void updateYaw(float deltaYaw);

    void updateMovable(float delta);

    void addGravityVelocity(float delta);
}
