package hills.Gurra;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by gustav on 2017-03-22.
 */
public class Terrain {
    NoiseMapGenerator noise;
    private double ELEVATION_MODIFIER = 0.8;
    private int HEIGHT = 2048;
    private int WIDTH = 2048;
    int[][] matrix = new int[WIDTH + 1][HEIGHT + 1];

    public Terrain(long seed) {
        noise = new NoiseMapGenerator(seed);
    }

    public int[][] createHeightMap() {
        Random rand = new Random();
        long startTime = System.nanoTime();
        double[][] noise1 = noise.createMatrix(150, 1);
        noise.setSeed(rand.nextLong());
        double[][] noise2 = noise.createMatrix(50, 0.8);
        noise.setSeed(rand.nextLong());
        double[][] noise3 = noise.createMatrix(25, 0.5);
        noise.setSeed(rand.nextLong());
        double[][] noise4 = noise.createMatrix(3, 0.4);
        noise.setSeed(rand.nextLong());
        double[][] noise5 = noise.createMatrix(2, 0.4);
        noise.setSeed(rand.nextLong());
        BufferedImage image = new BufferedImage(WIDTH + 1, HEIGHT + 1, BufferedImage.TYPE_INT_RGB);
        double exp = exponentCalc();
        double maximum = 0;
        double max = 0;
        for (int x = 0; x <= WIDTH; x++) {
            for (int y = 0; y <= HEIGHT; y++) {
                double green = noise1[x][y];
                if (green > maximum) {
                    maximum = green;
                }
                green = green / maximum;
                matrix[x][y] = (int) green;
                matrix[x][y] += Math.pow(noise2[x][y], 0.8);
                matrix[x][y] += Math.pow(noise3[x][y], 0.8);
                matrix[x][y] += Math.pow(noise4[x][y], 0.8);
                matrix[x][y] += Math.pow(noise5[x][y], 0.8);
                if (max < matrix[x][y]) {
                    max = matrix[x][y];
                }
            }

        }
        int[][] matrix2 = new int[WIDTH + 1][HEIGHT + 1];
        for (int x = 0; x <= WIDTH; x++) {
            for (int y = 0; y <= HEIGHT; y++) {
                matrix2[x][y] = (int) ((matrix[x][y] / max) * 255.0);
                matrix[x][y] = (int) Math.pow(matrix2[x][y] * matrix[x][y], 0.4);
                int[] rgb = {0, matrix[x][y], 0};
                image.getRaster().setPixel(x, y, rgb);
            }
        }
        System.out.println(System.nanoTime() - startTime);
        try {
            ImageIO.write(image, "png", new File("src/main/resources/textures/testnoise.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    private double exponentCalc() {
        double exp = 3.62;
        exp = exp - (Math.pow(Math.sqrt(HEIGHT * WIDTH) * Math.sqrt(2056.0 / WIDTH), 0.0448646631));
        return exp;
    }

    public int[][] createHeightMa() {
        Random rand = new Random();
        double frequency = 200;
        double amplitude = 1;
        BufferedImage image = new BufferedImage(WIDTH + 1, HEIGHT + 1, BufferedImage.TYPE_INT_RGB);
        int[][] colorMatrix = new int[WIDTH][HEIGHT];
        double green = 0;
        double maxAmp = 0;
        double largest = 0;
        for (int i = 0; i < 4; i++) {
            double[][] matrix = noise.createMatrix(frequency, amplitude);
            frequency = frequency * 0.5;
            amplitude = amplitude * 0.75;
            maxAmp += amplitude;
            noise.setSeed(rand.nextLong());
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    green = matrix[x][y];
                    colorMatrix[x][y] += (int) green;
                    if (largest < colorMatrix[x][y]) {
                        largest = colorMatrix[x][y];
                    }
                }
            }
        }
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                green = (colorMatrix[x][y] / largest) * 255.0;
                int[] rgb = {0, (int) green, 0};
                System.out.println(green);
                image.getRaster().setPixel(x, y, rgb);
            }
        }
        try {
            ImageIO.write(image, "png", new File("src/main/resources/textures/testnoise.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    public double[][] test() {
        double[][] matrix = noise.createMatrix(300, 1);
        double[][] matrix2 = noise.createMatrix(150, 0.5);
        double[][] matrix3 = noise.createMatrix(75, 0.25);
        BufferedImage image = new BufferedImage(WIDTH + 1, HEIGHT + 1, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                    double green = matrix[x][y];       // not correct. does create interesting effect however
                    green = (int) ((green + matrix2[x][y]) * 0.5);
                    green = (int) ((green + (matrix3[x][y])) * 0.25);
                    green = Math.pow(3 * green, 1 / ELEVATION_MODIFIER) - Math.pow(2 * green, 1 / ELEVATION_MODIFIER);
            }
            }
            try {
                ImageIO.write(image, "png", new File("src/main/resources/textures/testnoise.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        return matrix;
    }

    /*
     * Do Not Modify. works pretty perfectly like it should.
     */
    public double[][] createIsland(){
        double matrix[][] = new double[WIDTH+1][HEIGHT+1];
        BufferedImage image = new BufferedImage(WIDTH + 1, HEIGHT + 1, BufferedImage.TYPE_INT_RGB);
        double exp = exponentCalc();
        double[][] noise1 = noise.createMatrix(400, 1);
        for (int x = 0; x <= WIDTH; x++) {
            for (int y = 0; y <= HEIGHT; y++) {

                double green = 1;
                int distanceToCenterX;
                if (WIDTH / 2 - x > 0) {
                    distanceToCenterX = WIDTH / 2 - x;
                } else {
                    distanceToCenterX = x - WIDTH / 2;
                }
                int distanceToCenterY;
                if (HEIGHT / 2 - y > 0) {
                    distanceToCenterY = HEIGHT / 2 - y;
                } else {
                    distanceToCenterY = y - HEIGHT / 2;
                }
                double multiplier = (WIDTH * HEIGHT / 4) - ((distanceToCenterX * distanceToCenterY + Math.pow(distanceToCenterX, exp) + Math.pow(distanceToCenterY, exp)) / 4);
                multiplier = (multiplier / (WIDTH * HEIGHT / 4));
                //Multiplier is used to create a island like shape.
                green *= multiplier;
                green = setMinusToZero(green);
                boolean isZero = green==0;
                if(!isZero) {
                    green = Math.pow(green,1-setMinusToZero(noise1[x][y]));
                }
                matrix[x][y] = green;
                int[] rgb = {0,(int)(green*255.0),0};
                image.getRaster().setPixel(x, y, rgb);
            }
        }
        try {
            ImageIO.write(image, "png", new File("src/main/resources/islandNoise.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    private double setMinusToZero(double green) {
        if(green<0){
            return 0;
        }
        return green;
    }
}
