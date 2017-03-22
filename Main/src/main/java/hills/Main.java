package hills;


import hills.Anders.ObjectPlacer;
import hills.Anton.Init;

/**
 * Created by gustav on 2017-03-21.
 */
public class Main {

    public static void main(String [] args){
       // NoiseMapGenerator noise = new NoiseMapGenerator();
        //Random rand = new Random();
       //noise.create2DNoise(rand.nextLong());
        ObjectPlacer placement = new ObjectPlacer("Main/src/main/resources/height_map_test_2.png", "ObjectMap");
        placement.placeObjects();
        Init i = new Init();
        i.init();
    }
}
