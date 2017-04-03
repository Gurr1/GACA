package hills.Gurra;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

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
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        double maximum = 0;
        double max = 0;
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                            double green = noise1[x][y] + noise2[x][y]*0.5 + noise3[x][y]*0.25 + noise4[x][y]*0.2;
                            if (green>maximum){
                                maximum = green;
                            }
                            green = green / maximum;
                            green = Math.pow(green, 2.87);          // increasing exponent adds valleys
                            matrix[x][y] = (int)(green*255);
                        }
                    }
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
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

    /*
     * Do Not Modify. works pretty perfectly like it should.
     */
    public double[][] createIsland(){           // Can this be done Threaded?
        double matrix[][] = new double[WIDTH][HEIGHT];
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        double exp = exponentCalc();
        double[][] noise1 = noise.createMatrix(100, 1, true);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                double rgb = islandAlgorithm1(noise1[x][y], x, y, exp, image, matrix);
                rgb += islandAlgorithm2(noise1[x][y], x, y, exp, image, matrix);
                rgb/=2;
                image.setRGB(x,y,(int)(rgb/2));
            }
        }
        try {
            ImageIO.write(image, "png", new File("src/main/resources/textures/islandNoise.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }
    private double islandAlgorithm1(double noise, int x, int y, double exp, BufferedImage image, double[][] matrix){
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
            green = Math.pow(green,setMinusToZero(noise)*3);         // last number decides how steep natural slopes should be.
        }// line above is only thing that needs change
        matrix[x][y] = green;
        int r = ((int)(green*255))<<16 & 0xFF0000;
        int g = ((int)(green*255))<<8 & 0xFF00;
        int b = ((int)(green*255)) & 0xFF;
        int rgb = r+b+g;
        image.setRGB(x,y,rgb);
        return rgb;
    }
    private double islandAlgorithm2(double noise, int x, int y, double exp, BufferedImage image, double[][] matrix){

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
            green = Math.pow(green,1-setMinusToZero(noise)*1.1);         // last number decides how steep natural slopes should be.
        }
        matrix[x][y] = green;
        int r = ((int)(green*255))<<16 & 0xFF0000;
        int g = ((int)(green*255))<<8 & 0xFF00;
        int b = ((int)(green*255)) & 0xFF;
        int rgb = r+b+g;
        image.setRGB(x,y,rgb);
        return rgb;
    }

    public double[][] createfinalIsland(){
        double[][] islandMatrix = createIsland();
        int[][] noiseMatrix = createHeightMap();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        double[][] finalMatrix = new double[WIDTH][HEIGHT];
        for(int x = 0; x<WIDTH; x++){
            for(int y = 0; y<HEIGHT; y++){
                finalMatrix[x][y] = islandMatrix[x][y] * noiseMatrix[x][y];
                finalMatrix[x][y] = setToZero(finalMatrix[x][y]);
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
        generateNormal(finalMatrix);
        return finalMatrix;
    }
    public void generateNormal(double[][] terrain){
        List<Vec3> normals = new ArrayList<>();
        BufferedImage image = new BufferedImage(terrain.length, terrain[0].length, BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < terrain.length-1; x++){          // Since the last rows are always Black, no Normal-calculations are needed.
            for(int y = 0; y<terrain[0].length-1; y++){
                Vec3 v1 = new Vec3(x, y, (float)terrain[x][y]);
                Vec3 v2 = new Vec3(x+1, y, (float)terrain[x+1][y]);
                Vec3 v3 = new Vec3(x, y+1, (float)terrain[x][y+1]);
                TerrainData td = new TerrainData(v1,v2,v3);
                Vec3 normal = td.getNormal();
                normals.add(normal);
                int[]rgb = {(int) (normal.getX()*255), (int) (normal.getY()*255), (int) (normal.getZ()*255)};
                image.getRaster().setPixel(x,y,rgb);
            }
        }
        try {
            ImageIO.write(image, "png", new File("src/main/resources/textures/normal.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private double setMinusToZero(double green) {
        if(green<0.0){
            return 0;
        }
        return green;
    }
    private double setToZero(double green) {
        if(green<30){
            return 0;
        }
        return green;
    }
}
