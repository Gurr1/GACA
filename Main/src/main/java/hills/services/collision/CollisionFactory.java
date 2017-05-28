package hills.services.collision;

/**
 * Created by corne on 5/27/2017.
 */
public class CollisionFactory {

    private CollisionFactory(){

    }

    public static CollisionDetection collisionDetection = null;

    public static ICollisionDetection getCollisionDetectionInstance(){
        if(collisionDetection == null)
            collisionDetection = new CollisionDetection();

        return collisionDetection;
    }
}
