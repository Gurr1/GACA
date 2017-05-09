package hills.engine.system.domainModel;

import hills.engine.math.Vec3;
import hills.engine.math.shape.Sphere;
import hills.engine.model.Model;
import lombok.Getter;

/**
 * Created by corne on 5/3/2017.
 */
public class Food implements ICollectible {

    @Getter
    private final Vec3 pos;
    private float radius = 1;
    private final Model model;

    public Food(Vec3 pos, float radius, Model model) {
        this.pos = pos;
        this.radius = radius;
        this.model = model;
    }

    public Food(Vec3 pos, Model model) {
        this.pos = pos;
        this.model = model;
    }

    @Override
    public Sphere getBoundingSphere() {
        return new Sphere(pos, radius);
    }

    @Override
    public String getCollectibleName() {
        return "Food";
    }
}
