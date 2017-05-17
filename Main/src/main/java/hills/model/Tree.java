package hills.model;

import hills.util.math.Mat4;
import hills.util.math.Vec3;
import hills.util.model.Model;

/**
 * Created by gustav on 2017-05-15.
 */
public class Tree extends ImmovableObject{
        private Mat4 matrix;

    public Tree(Vec3 position, Model model) {
        super(position, model);
        matrix = Mat4.identity();
        matrix = matrix.scale(5,150,5);
        matrix = matrix.translate(position);
    }

    @Override
    public Mat4 getMatrix() {
        return matrix;
    }
}
