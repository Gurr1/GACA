package hills.model;

import hills.util.math.Mat4;
import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;
import hills.util.model.Model;

/**
 * Created by gustav on 2017-04-25.
 */
public class Rock extends ImmovableObject{
    private float radius = 5;

    public Rock(Vec3 position, Model model) {
        super(position, model);
    }

    @Override
    public Mat4 getMatrix() {
        Mat4 matrix = Mat4.identity();
        matrix = matrix.scale(5,5,50);
        matrix = matrix.translate(position);
        return matrix;
    }

    @Override
    public Sphere getBoundingSphere() {
        return new Sphere(position, radius);
    }
}
