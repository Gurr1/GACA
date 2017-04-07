package hills.engine.system.domainModel;

import hills.engine.math.shape.Sphere;

/**
 * Created by Anders on 2017-04-03.
 */
public interface ICollectible { //collect
    Sphere getBoundingSphere();
    String getCollectibleName();
}
