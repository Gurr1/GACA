/*import hills.services.ServiceLocator;
import hills.services.generation.GenerationMediator;
import hills.services.generation.IGenerationMediator;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Random;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by gustav on 2017-03-28.
 */

/*
Once the File getter is implemented in services, use that to fetch file.
public class terrainTest {

	IGenerationMediator generator;

	@Before
	public void testTerrain() {
		Random random = new Random();
        generator = new GenerationMediator();
		assertNotNull(generator);
	}

    @Test
    public void testCreateIsland(){
        generator.generateWorldImage();
        BufferedImage bf = ImageIO.read(ServiceLocator.INSTANCE.getFileService.readImage());
        double[][] terrain =
        double smallest = 1;
        double largest = 0;
        for(int x = 0; x<terrain.length; x++){
            for (int y = 0; y < terrain[0].length; y++){
                assertTrue(terrain[x][y]<=255);
                assertTrue(terrain[x][y]>=0);
                if(smallest>terrain[x][y]){
                    smallest = terrain[x][y]();
                }
                if(largest<terrain[x][y]){
                    largest = terrain[x][y];
                }
            }
        }
        assertTrue(largest>0.95);
        assertTrue(smallest<0.05);
    }
    @Test
    public void testCreateFinalMap(){
        generator.generateWorldImage();
        BufferedImage bf = ImageIO.read(ServiceLocator.INSTANCE.getFileService.readImage());
        double[][] terrain = bf.get
        double smallest = 255;
        double largest = 0;
        for(int x = 0; x<terrain.length; x++){
            for (int y = 0; y < terrain[0].length; y++){
                assertTrue(terrain[x][y]<=255);
                assertTrue(terrain[x][y]>=0);
                if(terrain[x][y] < smallest){
                    smallest = terrain[x][y];
                }
                if(largest < terrain[x][y]){
                    largest = terrain[x][y];
                }
            }
        }
        assertTrue(largest>0.95);
        assertTrue(smallest<0.05);
    }
}
*/