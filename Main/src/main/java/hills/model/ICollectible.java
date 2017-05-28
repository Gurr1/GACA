package hills.model;


import hills.util.math.Vec3;

/**
 * Created by Anders on 2017-04-03.
 */
public interface ICollectible extends ICollidable{ //collect
    String getCollectibleName();
    Vec3 getPos();
}
