import hills.Gurra.Terrain;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by gustav on 2017-03-28.
 */
public class terrainTest {

    Terrain t;

    @Before
    public void testTerrain(){
        Random random = new Random();
        t = new Terrain(random.nextLong());
        assertNotNull(t);
    }
    @Test
    public void testCreateHeightMap(){
        int[][] terrain = t.createHeightMap();
        int smallest = 100;
        int largest = 100;
        for(int x = 0; x<terrain.length; x++){
            for (int y = 0; y < terrain[0].length; y++){
                assertTrue(terrain[x][y]<=255);
                assertTrue(terrain[x][y]>=0);
                if(smallest>terrain[x][y]){
                    smallest = terrain[x][y];
                }
                if(largest<terrain[x][y]){
                    largest = terrain[x][y];
                }
            }
        }
        assertTrue(largest>250);
        assertTrue(smallest<110);
    }

    @Test
    public void testCreateIsland(){
        double[][] terrain = t.createIsland();
        double smallest = 1;
        double largest = 0;
        for(int x = 0; x<terrain.length; x++){
            for (int y = 0; y < terrain[0].length; y++){
                assertTrue(terrain[x][y]<=1);
                assertTrue(terrain[x][y]>=0);
                if(smallest>terrain[x][y]){
                    smallest = terrain[x][y];
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
        double[][] terrain = t.createfinalIsland();
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
