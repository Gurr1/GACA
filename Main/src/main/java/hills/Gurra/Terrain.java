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
    private int HEIGHT = 2048;
    private int WIDTH = 2048;
    int[][] matrix = new int[WIDTH + 1][HEIGHT + 1];

    public Terrain(long seed) {
        noise = new NoiseMapGenerator(seed);
    }

    public int[][] createHeightMap() {
        Random rand = new Random();
        long startTime = System.nanoTime();
        double[][] noise1 = noise.createMatrix(500, 1, false);
        noise.setSeed(rand.nextLong());
        double[][] noise2 = noise.createMatrix(150, 0.8, false);
        noise.setSeed(rand.nextLong());
        double[][] noise3 = noise.createMatrix(70, 0.5, false);
        noise.setSeed(rand.nextLong());
        double[][] noise4 = noise.createMatrix(30, 0.4, false);
        noise.setSeed(rand.nextLong());
        double[][] noise5 = noise.createMatrix(5, 0.4, false);
        noise.setSeed(rand.nextLong());
        BufferedImage image = new BufferedImage(WIDTH + 1, HEIGHT + 1, BufferedImage.TYPE_INT_RGB);
        double maximum = 0;
        double max = 0;
        for (int x = 0; x <= WIDTH; x++) {
            for (int y = 0; y <= HEIGHT; y++) {
                            double green = noise1[x][y] + noise2[x][y]*0.5 + noise3[x][y]*0.25 + noise4[x][y]*0.2;
                            if (green>maximum){
                                maximum = green;
                            }
                            green = green / maximum;
                            green = Math.pow(green, 2.87);          // increasing exponent adds valleys
                            matrix[x][y] = (int)(green*255);
                        }
                    }
        for (int x = 0; x <= WIDTH; x++) {
            for (int y = 0; y <= HEIGHT; y++) {
                int r = ((matrix[x][y]))<<16 & 0xFF0000;
                int g = ((matrix[x][y]))<<8 & 0xFF00;
                int b = ((matrix[x][y])) & 0xFF;
                int rgb = r+g+b;
                image.setRGB(x,y,rgb);
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
            double[][] matrix = noise.createMatrix(frequency, amplitude,false);
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
        return matrix;
    }

    public double[][] test() {
        double[][] matrix = noise.createMatrix(300, 1, false);
        double[][] matrix2 = noise.createMatrix(150, 0.5, false);
        double[][] matrix3 = noise.createMatrix(75, 0.25, false);
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
        double[][] noise1 = noise.createMatrix(100, 1, true);
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
                    green = Math.pow(green,setMinusToZero(noise1[x][y])*3);         // last number decides how steep natural slopes should be.
                }
                matrix[x][y] = green;
                int r = ((int)(green*255))<<16 & 0xFF0000;
                int g = ((int)(green*255))<<8 & 0xFF00;
                int b = ((int)(green*255)) & 0xFF;
                int rgb2 = r+b+g;
                image.setRGB(x,y,rgb2);
            }
        }
        try {
            ImageIO.write(image, "png", new File("src/main/resources/textures/islandNoise.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }


    public double[][] finalIsland(){
        double[][] islandMatrix = createIsland();
        int[][] noiseMatrix = createHeightMap();
        BufferedImage image = new BufferedImage(WIDTH + 1, HEIGHT + 1, BufferedImage.TYPE_INT_RGB);
        double[][] finalMatrix = new double[WIDTH+1][HEIGHT+1];
        for(int x = 0; x<=WIDTH; x++){
            for(int y = 0; y<=HEIGHT; y++){
                finalMatrix[x][y] = islandMatrix[x][y] * noiseMatrix[x][y];
                finalMatrix[x][y] = setToZero(finalMatrix[x][y]);       // TO REMOVE. only to simulate water
                int r = ((int)(finalMatrix[x][y]))<<16 & 0xFF0000;
                int g = ((int)(finalMatrix[x][y]))<<8 & 0xFF00;
                int b = ((int)(finalMatrix[x][y])) & 0xFF;
                int rgb = r+b+g;
                image.setRGB(x,y,rgb);
            }
        }
        try {
            ImageIO.write(image, "png", new File("src/main/resources/textures/finalNoise.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalMatrix;
    }
    private double setMinusToZero(double green) {
        if(green<0.0){
            return 0;
        }
        return green;
    }
    private double setToZero(double green) {
        if(green<15.0){
            return 0;
        }
        return green;
    }

}
