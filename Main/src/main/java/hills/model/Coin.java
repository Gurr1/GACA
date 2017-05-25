package hills.model;

import hills.util.math.Mat4;
import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;
import hills.util.model.Model;

/**
 * Created by Anders on 2017-03-30.
 */
public class Coin extends CollectibleObject{

    private float radius = 3;
    private Mat4 matrix;

    public Coin(Vec3 pos, Model model) {
        super(pos, model);
        this.pos = pos;
        matrix = Mat4.identity();
        matrix = matrix.scale(radius,5,1);
        matrix = matrix.translate(pos);

    }


    @Override
    public Sphere getBoundingSphere() {
        return new Sphere(pos, radius);
    }

    @Override
    public String getCollectibleName() {
        return "Coin";
    }

    @Override
    public Vec3 getPos() {
        return new Vec3(pos);
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public Mat4 getMatrix() {
        return matrix;
    }

    @Override
    public void updateMovable(float delta) {
        // does nothing, because it shouldn't move.
    }
}
