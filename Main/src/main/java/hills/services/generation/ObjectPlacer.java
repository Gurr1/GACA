package hills.services.generation;
import hills.services.ServiceLocator;
import hills.services.terrain.ITerrainHeightService;
import hills.services.terrain.TerrainServiceConstants;
import hills.util.math.NormalDistribution;
import hills.util.math.Vec3;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 2017-03-21.
 */
public class ObjectPlacer {

    private int WIDTH;
    private int HEIGHT;
    private int COLOR = 255;
    private double DENSITY = 0.2;
    @Getter private double RADIUS = 1;
    private double OPTIMAL_HEIGHT = 0.2;
    @Getter private boolean OBJECT_MAP[][];
    private BufferedImage NOISE_MAP;
    private List<Vec3> vecList = new ArrayList<>();
    private ITerrainHeightService terrainHeight;


    public ObjectPlacer() {
        terrainHeight = ServiceLocator.INSTANCE.getTerrainHeightService(false);
        HEIGHT = TerrainServiceConstants.TERRAIN_HEIGHT;
        WIDTH = TerrainServiceConstants.TERRAIN_WIDTH;
    }

    /**
     *
     * @param color value of the color the objects will be marked as, between 0 and 255 (standard 255)
     * @param density value of  objects will be placed between 0 and 1(standard 0.1)
     * @param radius value of the minimum distance objects will have from eachother above 0 (standard 1)
     * @param optimalHeight value of the most likely height objects will spawn at (standard 0.3)
     */
    public ObjectPlacer(int color, double density, double radius, double optimalHeight) {
        setDensity(density);
        setColor(color);
        setRadius(radius);
        setOptimalHeight(optimalHeight);
        terrainHeight = ServiceLocator.INSTANCE.getTerrainHeightService(false);
        HEIGHT = TerrainServiceConstants.TERRAIN_HEIGHT;
        WIDTH = TerrainServiceConstants.TERRAIN_WIDTH;
    }

    /**
     * Places objects based on the green color value of the READ_FILE variable and
     * other factors such as density, radius and optimal height.
     * The objects are marked with the red color value of the variable COLOR
     * and then stored in a .png-file with the name of the variable SAVE_FILE
     */
    public List<Vec3> placeObjects() {
        ITerrainGenerationService gm= ServiceLocator.INSTANCE.getTerrainGenerationService();
        NOISE_MAP = gm.getRandomNoisemap(1.0f,1.0f);
        OBJECT_MAP = new boolean[WIDTH][HEIGHT];
        setFalse(OBJECT_MAP);
        CalculatePlacement();
        return vecList;

    }

    private void setFalse(boolean toClear[][]) {
        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++)
                toClear[j][i] = false;
    }


    private void CalculatePlacement() { //calculates for each pixel if an object will be placed or not and marks it in OBJECT_MAP
        for (int i = 0; i < WIDTH-1; i++) {
            for (int j = 0; j < WIDTH-1; j++) {
                float height = terrainHeight.getHeight(j,i);
                if (height != 0 && canPlace(height, j, i)) {
                    OBJECT_MAP[j][i] = true;//setRGB(j, i, new Color(COLOR,0,0).getRGB());
                    vecList.add(new Vec3(j,height,i));
                    j+=(int)Math.round(RADIUS);
                }
            }
        }
    }

    private boolean canPlace(float height, int x, int y) { //checks if an object can be placed in coordinate(x,y)
        return (Math.random() <= GetProbability(height, x, y) && isClear(x, y));
    }

    private boolean isClear(int x, int y) { // checks the surroundings of point x,y if objects are to close. returns true if not
        int rounded = (int) Math.round(RADIUS);
        for (int i = 1; i < rounded; i++) {
            for (int j = -rounded; j < rounded; j++) {
                int tempX = x - j;
                int tempY = y - i;
                if (tempY < 0) {
                    return true;
                }
                if (tempX >= 0 && tempX < WIDTH && (j * j) + (i * i) <= (RADIUS * RADIUS)) {
                    if (OBJECT_MAP[tempX][tempY]) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private double GetProbability(float height, int x, int y) { // Calculates the probability for an object to be placed on the point x,y
        double prob = height / TerrainServiceConstants.MAX_HEIGHT;
        Color c = new Color(NOISE_MAP.getRGB(x,y));
        prob = NormalDistribution.solve(prob, OPTIMAL_HEIGHT, 0.01, 0, 0.5);
        prob *= NormalDistribution.solve(c.getBlue(), 1, 0.02, 0, 0.5);
        prob *= DENSITY;
        return prob;
    }


    private double setToInterval(double fitToInterval, Double max, Double min) {
        //sets a double to fit in the specified interval, if max or min is null they will be ignored
        if (min != null && fitToInterval < min) {
            return min;
        } else if (max != null && fitToInterval > max) {
            return max;
        } else {
            return fitToInterval;
        }
    }

    //<editor-fold desc="Setters">
    public void setColor(int color) {
        this.COLOR = (int) setToInterval(color, 255.0, 0.0);
    }

    public void setRadius(double radius) {
        this.RADIUS = setToInterval(radius, 0.0, null);
    }

    public void setOptimalHeight(double optimalHeight) {
        this.OPTIMAL_HEIGHT = setToInterval(optimalHeight, 1.0, 0.0);
    }

    public void setDensity(double density) {
        this.DENSITY = setToInterval(density, 1.0, 0.0);
    }
    //</editor-fold>
}
