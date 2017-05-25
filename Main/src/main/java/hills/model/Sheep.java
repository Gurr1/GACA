package hills.model;

import hills.util.math.Mat4;
import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;
import hills.util.model.Model;

import java.util.Random;

/**
 * Created by Anders on 2017-04-03.
 */
public class Sheep extends Creature{
    /**
     * {@inheritDoc}
     */
    float radius;
    Random rand = new Random();
    private float move = 0;


    public Sheep(Vec3 pos, Model model){
        this.model = model;
        this.pos = pos;
        this.healthPoints = 20;
        this.speed = 3;
        this.maxHealth = 20;
        this.radius = 3;
    }

    @Override
    public Sphere getBoundingSphere() {
        return new Sphere(pos, radius);
    }

    @Override
    public Mat4 getMatrix() {
        Mat4 matrix = Mat4.identity();
        matrix = matrix.translate(pos);
        matrix = matrix.scale(radius, 5, radius);
        return matrix;
    }

    public void updatePosition() {
        //TODO
    }

    @Override
    public void updateMovable(float delta) {
        pos = pos.add(velocity.mul(delta));
        //TODO
    }
}
