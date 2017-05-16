package hills.model;

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
        this.speed = 1;
        this.maxHealth = 20;
        this.radius = 1;
    }

    @Override
    public Sphere getBoundingSphere() {
        return new Sphere(pos, radius);
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
