package hills.Gurra;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.util.Random;
import java.util.WeakHashMap;

/**
 * Created by gustav on 2017-03-22.
 */
public class Terrain {
    NoiseMapGenerator noise;
    private double ELEVATION_MODIFIER = 0.8;
    private int HEIGHT = 1056;
    private int WIDTH = 1056;
    public Terrain(long seed){
        noise = new NoiseMapGenerator(seed);
    }
    public void createHeightMap(){
        long startTime = System.nanoTime();
        noise.create2DNoiseImage("backgroundNoise", 150, 1);
        noise.create2DNoiseImage("detailsNoise", 30, 0.7);
        noise.create2DNoiseImage("smallestNoise", 8, 0.8);
        noise.create2DNoiseImage("Noise4", 0.5, 0.4);
        BufferedImage image = new BufferedImage(WIDTH+1, HEIGHT+1, BufferedImage.TYPE_INT_RGB);
        BufferedImage details = null;
        BufferedImage background = null;
        BufferedImage smallestNoise = null;
        BufferedImage noise4 = null;
        try {
            details = ImageIO.read(new File("src/main/resources/detailsNoise.png"));
            background = ImageIO.read(new File("src/main/resources/backgroundNoise.png"));
            smallestNoise = ImageIO.read(new File("src/main/resources/smallestNoise.png"));
            noise4 = ImageIO.read(new File("src/main/resources/Noise4.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        double exp = exponentCalc();
        double maximum = 0;
        for(int x = 0; x<=WIDTH; x++){
            for(int y = 0; y<=HEIGHT; y++){
                double green = background.getRGB(x, y) >> 8 & 0xFF;
                green = (green + (details.getRGB(x, y) >> 8 & 0xFF)*0.5);
                green = (green + (smallestNoise.getRGB(x, y) >> 8 & 0xFF)*0.25);
                green = (green + (noise4.getRGB(x, y) >> 8 & 0xFF)*0.2);
                green = Math.pow(3*green, 1/ELEVATION_MODIFIER) - Math.pow(2*green, 1/ELEVATION_MODIFIER);
                if (green>maximum){
                    maximum = green;
                }
                green = green / maximum;
                int distanceToCenterX;
                if(WIDTH/2-x>0) {
                    distanceToCenterX = WIDTH/2 - x;
                }
                else{
                    distanceToCenterX = x-WIDTH/2;
                }
                int distanceToCenterY;
                if(HEIGHT/2-y>0) {
                    distanceToCenterY = HEIGHT/2 - y;
                }
                else{
                    distanceToCenterY = y-HEIGHT/2;
                }
                double multiplier = (WIDTH*HEIGHT/4)-((distanceToCenterX*distanceToCenterY + Math.pow(distanceToCenterX,exp) + Math.pow(distanceToCenterY,exp))/4);
                multiplier = (multiplier/(WIDTH*HEIGHT/4));
                multiplier = Math.pow(multiplier, 1.5);
                //Multiplier is used to create a island like shape.
                green *= multiplier*255;
                int [] rgb = {0,(int)green,0};
                image.getRaster().setPixel(x, y, rgb);
            }
        }
        System.out.println(System.nanoTime()-startTime);
        try {
            ImageIO.write(image, "png", new File("src/main/resources/testnoise.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private double exponentCalc(){
        double exp = 3.6;
        exp = exp - (Math.pow(Math.sqrt(HEIGHT * WIDTH)*Math.sqrt(2056.0/WIDTH), 0.0448646631));
        System.out.println(Math.sqrt(2056.0/WIDTH));
        System.out.println(exp);
        return exp;
    }
}
