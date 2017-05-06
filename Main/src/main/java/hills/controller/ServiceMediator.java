package hills.controller;

import hills.services.generation.MapFactory;
import hills.services.generation.IMapFactory;
import hills.services.terrain.TerrainService;
import hills.services.terrain.TerrainServiceConstants;
import hills.util.math.Vec3;

import java.util.Random;

/**
 * Created by gustav on 2017-04-29.
 */
public enum ServiceMediator {
    INSTANCE();
    private TerrainService terrainService;
    private IMapFactory generator = new MapFactory();
    ServiceMediator(){
        terrainService = TerrainService;
    }
    public float getHeight(float x, float z){
        return terrainService.getHeight(x, z);
    }
    public void generateMap(){
        generator.generateWorldImage();
    }
    public double generateDirection(float x){
        return generator.generateDirection(x);
    }
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
