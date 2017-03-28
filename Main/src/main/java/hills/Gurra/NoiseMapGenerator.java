package hills.Gurra;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by gustav on 2017-03-21.
 */
public class NoiseMapGenerator {            // This file should be cleaned up for readability.
    private final int WIDTH = 1056;
    private final int HEIGHT = 1056;

    private static final double FEATURE_SIZE = 24;      // >Zooms in on the picture, good for creating an overall world.
    OpenSimplexNoise noise;
    int[][] greenMatrix = new int[WIDTH][HEIGHT];

    public NoiseMapGenerator(long seed) {
       noise = new OpenSimplexNoise(seed);
    }
    public void setSeed(long seed){
        noise.setSeed(seed);
    }

    public void create2DNoiseImage(String name, double frequency, double scale){        // this should probably be created in a thread, takes about .25 secs with current values
        BufferedImage image = new BufferedImage(WIDTH+1, HEIGHT+1, BufferedImage.TYPE_INT_RGB);
            int[][] greens = createMatrix(frequency, scale);
            for (int y = 0; y <= HEIGHT; y++) {
                for (int x = 0; x <= WIDTH; x++) {
                    int[] rgb = {0, greens[x][y], 0};
                    image.getRaster().setPixel(x, y, rgb);
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

    public int[][] createMatrix(double frequency, double scale){
        int[][] matrix = new int[WIDTH+1][HEIGHT+1];
        for(int y = 0; y <= HEIGHT; y++) {
            for (int x = 0; x <= WIDTH; x++) {
                double value = noise.eval(x / frequency, y / frequency)*scale;
                int green = (0x010101 * (int) ((value + 1) * 127.5) >> 8) & 0xFF;
                matrix[x][y] = green;
            }
        }
        this.greenMatrix = matrix;
        return matrix;
    }
}


