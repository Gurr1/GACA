package hills.services.collision;

import hills.model.ICollidable;
import hills.services.Service;

/**
 * @Author Gustav Engsmyre
 * @RevisedBy Anders Hansson
 */
public class CollisionDetection implements Service, ICollisionDetection{

    protected CollisionDetection(){

    }

    public boolean isColliding(ICollidable collidable1, ICollidable collidable2){
        return collidable1.getBoundingSphere().intersects(collidable2.getBoundingSphere());
    }

    @Override
    public void cleanUp() {

    }

}
