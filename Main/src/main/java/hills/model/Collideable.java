package hills.model;

import hills.util.math.shape.Sphere;

/**
 * Created by Anders on 2017-03-30.
 */
public interface Collideable {
    Sphere getBoundingSphere();
}
