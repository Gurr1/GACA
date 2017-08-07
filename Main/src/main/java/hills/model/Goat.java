package hills.model;

import hills.util.math.Mat4;
import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;
import hills.util.model.Model;

public class Goat extends Creature implements IHarmful{

    float radius;
    int damagePoints;

    public Goat(Vec3 pos, Model model){
        this.model = model;
        this.pos = pos;
        this.healthPoints = 20;
        this.speed = 3;
        this.maxHealth = 20;
        this.radius = 10;
        this.damagePoints = 3;
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

    @Override
    public int getDamagePoints() {
        return damagePoints;
    }

    @Override
    public void updateMovable(float delta) {
        pos = pos.add(velocity.mul(delta));
    }
}
