package hills.controller;

import hills.model.World;
import hills.util.math.Vec3;

/**
 * Created by gustav on 2017-05-04.
 */
public class NPCHeightController{
    World w;
    NPCHeightController(){
        w = World.getInstance();
    }

    public void updateNPC(){
        for(int i = 0; i<w.getNNPCs(); i++) {
            Vec3 pos = w.getCreaturePosition(i);
            w.setNPCHeight(ServiceMediator.INSTANCE.getHeight(pos.getX(), pos.getZ()), i);
        }
    }
}
