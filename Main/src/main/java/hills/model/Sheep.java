package hills.model;

import hills.util.math.Mat4;
import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;
import hills.util.model.Model;

import java.util.Random;

/**
 *
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
        this.healthPoints = 5;
        this.speed = 3;
        this.maxHealth = 5;
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

    @Override
    public void takeShotDamage(int shotDamage){
        healthPoints -= shotDamage;
        if (healthPoints <= 0){
            //System.out.println("Goat is dead!");
            healthPoints = maxHealth;
            Random rand = new Random();
            pos = new Vec3(rand.nextInt(2000), 0, rand.nextInt(2000));
        }
        //System.out.println("Sheep damaged!");
    }

    @Override
    public Sphere getInteractionSphere(){
        return new Sphere(pos, radius);
    }
}
