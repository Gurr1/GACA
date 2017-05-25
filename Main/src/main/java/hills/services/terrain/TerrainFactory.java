package hills.services.terrain;

/**
 * Created by corne on 5/24/2017.
 */
public class TerrainFactory {

    private TerrainFactory(){

    }

    private static TerrainService terrainService = null;


    public static TerrainService getTerrainServiceInstance(boolean b){
        if(terrainService == null)
            terrainService = new TerrainService(b);

        return terrainService;
    }
}
