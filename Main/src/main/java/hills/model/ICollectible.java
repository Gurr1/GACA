package hills.model;

import hills.util.math.shape.Sphere;

/**
 * Created by Anders on 2017-04-03.
 */
public interface ICollectible { //collect
    Sphere getBoundingSphere();
    String getCollectibleName();
}
