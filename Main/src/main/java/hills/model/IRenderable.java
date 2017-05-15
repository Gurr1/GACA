package hills.model;

import hills.util.math.Mat4;
import hills.util.model.Model;

/**
 * Created by gustav on 2017-05-15.
 */
public interface IRenderable {
    Model getModel();
    Mat4 getMatrix();
}
