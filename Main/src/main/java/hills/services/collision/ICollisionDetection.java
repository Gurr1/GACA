package hills.services.collision;

import hills.model.ICollidable;

/**
 * @author Cornelis T Sjöbeck
 */
public interface ICollisionDetection {
    boolean isColliding(ICollidable collidable1, ICollidable collidable2);
}
