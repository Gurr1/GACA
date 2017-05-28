package hills.services.generation;

import hills.services.terrain.TerrainServiceConstants;

import java.awt.image.BufferedImage;

/**
 * Created by gustav on 2017-03-21.
 */

public class NoiseMapGenerator{
    private final int WIDTH = TerrainServiceConstants.TERRAIN_WIDTH;
    private final int HEIGHT = TerrainServiceConstants.TERRAIN_HEIGHT;

    OpenSimplexNoise noise;
    double[][] greenMatrix = new double[WIDTH][HEIGHT];

    NoiseMapGenerator(long seed) {
		noise = new OpenSimplexNoise(seed);
	}

	void setSeed(long seed){
    	noise.setSeed(seed); 
    }

    BufferedImage createNoiseMap(double frequency, double scale){
	    double map[][] = createMatrix(frequency, scale, true);
        BufferedImage bf = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	    for(int x = 0; x < WIDTH; x++){
	        for(int y = 0; y < HEIGHT; y++){
                bf.setRGB(x, y,(int) (map[x][y]*255));
            }
        }
        return bf;
    }

    double[][] createMatrix(double frequency, double scale, boolean zeroToOne){
        double[][] matrix = new double[WIDTH+1][HEIGHT+1];
        for(int y = 0; y <= HEIGHT; y++) {
            for (int x = 0; x <= WIDTH; x++) {
                double value = noise.eval(x / frequency, y / frequency)*scale;
                if(!zeroToOne) {
                    int rgb = 0x010101 * (int) ((value + 1) * 127.5);
                    matrix[x][y] = rgb;
                }
                else{
                    matrix[x][y] = value;
                }
            }
        }
        this.greenMatrix = matrix;
        return matrix;
    }

}
