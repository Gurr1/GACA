package hills.services.collision;

import hills.model.ICollidable;

/**
 * Created by gustav on 2017-05-22.
 */
public class CollisionDetection {

    public boolean isColliding(ICollidable collidable1, ICollidable collidable2){
        return collidable1.getBoundingSphere().intersects(collidable2.getBoundingSphere());
    }
}
