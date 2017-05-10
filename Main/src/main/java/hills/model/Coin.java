package hills.model;

import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;
import hills.util.model.Model;
import lombok.Getter;

/**
 * Created by Anders on 2017-03-30.
 */
public class Coin implements ICollectible {

    @Getter private final Vec3 pos;
    private float radius = 1;
    protected Model model;

    public Coin(Vec3 pos, float radius) {
        this.pos = pos;
        this.radius = radius;
    }

    public Coin(Vec3 pos) {
        this.pos = pos;
    }

    @Override
    public Sphere getBoundingSphere() {
        return new Sphere(pos, radius);
    }

    @Override
    public String getCollectibleName() {
        return "Coin";
    }

}
