package hills;


import hills.Gurra.NoiseMapGenerator;
import lombok.Getter;
=======
import hills.Anton.Init;

/**
 * Created by gustav on 2017-03-21.
 */
public class Main {
    public static void main(String [] args){
        NoiseMapGenerator noise = new NoiseMapGenerator();
        noise.create2DNoise(15);
        Init i = new Init();
        i.init();
    }
}
