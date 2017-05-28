package hills.services.collision;

/**
 * @author Cornelis T Sjöbeck
 */
public class CollisionFactory {

    private CollisionFactory(){

    }

    private static CollisionDetection collisionDetection = null;

    public static ICollisionDetection getCollisionDetectionInstance(){
        if(collisionDetection == null)
            collisionDetection = new CollisionDetection();

        return collisionDetection;
    }
}
