package hills.model;


import hills.util.math.Vec3;

/**
 *
 */
public interface ICollectible extends ICollidable{ //collect
    String getCollectibleName();
    Vec3 getPos();
}
