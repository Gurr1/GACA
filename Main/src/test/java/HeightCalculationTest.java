import hills.services.ServiceLocator;
import hills.services.terrain.ITerrainHeightService;
import hills.services.terrain.TerrainServiceConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Created by gustav on 2017-05-17.
 */
public class HeightCalculationTest {
    @Test
    public void testHeightCalculations(){
        ServiceLocator.INSTANCE.getTerrainGenerationService().generateWorldImage();
        ITerrainHeightService ts = ServiceLocator.INSTANCE.getTerrainHeightTestService();       // why doesn't this work
        double[][] terrain = new double[TerrainServiceConstants.TERRAIN_WIDTH][TerrainServiceConstants.TERRAIN_HEIGHT];
        Random rand = new Random();
        for(int x = 0 ; x < terrain.length-1; x++){
            for(int y = 0; y < terrain[0].length-1; y++){
                terrain[x][y] = ts.getHeight(x,y);
            }
        }
        for(int x = 0 ; x < terrain.length-2; x++) {
            for (int y = 0; y < terrain[0].length-2; y++) {
                float r = rand.nextFloat();
               if(terrain[x+1][y]>terrain[x][y]){
                    Assert.assertTrue(ts.getHeight(x+1, y) >= ts.getHeight(x+r, y));
                }
                if(terrain[x+1][y]<terrain[x][y]){
                    Assert.assertTrue(ts.getHeight(x+1, y) <= ts.getHeight(x+r, y));
                }
                if(terrain[x][y+1]>terrain[x][y]){
                    Assert.assertTrue(ts.getHeight(x, y+1) >= ts.getHeight(x, y+r));
                }
                if(terrain[x][y+1]<terrain[x][y]){
                    Assert.assertTrue(ts.getHeight(x, y+1) <= ts.getHeight(x, y+r));
                }
            }
        }
    }
}
