package hills;

import hills.Gurra.NoiseMapGenerator;
import lombok.Getter;

/**
 * Created by gustav on 2017-03-21.
 */
public class Main {
    public static void main(String [] args){
        NoiseMapGenerator noise = new NoiseMapGenerator();
        noise.create2DNoise(15);
    }
}
