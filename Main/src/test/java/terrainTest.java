import hills.Anton.engine.math.Vec3;
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
        t = new Terrain(1);
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
        System.out.println(largest);
        System.out.println(smallest);
        assertTrue(largest>250);
        assertTrue(smallest<110);
    }
}
