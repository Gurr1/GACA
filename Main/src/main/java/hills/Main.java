package hills;


import hills.Gurra.Terrain;
import hills.Anton.Init;

import java.util.Random;

/**
 * Created by gustav on 2017-03-21.
 */
public class Main {

    public static void main(String [] args){
        Random rand = new Random();
        Terrain noise = new Terrain(rand.nextLong());
        noise.createHeightMap();
        Init i = new Init();
        i.init();
    }
}
