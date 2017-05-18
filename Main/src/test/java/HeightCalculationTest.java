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
        ServiceLocator.INSTANCE.getGenerationService().generateWorldImage();
        ITerrainHeightService ts = ServiceLocator.INSTANCE.getTerrainHeightTestService();       // why doesn't this work
        double[][] terrain = new double[TerrainServiceConstants.TERRAIN_WIDTH][TerrainServiceConstants.TERRAIN_HEIGHT];
        Random rand = new Random();
        for(int x = 700 ; x < 1200; x++){
            for(int y = 700; y < 1200; y++){
                float r = rand.nextFloat();
                System.out.println("new");
                System.out.println(ts.getHeight(x,y+r));
               // System.out.println(ts.getHeight(x,y+1));
                //System.out.println(r);
                //System.out.println(ts.getHeight(x,y+r));
            }
        }
        Assert.assertTrue(false);
        for(int x = 0 ; x < terrain.length-2; x++) {
            for (int y = 0; y < terrain[0].length-2; y++) {
            //    System.out.println(x + " " + y);
            //    System.out.println(ts.getHeight(x+1, y));
            //    System.out.println(ts.getHeight(x, y));
      /*          if(terrain[x+1][y]>terrain[x][y]){
                    Assert.assertTrue(ts.getHeight(x+1, y) > ts.getHeight(x+r, y));
                }
                if(terrain[x+1][y]<terrain[x][y]){
                    Assert.assertTrue(ts.getHeight(x+1, y) <= ts.getHeight(x+r, y));
                }
                System.out.println(ts.getHeight(x, y+1));
                System.out.println(ts.getHeight(x, y));
                if(terrain[x][y+1]>terrain[x][y]){
                    Assert.assertTrue(ts.getHeight(x, y+1) > ts.getHeight(x, y+r));
                }
                if(terrain[x][y+1]<terrain[x][y]){
                //    System.out.println(ts.getHeight(x,y+r));
                    Assert.assertTrue(ts.getHeight(x, y+1) <= ts.getHeight(x, y+r));
                }*/
            }
        }
    }
}
