package hills.model;

import hills.util.math.Vec3;
import hills.util.model.Model;

/**
 * Created by gustav on 2017-05-22.
 */
public abstract class CollectibleObject extends Creature implements ICollectible, IRenderable{

    public CollectibleObject(Vec3 pos, Model model){
        this.pos = pos;
        this.model = model;
    }
}
