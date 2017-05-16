package hills.services.generation;

import hills.services.terrain.TerrainServiceConstants;
import hills.util.math.Vec3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by gustav on 2017-03-22.
 */
public class Terrain {

    NoiseMapGenerator noise;
    private static int  HEIGHT = TerrainServiceConstants.TERRAIN_HEIGHT;
    private static int WIDTH = TerrainServiceConstants.TERRAIN_WIDTH;
    private static String HEIGHT_MAP_PATH = TerrainServiceConstants.HEIGHT_MAP_DIRECTORY
            + TerrainServiceConstants.HEIGHT_MAP_NAME;
    private static String NORMAL_MAP_PATH = TerrainServiceConstants.HEIGHT_MAP_DIRECTORY
            + TerrainServiceConstants.HEIGHT_MAP_NORMAL_MAP_NAME;
    private int[][] matrix = new int[WIDTH + 1][HEIGHT + 1];
    private float WATER_HEIGHT = TerrainServiceConstants.WATER_HEIGHT;
    protected Terrain(long seed) {
        noise = new NoiseMapGenerator(seed);
    }
    
    private int[][] createHeightMap() {
        Random rand = new Random();
        Thread t = new Thread();
        double[][] noise1 = noise.createMatrix(500, 1, false);
        noise.setSeed(rand.nextLong());
        double[][] noise2 = noise.createMatrix(150, 0.8, false);
        noise.setSeed(rand.nextLong());
        double[][] noise3 = noise.createMatrix(70, 0.5, false);
        noise.setSeed(rand.nextLong());
        double[][] noise4 = noise.createMatrix(30, 0.4, false);
        noise.setSeed(rand.nextLong());
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
        try {
            ImageIO.write(image, "png", new File(HEIGHT_MAP_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        generateTerrain(finalMatrix);
    }

    private void blurImage(BufferedImage bf) {
        double[][] blur = {
                {0.003765, 0.015019, 0.023792, 0.015019, 0.003765},
                {0.015019, 0.059912, 0.094907, 0.059912, 0.015019},
                {0.023792, 0.094907, 0.150342, 0.094907, 0.023792},
                {0.015019, 0.059912, 0.094907, 0.059912, 0.015019},
                {0.003765, 0.015019, 0.023792, 0.015019, 0.003765}};
        for(int x = 20; x < WIDTH-20; x++) {
            for(int y = 20; y<HEIGHT-20; y++) {
                long rgb = 0;
                for (int i = -2; i < blur.length-2; i++) {
                    for (int j = -2; j < blur[0].length-2; j++) {
                        rgb += bf.getRGB(i+x, j+y)*blur[i+2][j+2];
                        if(x == 1028 && y == 1028){
                            System.out.println(rgb);
                        }
                    }
                }
       //         image.setRGB(x,y, rgb);
            }
        }
    }

    private void generateTerrain(double[][] terrain){
        BufferedImage image = new BufferedImage(terrain.length, terrain[0].length, BufferedImage.TYPE_INT_RGB);
        float upValue;
        Vec3 upVec = new Vec3(0,0,0);
        float leftValue;
        Vec3 leftVec = new Vec3(0,0,0);
        float rightValue;
        Vec3 rightVec = new Vec3(0,0,0);
        float downValue;
        Vec3 downVec = new Vec3(0,0,0);
        for(int x = 1; x < terrain.length-1; x++){          // Since the last rows are always Black, no Normal-calculations are needed.
            for(int y = 1; y<terrain[0].length-1; y++){
                if(terrain[x-1][y-1]==0 && terrain[x-1][y+1] == 0     // Speeds up calculations by ignoring all black space.
                        && terrain[x+1][y-1] == 0 && terrain[x+1][y+1] == 0){
                    image.setRGB(x,y,0);
                    continue;
                }
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

                result = result.normalize().add(1.0f).div(2.0f);

                image.setRGB(x, y, new Color(result.getX(), result.getY(), result.getZ()).getRGB());
            }
        }





                //Vec3 normX = new Vec3((terrainVertices[x - 1 + ZTimesSize].Position.Y - terrainVertices[x + 1 + ZTimesSize].Position.Y) / 2, 1, 0);
               // Vec3 normZ = new Vec3(0, 1, (terrainVertices[x + (z - 1) * this.size].Position.Y - terrainVertices[x + (z + 1) * this.size].Position.Y) / 2);
          /*      Vec3 v1 = new Vec3(x-1, (float)terrain[x-1][y-1], y-1);
                Vec3 v2 = new Vec3(x-1, (float)terrain[x-1][y+1],y+1);
                Vec3 v3 = new Vec3(x+1, (float)terrain[x+1][y-1],y-1);
                Vec3 v4 = new Vec3(x+1, (float)terrain[x+1][y+1], y+1);
                int[]rgb = generateNormal(v1,v2,v3,v4);
                image.getRaster().setPixel(x,y,rgb);
            }
        }*/
        try {
            ImageIO.write(image, "png", new File(NORMAL_MAP_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int[] generateNormal(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4) {

        Vec3 rel1 = v4.sub(v1);
        Vec3 rel2 = v2.sub(v3);
        System.out.println("rel1 " + rel1);
        System.out.println("rel2 " + rel2);
        Vec3 normal = rel2.cross(rel1).normalize();
        return new int[] {(int)(normal.getX() * 255),
                (int)(normal.getY() * 255), (int)(normal.getZ() * 255)};
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
