package hills.model;

import hills.util.math.Vec3;

/**
 *
 */
public interface PlayerMovable extends IMovable{
    Vec3 getHeadPos();

    enum Direction{
        FORWARD, RIGHT, LEFT, BACK, SPRINT, UP, DOWN;
    }
    void addVelocity(PlayerMovable.Direction direction, boolean pressed);
    void addVelocity(Vec3 v);
    Vec3 getForwardVector();
    Vec3 getRightVector();
    Vec3 getUpVector();
    void setRunModifier(float mod);
}
