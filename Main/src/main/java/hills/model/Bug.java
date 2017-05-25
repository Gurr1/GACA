package hills.model;

import hills.util.math.Mat4;
import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;
import hills.util.model.Model;

/**
 * Created by gustav on 2017-05-23.
 */
public class Bug extends CollectibleObject implements ICollectible{
    private float radius = 1.5f;

    public Bug(Vec3 position, Model model){
        super(position, model);
        this.pos = position;
        this.model = model;
        this.healthPoints = 1;
        this.speed = 5;
    }
    @Override
    public String getCollectibleName() {
        return "Bug";
    }

    @Override
    public Vec3 getPos() {
        return pos;
    }

    @Override
    public void updateMovable(float delta) {
        this.pos = pos.add(velocity.mul(delta));
    }

    @Override
    public Sphere getBoundingSphere() {
        return new Sphere(pos,radius);
    }

    @Override
    public Mat4 getMatrix() {
        Mat4 matrix = Mat4.identity();
        matrix = matrix.translate(pos);
        matrix = matrix.scale(radius,1,radius);
        return matrix;
    }
}
