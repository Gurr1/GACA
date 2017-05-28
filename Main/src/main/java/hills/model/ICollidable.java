package hills.model;

import hills.util.math.shape.Sphere;

/**
 *
 */
public interface ICollidable {

    /**
     * gets the Sphere of the object for calculation of intersect
     * @return
     */
    Sphere getBoundingSphere();
}
