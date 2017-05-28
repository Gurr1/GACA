package hills.services.generation;

import hills.services.ServiceLocator;
import hills.services.terrain.TerrainServiceConstants;
import hills.util.math.Vec3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @Author Gustav Engsmyre
 */
public class TerrainGenerator {

    NoiseMapGenerator noise;
    private static int  HEIGHT = TerrainServiceConstants.TERRAIN_HEIGHT;
    private static int WIDTH = TerrainServiceConstants.TERRAIN_WIDTH;
    private static String HEIGHT_MAP_NAME = TerrainServiceConstants.HEIGHT_MAP_NAME;
    private static String NORMAL_MAP_NAME = TerrainServiceConstants.HEIGHT_MAP_NORMAL_MAP_NAME;
    private int[][] matrix = new int[WIDTH + 1][HEIGHT + 1];
    private float WATER_HEIGHT = TerrainServiceConstants.WATER_HEIGHT;
    protected TerrainGenerator(long seed) {
        noise = new NoiseMapGenerator(seed);
    }
    
    private int[][] createHeightMap() {
        Random rand = new Random();
        Thread t = new Thread();
        double[][] noise1 = noise.createMatrix(500, 1, false);
        noise.setSeed(rand.nextLong());
        double[][] noise2 = noise.createMatrix(250, 0.8, false);
        noise.setSeed(rand.nextLong());
        double[][] noise3 = noise.createMatrix(150, 0.6, false);
        noise.setSeed(rand.nextLong());
        double[][] noise4 = noise.createMatrix(70, 0.5, false);
        noise.setSeed(rand.nextLong());
        double[][] noise5 = noise.createMatrix(30, 0.4, false);
        noise.setSeed(rand.nextLong());
        double maximum = 0;
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                            double green = noise1[x][y] + noise2[x][y]*0.5 + noise3[x][y]*0.4 + noise4[x][y]*0.25 + noise5[x][y]*0.20;
                            if (green>maximum){
                                maximum = green;
                            }
                            green = green / maximum;
                            green = Math.pow(green, 2.87);          // increasing exponent adds valleys
                            matrix[x][y] = (int)(green*255);
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
                matrix[x][y] += islandAlgorithm2(noise1[x][y], x, y, exp, image, matrix);
            }
        }
        return matrix;
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
        return green;
    }

    void createfinalIsland(){
        double[][] islandMatrix = createIsland();
        int[][] noiseMatrix = createHeightMap();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        double[][] finalMatrix = new double[WIDTH][HEIGHT];
        for(int x = 0; x<WIDTH; x++){
            for(int y = 0; y<HEIGHT; y++){
                finalMatrix[x][y] = islandMatrix[x][y] * noiseMatrix[x][y];
                finalMatrix[x][y] = setUnderWaterToZero(finalMatrix[x][y]);
                int r = ((int)(finalMatrix[x][y]))<<16 & 0xFF0000;
                int g = ((int)(finalMatrix[x][y]))<<8 & 0xFF00;
                int b = ((int)(finalMatrix[x][y])) & 0xFF;
                int rgb = r+b+g;
                image.setRGB(x,y,rgb);
            }
        }
  //      blurImage(image);
        ServiceLocator.INSTANCE.getPictureFileService().writeImage(image, HEIGHT_MAP_NAME);
        generateTerrain(finalMatrix);
    }

    private void generateTerrain(double[][] terrain){
        BufferedImage image = new BufferedImage(terrain.length, terrain[0].length, BufferedImage.TYPE_INT_RGB);
        for(int x = 1; x < terrain.length-1; x++){          // Since the last rows are always Black, no Normal-calculations are needed.
            for(int y = 1; y<terrain[0].length-1; y++){
                if(terrain[x-1][y-1]==0 && terrain[x-1][y+1] == 0     // Speeds up calculations by ignoring all black space.
                        && terrain[x+1][y-1] == 0 && terrain[x+1][y+1] == 0){
                    image.setRGB(x,y,0);
                    continue;
                }

                Vec3 result = generateNormal(terrain, x, y);
                image.setRGB(x, y, new Color(result.getX(), result.getY(), result.getZ()).getRGB());
            }
        }
        ServiceLocator.INSTANCE.getPictureFileService().writeImage(image, NORMAL_MAP_NAME);
    }
    private Vec3 generateNormal(double[][] terrain, int x, int y) {
        float upValue;
        Vec3 upVec = new Vec3(0,0,0);
        float leftValue;
        Vec3 leftVec = new Vec3(0,0,0);
        float rightValue;
        Vec3 rightVec = new Vec3(0,0,0);
        float downValue;
        Vec3 downVec = new Vec3(0,0,0);

        float value = (float) (terrain[x][y] / 255.0f * 100);

        if(y > 0) {
            upValue = (float) (terrain[x][y - 1] / 255.0f * 100);
            upVec = new Vec3(0.0f, upValue - value, -1.0f).normalize();
        }

        if(x > 0){
            leftValue = (float) (terrain[x - 1][y] / 255.0f * 100);
            leftVec = new Vec3(-1.0f, leftValue - value, 0.0f).normalize();
        }

        if(x < terrain.length - 1){
            rightValue = (float) (terrain[x + 1][y] / 255.0f * 100);
            rightVec = new Vec3(1.0f, rightValue - value, 0.0f).normalize();
        }

        if(y < terrain[0].length - 1){
            downValue = (float) (terrain[x][y + 1]/ 255.0f * 100);
            downVec = new Vec3(0.0f, downValue - value, 1.0f).normalize();
        }

        Vec3 result = new Vec3(0.0f, 0.0f, 0.0f);
        if(y > 0 && x > 0)
            result = result.add(upVec.cross(leftVec));

        if(x > 0 && y < terrain[0].length - 1)
            result = result.add(leftVec.cross(downVec));

        if(y < terrain[0].length - 1 && x < terrain.length - 1)
            result = result.add(downVec.cross(rightVec));

        if(x < terrain.length - 1 && y > 0)
            result = result.add(rightVec.cross(upVec));

         return result.normalize().add(1.0f).div(2.0f);
    }

    private double setMinusToZero(double green) {
        if(green<0.0){
            return 0;
        }
        return green;
    }
    private double setUnderWaterToZero(double green) {
        if(green<WATER_HEIGHT){
            return 0;
        }
        return green;
    }
}
