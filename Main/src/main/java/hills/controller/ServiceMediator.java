package hills.controller;

import hills.services.generation.MapFactory;
import hills.services.generation.IMapFactory;
import hills.services.terrain.TerrainService;

/**
 * Created by gustav on 2017-04-29.
 */
public enum ServiceMediator {
    INSTANCE();
    private TerrainService terrainService;
    private IMapFactory generator = new MapFactory();
    ServiceMediator(){
        terrainService = TerrainService.INSTANCE;
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

}
