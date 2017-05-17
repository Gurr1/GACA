import hills.services.ServiceLocator;
import hills.services.terrain.ITerrainHeightService;
import hills.services.terrain.TerrainService;
import hills.services.terrain.TerrainServiceConstants;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by gustav on 2017-05-17.
 */
public class HeightCalculationTest {
    @Test
    public void testHeightCalculations(){
        ServiceLocator.INSTANCE.getGenerationService().generateWorldImage();
        BufferedImage bf = null;
        try {
            bf = ImageIO.read(new File(TerrainServiceConstants.HEIGHT_MAP_DIRECTORY + TerrainServiceConstants.HEIGHT_MAP_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
        double[][] terrain = new double[bf.getWidth()][bf.getHeight()];
        for(int x = 0 ; x < bf.getWidth(); x++){
            for(int y = 0; y < bf.getHeight(); y++){
                terrain[x][y] = bf.getRGB(x, y);
            }
        }
        Random rand = new Random();
        ITerrainHeightService ts = ServiceLocator.INSTANCE.getTerrainHeightService();       // why doesn't this work
        for(int x = 0 ; x < terrain.length-1; x++) {
            for (int y = 0; y < terrain[0].length-1; y++) {
                if(terrain[x+1][y]>terrain[x][y]){
                    Assert.assertTrue(ts.getHeight(x+1, y) > ts.getHeight(x+rand.nextFloat(), y));
                }
                else{
                    Assert.assertTrue(ts.getHeight(x+1, y) < ts.getHeight(x+rand.nextFloat(), y));
                }
                if(terrain[x][y+1]>terrain[x][y]){
                    Assert.assertTrue(ts.getHeight(x, y+1) > ts.getHeight(x, y+rand.nextFloat()));
                }
                else{
                    Assert.assertTrue(ts.getHeight(x, y+1) < ts.getHeight(x, y+rand.nextFloat()));
                }
            }
        }
    }
}
