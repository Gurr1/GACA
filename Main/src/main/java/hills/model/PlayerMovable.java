package hills.model;

import hills.util.math.Vec3;

/**
 *
 */
public interface PlayerMovable extends IMovable{
    Vec3 getHeadPos();

    enum Direction{
        FORWARD, RIGHT, LEFT, BACK, SPRINT, UP;
    }
    void addVelocity(PlayerMovable.Direction direction, boolean pressed);
    Vec3 getForwardVector();
    Vec3 getRightVector();
    Vec3 getUpVector();
}
