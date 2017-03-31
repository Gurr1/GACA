package hills.engine.system.domainModel;

import hills.engine.math.Vec3;
import hills.engine.math.shape.Sphere;
import hills.engine.model.Model;
import lombok.Getter;

/**
 * Created by Anders on 2017-03-30.
 */
public class Coin implements Collideable {

    @Getter private final Vec3 pos;
    private float radius = 1;
    private final Model model;

    public Coin(Vec3 pos, float radius, Model model) {
        this.pos = pos;
        this.radius = radius;
        this.model = model;
    }

    public Coin(Vec3 pos, Model model) {
        this.pos = pos;
        this.model = model;
    }

    @Override
    public Sphere getBoundingSphere() {
        return new Sphere(pos, radius);
    }
}
