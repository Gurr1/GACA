package hills.engine.system.domainModel;

import hills.engine.math.Vec3;
import hills.engine.math.shape.Sphere;
import hills.engine.model.Model;

/**
 * Created by Anders on 2017-04-03.
 */
public class Sheep extends Creature{
    /**
     * {@inheritDoc}
     */
    float radius;

    Sheep(Model model, Vec3 pos){
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
}
