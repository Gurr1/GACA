package hills.Gurra;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Created by gustav on 2017-03-22.
 */
public class Terrain {
    NoiseMapGenerator noise;
    private double ELEVATION_MODIFIER = 0.8;
    private int HEIGHT = 512;
    private int WIDTH = 512;
    public Terrain(long seed){
        noise = new NoiseMapGenerator(seed);
    }
    public void createHeightMap(){
        long startTime = System.nanoTime();
        noise.create2DNoiseImage("backgroundNoise", 150, 1);
        noise.create2DNoiseImage("detailsNoise", 30, 0.7);
        noise.create2DNoiseImage("smallestNoise", 8, 0.8);
        noise.create2DNoiseImage("tree", 0.5, 0.4);
        BufferedImage image = new BufferedImage(WIDTH+1, HEIGHT+1, BufferedImage.TYPE_INT_RGB);
        BufferedImage details = null;
        BufferedImage background = null;
        BufferedImage smallestNoise = null;
        try {
            details = ImageIO.read(new File("src/main/resources/detailsNoise.png"));
            background = ImageIO.read(new File("src/main/resources/backgroundNoise.png"));
            smallestNoise = ImageIO.read(new File("src/main/resources/smallestNoise.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        double maximum = 0;
        for(int x = 0; x<=WIDTH; x++){
            for(int y = 0; y<=HEIGHT; y++){
                double green = background.getRGB(x, y) >> 8 & 0xFF;       // not correct. does create interesting effect however
                green = (int)(green + (details.getRGB(x, y) >> 8 & 0xFF)*0.5);
                green = (int)(green + (smallestNoise.getRGB(x, y) >> 8 & 0xFF)*0.25);
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
                double multiplier = (WIDTH*HEIGHT/4)-distanceToCenterX*distanceToCenterY;
                multiplier = multiplier/(WIDTH*HEIGHT/4);
                System.out.println("x " + distanceToCenterX + " y " + distanceToCenterY + " " + (multiplier));
                green = (green*Math.pow(0.5, 1-multiplier))*255;

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
}
