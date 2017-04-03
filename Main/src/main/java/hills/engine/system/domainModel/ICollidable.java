package hills.engine.system.domainModel;

import hills.engine.math.shape.Sphere;

/**
 * Created by Anders on 2017-03-30.
 */
public interface ICollidable {

    /**
     * gets the Sphere of the object for calculation of intersect
     * @return
     */
    Sphere getBoundingSphere();
}