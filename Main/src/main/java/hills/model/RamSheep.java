package hills.model;

import hills.util.math.Mat4;
import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;

import java.util.Random;

/**
 * @author Cornelis T Sjöbeck
 */
public class RamSheep extends Mob {

    private float radius;
    protected Random rand = new Random();
    private float move = 0;
    private final int damagePoints = 10;

    public RamSheep(Vec3 pos){
        this.pos = pos;
        this.healthPoints = 30;
        this.speed = 2;
        this.maxHealth = 30;
        this.radius = 1;
    }

    @Override
    void dealDamage(int health) {
        int damage = rand.nextInt(40) + 20;
        health -= damage;
    }

    @Override
    public Sphere getBoundingSphere() {
        return null;
        //TODO
    }

    @Override
    public Mat4 getMatrix() {
        Mat4 matrix = Mat4.identity();
        matrix = matrix.translate(pos);
        matrix = matrix.translate(radius, 5, radius);
        return matrix;
    }

    public void moveRandomly() {
        //TODO
    }

    @Override
    public void updateMovable(float delta) {

    }

    //TODO
}
