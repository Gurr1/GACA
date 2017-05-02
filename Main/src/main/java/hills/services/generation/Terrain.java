package hills.services.generation;

import hills.services.terrain.TerrainService;
import hills.util.math.Vec3;

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
    private static int  HEIGHT = TerrainService.TERRAIN_HEIGHT;
    private static int WIDTH = TerrainService.TERRAIN_WIDTH;
    private static String HEIGHT_MAP_PATH = "src/main/resources/textures/finalNoise.png";
    private static String NORMAL_MAP_PATH = "src/main/resources/textures/normal.png";
    private int[][] matrix = new int[WIDTH + 1][HEIGHT + 1];

    protected Terrain(long seed) {
        noise = new NoiseMapGenerator(seed);
    }
    
    private int[][] createHeightMap() {
        Random rand = new Random();
        double[][] noise1 = noise.createMatrix(500, 1, false);
        noise.setSeed(rand.nextLong());
        double[][] noise2 = noise.createMatrix(150, 0.8, false);
        noise.setSeed(rand.nextLong());
        double[][] noise3 = noise.createMatrix(70, 0.5, false);
        noise.setSeed(rand.nextLong());
        double[][] noise4 = noise.createMatrix(30, 0.4, false);
        noise.setSeed(rand.nextLong());
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        double maximum = 0;
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
    private double[][] createIsland(){           // Can this be done Threaded?
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

    protected TerrainData[][] createfinalIsland(){
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
            ImageIO.write(image, "png", new File(HEIGHT_MAP_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return generateTerrain(finalMatrix);
    }
    private TerrainData[][] generateTerrain(double[][] terrain){
        TerrainData[][] datas = new TerrainData[terrain.length][terrain[0].length];
        BufferedImage image = new BufferedImage(terrain.length, terrain[0].length, BufferedImage.TYPE_INT_RGB);
        for(int x = 1; x < terrain.length-1; x++){          // Since the last rows are always Black, no Normal-calculations are needed.
            for(int y = 1; y<terrain[0].length-1; y++){
                Vec3 pos = new Vec3(x, (float) terrain[x][y], y);
                Vec3 v1 = new Vec3(x-1, (float)terrain[x-1][y], y+1);
                Vec3 v2 = new Vec3(x+1, (float)terrain[x+1][y],y);
                Vec3 v3 = new Vec3(x, (float)terrain[x][y-1],y-1);
                Vec3 v4 = new Vec3(x, (float)terrain[x][y+1], y+1);
                TerrainData td = new TerrainData(pos,v1,v2,v3,v4);
                Vec3 normal = td.getNormal();
                datas[x][y] = td;
                int[]rgb = {(int) (normal.getX()*255), (int) (normal.getZ()*255), (int) (normal.getY()*255)};
                image.getRaster().setPixel(x,y,rgb);
            }
        }
        try {
            ImageIO.write(image, "png", new File(NORMAL_MAP_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datas;
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
