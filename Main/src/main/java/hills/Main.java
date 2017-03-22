package hills;


import hills.Gurra.NoiseMapGenerator;
import lombok.Getter;
import hills.Anton.Init;

import java.util.Random;

/**
 * Created by gustav on 2017-03-21.
 */
public class Main {

    public static void main(String [] args){
        Random rand = new Random();
        NoiseMapGenerator noise = new NoiseMapGenerator(rand.nextLong());
        noise.create2DNoiseImage("noise");
        Init i = new Init();
        i.init();
    }
}
