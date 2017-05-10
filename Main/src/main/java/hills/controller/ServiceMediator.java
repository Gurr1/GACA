package hills.controller;

import hills.services.generation.GenerationMediator;
import hills.services.generation.IGenerationMediator;
import hills.services.terrain.TerrainIService;
import hills.services.terrain.TerrainServiceConstants;
import hills.util.math.Vec3;

import java.util.Random;

/**
 * Created by gustav on 2017-04-29.
 */
public enum ServiceMediator {
    INSTANCE();
    private TerrainIService terrainService;
    private IGenerationMediator generator = new GenerationMediator();
    ServiceMediator(){
        terrainService = new TerrainIService();
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
