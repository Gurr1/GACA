package hills.services.terrain;

/**
 * @author Cornelis T Sjöbeck
 */
public class TerrainFactory {

    private TerrainFactory(){

    }

    private static TerrainService terrainService = null;
    private static TerrainChunkService terrainChunkService = null;


    public static ITerrainHeightService getTerrainHeightServiceInstance(boolean b){
        if(terrainService == null)
            terrainService = new TerrainService(b);

        return terrainService;
    }

    public static ITerrainRenderDataService getTerrainRenderDataServiceInstance(boolean b){
        if(terrainService == null)
            terrainService = new TerrainService(b);

        return terrainService;
    }

    public static ITerrainTreeService getTerrainTreeServiceInstance(boolean b){
        if(terrainService == null)
            terrainService = new TerrainService(b);

        return terrainService;
    }

    public static ITerrainChunkService getTerrainChunkServiceInstance(boolean b){
        if(terrainChunkService == null)
            terrainChunkService = new TerrainChunkService();

        return terrainChunkService;
    }
}
