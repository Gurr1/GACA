package hills.model;

import hills.util.math.Mat4;
import hills.util.model.Model;

/**
 *
 */
public interface IRenderable {
    Model getModel();
    Mat4 getMatrix();
}
