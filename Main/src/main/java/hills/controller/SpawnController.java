package hills.controller;

import hills.services.terrain.TerrainServiceConstants;
import hills.util.math.Vec3;

import java.util.Random;

/**
 * Created by gustav on 2017-05-04.
 */
public class SpawnController {
    public Vec3 generateSpawnLocation(){
        Random random = new Random();
        int x;
        int z;
        float y;
        do {
            x = random.nextInt();
            z = random.nextInt();
            y = ServiceMediator.INSTANCE.getHeight(x, z);
        }while(y < TerrainServiceConstants.WATER_HEIGHT);
        return new Vec3(x, y, z);
    }
}
