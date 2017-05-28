package hills.model;

import hills.util.math.Vec3;
import hills.util.model.Model;

/**
 * @Author Gustav Engsmyre
 */
public abstract class CollectibleObject extends Creature implements ICollectible, IRenderable{

    public CollectibleObject(Vec3 pos, Model model){
        this.pos = pos;
        this.model = model;
    }
}
