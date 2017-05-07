package hills.model;

/**
 * Created by gustav on 2017-05-06.
 */
public interface PlayerMovable extends IMovable{
    enum Direction{
        FORWARD, RIGHT, LEFT, BACK, FORWARD_SPRINT;
    }
    void updateVelocity(Direction direction, boolean AddOrRemove);
}
