package hills.services.generation;



import hills.model.Coin;
import hills.model.ICollectible;
import hills.services.ServiceLocator;
import hills.services.terrain.ITerrainHeightService;
import hills.util.math.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Ander on 2017-05-03.
 */
public class CollectiblePlacer {
    private int nrOfPlacements;
    private List<ICollectible> placedObjects;
    private int maxTry = 100;
    private Random random;
    private int Height = 2048;
    private int Width = 2048;
    private float minRadius = 10;
    private ITerrainHeightService terrainHeightService;

    public CollectiblePlacer(int nrOfPlacements){
        this.nrOfPlacements = nrOfPlacements;
        this.terrainHeightService = ServiceLocator.INSTANCE.getTerrainHeightService();
        random = new Random();
        placedObjects = new ArrayList<>();

        add(random.nextInt()%Height,random.nextInt()%Width);
    }

    /**
     * Places the amount of coins outside of a minimum radius of the collectibles with a max of 100 tries per collectible
     * @return the list of the placed collectibles
     */
    public List<ICollectible> getPlacedCollectibles(){
        for(int i = 0 ; i < nrOfPlacements-1 ; i++){
            for(int j = 0 ; j < maxTry ; j++){
                if(addCollectible())
                    break;
            }
        }
        return placedObjects;
    }

    /**
     * gets a random position and checks if its to close to others and of valid height
     * @return false if its to close to others
     */
    private boolean addCollectible() {
        int x = random.nextInt() % Height;
        int z = random.nextInt() % Width;

        for (ICollectible c : placedObjects) {
            float tempX = x - c.getPos().getX();
            float tempZ = z - c.getPos().getZ();
            if (tempX * tempX + tempZ * tempZ <= minRadius * minRadius || terrainHeightService.getHeight(x,z) == 0.0f)
                return false;
        }
        add(x,z);
        return true;
    }

    /**
     *  adds a specified collectible to the coord parameters
     * @param x-coord
     * @param z-coord
     */
    private void add(float x, float z){
        float y = terrainHeightService.getHeight(x,z);
        if(y!=0.0f)
            placedObjects.add(new Coin(new Vec3(x,y,z)));

    }

}
