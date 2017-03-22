package hills;


import hills.Anders.ObjectPlacement;
import hills.Gurra.NoiseMapGenerator;
import lombok.Getter;
import hills.Anton.Init;

import java.util.Random;

/**
 * Created by gustav on 2017-03-21.
 */
public class Main {

    public static void main(String [] args){
  //      NoiseMapGenerator noise = new NoiseMapGenerator();
        Random rand = new Random();
//        noise.create2DNoise(rand.nextLong());
        ObjectPlacement placement = new ObjectPlacement("Main/src/main/resources/noise.png");
        Init i = new Init();
        i.init();
    }
}
