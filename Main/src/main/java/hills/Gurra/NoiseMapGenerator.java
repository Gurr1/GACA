package hills.Gurra;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

/**
 * Created by gustav on 2017-03-21.
 */
public class NoiseMapGenerator {            // This file should be cleaned up for readability.
    private final int WIDTH = 512;
    private final int HEIGHT = 512;
    private static final double FEATURE_SIZE = 24;
    OpenSimplexNoise noise;
    int[][] greenMatrix = new int[WIDTH][HEIGHT];
    public NoiseMapGenerator(long seed) {
       noise = new OpenSimplexNoise(seed);
    }
    public void create2DNoiseImage(String name){        // this should probably be created in a thread, takes about 20 secs with current values
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage[] images = new BufferedImage[4];
        for(int i = 0; i<4; i++) {
            int[][] greens = createMatrix();
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    int[] rgb = {0, greens[x][y], 0};
                    image.getRaster().setPixel(x, y, rgb);
                }
            }
            images[i] = image;
        }
        int pixelValue[][] = new int[WIDTH][HEIGHT];
        for(int i = 0; i<4; i++) {
            for (int y = 0; y<HEIGHT; y++) {
                for (int x = 0; x<WIDTH; x++) {
                    pixelValue[x][y] += images[i].getRGB(x, y);
                    if (i == 3) {
                        image.setRGB(x, y, pixelValue[x][y]/4);
                    }
                }
            }
        }

        try {
            ImageIO.write(image, "png", new File("src/main/resources/" + name + ".png"));
        }
        catch (IOException io){
            System.out.println("failed to create file");
            io.printStackTrace();
        }
    }
    public double getDoubleValue(int x, int y){
        return greenMatrix[x][y] / 255.0;
    }
    private int[] arrayFromMatrix(int[][] matrix){
        int width = matrix.length;
        int height = matrix[0].length;
        int length = width*height;
        int[] arr = new int[length];
        int index = 0;
        for(int i = 0; i<width; i++) {
            for (int j = 0; j<height; j++) {
                arr[index] = matrix[i][j];
                index++;
            }
        }
        return arr;
    }

    private int[][] createMatrix(){
        int[][] matrix = new int[WIDTH][HEIGHT];
        for(int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                double value = noise.eval(y / FEATURE_SIZE, x / FEATURE_SIZE);
                int green = (0x010101 * (int) ((value + 1) * 127.5) >> 8) & 0xFF;
                matrix[x][y] = green;
            }
        }
        this.greenMatrix = matrix;
        return matrix;
    }
}

