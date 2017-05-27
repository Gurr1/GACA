package hills.services.collision;

import hills.model.ICollidable;

/**
 * Created by corne on 5/27/2017.
 */
public interface ICollisionDetection {
    boolean isColliding(ICollidable collidable1, ICollidable collidable2);
}
