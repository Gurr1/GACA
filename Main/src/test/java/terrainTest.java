import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by gustav on 2017-03-28.
 */

/*
Once the File getter is implemented in services, use that to fetch file.
 */
/*public class terrainTest {

	IMapFactory generator;

	@Before
	public void testTerrain() {
		Random random = new Random();
        generator = new MapFactory();
		assertNotNull(generator);
	}

    @Test
    public void testCreateIsland(){
        TerrainData[][] terrain = generator.generateWorldDataAndImage();
        double smallest = 1;
        double largest = 0;
        for(int x = 0; x<terrain.length; x++){
            for (int y = 0; y < terrain[0].length; y++){
                assertTrue(terrain[x][y].getPosition().getY()<=1);
                assertTrue(terrain[x][y].getPosition().getY()>=0);
                if(smallest>terrain[x][y].getPosition().getY()){
                    smallest = terrain[x][y].getPosition().getY();
                }
                if(largest<terrain[x][y].getPosition().getY()){
                    largest = terrain[x][y].getPosition().getY();
                }
            }
        }
        assertTrue(largest>0.95);
        assertTrue(smallest<0.05);
    }
    @Test
    public void testCreateFinalMap(){
        TerrainData[][] terrain = generator.generateWorldDataAndImage();
        double smallest = 255;
        double largest = 0;
        for(int x = 0; x<terrain.length; x++){
            for (int y = 0; y < terrain[0].length; y++){
                assertTrue(terrain[x][y].getPosition().getZ()<=255);
                assertTrue(terrain[x][y].getPosition().getZ()>=0);
                if(terrain[x][y].getPosition().getZ() < smallest){
                    smallest = terrain[x][y].getPosition().getZ();
                }
                if(largest < terrain[x][y].getPosition().getZ()){
                    largest = terrain[x][y].getPosition().getZ();
                }
            }
        }
        assertTrue(largest>0.95);
        assertTrue(smallest<0.05);
    }
}*/
