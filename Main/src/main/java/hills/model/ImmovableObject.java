package hills.model;

import hills.util.math.Vec3;
import hills.util.model.Model;

/**
 * Created by gustav on 2017-04-25.
 */
public abstract class ImmovableObject implements IRenderable, ICollidable{
    protected Vec3 position;
    Model model;

    public ImmovableObject(Vec3 position, Model model ){
        this.position = position;
        this.model = model;
    }

    @Override
    public Model getModel() {
        return model;
    }
}
