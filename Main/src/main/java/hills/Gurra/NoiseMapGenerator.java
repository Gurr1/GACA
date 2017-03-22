package hills.Gurra;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Created by gustav on 2017-03-21.
 */
public class NoiseMapGenerator {
    private final int WIDTH = 512;
    private final int HEIGHT = 512;
    private static final double FEATURE_SIZE = 24;
    public NoiseMapGenerator() {
    }
    public void create2DNoise(long seed){        // this should probably be created in a thread, takes about 20 secs with current values
        OpenSimplexNoise noise = new OpenSimplexNoise(0);
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < HEIGHT; i++){
            for (int j = 0; j < WIDTH; j++){
                double value = noise.eval(i / FEATURE_SIZE, j / FEATURE_SIZE);
                int rgb = 0x010101 * (int)((value + 1) * 127.5);
                image.setRGB(i, j, rgb);
            }
            try {
                ImageIO.write(image, "png", new File("src/main/resources/noise.png"));
            }
            catch (IOException io){
                System.out.println("failed to create file");
                io.printStackTrace();
            }
        }
    }
}


