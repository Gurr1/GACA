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
    public double getHeight(int x, int z){
        return terrainService.getHeight(x, z);
    }
    public void generateMap(){
        generator.generateWorldImage();
    }
    public double generateDirection(float x){
        return generator.generateDirection(x);
    }

}
