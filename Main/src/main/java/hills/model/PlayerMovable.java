package hills.model;

import hills.util.math.Vec3;

/**
 * Created by gustav on 2017-05-06.
 */
public interface PlayerMovable extends IMovable{
    enum Direction{
        FORWARD, RIGHT, LEFT, BACK, FORWARD_SPRINT;
    }
    void addVelocity(PlayerMovable.Direction direction, boolean pressed);
    Vec3 getForwardVector();
    Vec3 getRightVector();
    Vec3 getUpVector();
}
