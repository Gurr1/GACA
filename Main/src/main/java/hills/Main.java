package hills;



import hills.Anton.Init;
import hills.Anton.engine.math.Vec3;

import hills.Gurra.NoiseMapGenerator;
import hills.Gurra.Terrain;
import hills.Gurra.TerrainData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by gustav on 2017-03-21.
 */
public class Main {

    public static void main(String [] args){
       // NoiseMapGenerator noise = new NoiseMapGenerator();
        //Random rand = new Random();
       //noise.create2DNoise(rand.nextLong());
        //Long s = System.nanoTime();
       // System.out.println();
        //ObjectPlacer placer = new ObjectPlacer("Main/src/main/resources/GreenShades.png", "ObjectMap");
       // placer.placeObjects();
        //System.out.println((System.nanoTime()-s)/1000000);
     /*   Long s = System.nanoTime();
        System.out.println();
        ObjectPlacer placer = new ObjectPlacer("Main/src/main/resources/GreenShades.png", "ObjectMap");
        placer.placeObjects();
        System.out.println((System.nanoTime()-s)/1000000);*/


    	
        Random rand = new Random();
        Terrain noise = new Terrain(rand.nextLong());
        double[][] terrain = noise.finalIsland();
        noise.createfinalIsland();
        NoiseMapGenerator n = new NoiseMapGenerator(rand.nextLong());
        n.create2DNoiseImage("test", 100, 1);        Init i = new Init();
        i.init();



        //WaterGeneration waterGeneration = new WaterGeneration();
        //waterGeneration.genRiver();

    }
}
