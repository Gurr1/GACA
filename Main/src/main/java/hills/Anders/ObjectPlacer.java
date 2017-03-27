package hills.Anders;

import com.sun.istack.internal.Nullable;
import hills.Gurra.NoiseMapGenerator;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by Anders on 2017-03-21.
 */
public class ObjectPlacer {

    private int WIDTH;
    private int HEIGHT;
    private int COLOR = 255;
    private double DENSITY = 0.2;
    private double RADIUS = 1;
    private double OPTIMAL_HEIGHT = 0.3;
    @Setter private String READ_FILE;
    @Setter private String SAVE_FILE;
    private BufferedImage OBJECT_MAP;
    private BufferedImage HEIGHT_MAP;
    private NoiseMapGenerator NOISE_MAP;
    private List<Point> pointList = new ArrayList<>();

    /**
     * Constructor with a File to be read from and a filename to save after placement
     * @param readFile name of the file to be read from
     * @param saveFile name of the file that will be created after ObjectPlacement() is run
     */
    public ObjectPlacer(String readFile, String saveFile) {
        READ_FILE = readFile;
        SAVE_FILE = saveFile;
    }

    /**
     *
     * @param color value of the color the objects will be marked as, between 0 and 255 (standard 255)
     * @param density value of  objects will be placed between 0 and 1(standard 0.1)
     * @param radius value of the minimum distance objects will have from eachother above 0 (standard 1)
     * @param optimalHeight value of the most likely height objects will spawn at (standard 0.3)
     * @param readFile name of the file to be read from
     * @param saveFile name of the file that will be created after ObjectPlacement() is run
     */
    public ObjectPlacer(int color, double density, double radius, double optimalHeight, String readFile, String saveFile) {
        setDensity(density);
        setColor(color);
        setRadius(radius);
        setOptimalHeight(optimalHeight);
        this.READ_FILE = readFile;
        this.SAVE_FILE = saveFile;
    }

    /**
     * Places objects based on the green color value of the READ_FILE variable and
     * other factors such as density, radius and optimal height.
     * The objects are marked with the red color value of the variable COLOR
     * and then stored in a .png-file with the name of the variable SAVE_FILE
     */
    public void placeObjects() {
        try {
            HEIGHT_MAP = ImageIO.read(new File(READ_FILE));
            WIDTH = HEIGHT_MAP.getWidth();
            HEIGHT = HEIGHT_MAP.getHeight();
        } catch (IOException e) {
            System.out.println("failed to read file");
            e.printStackTrace();
            return;
        }
        NOISE_MAP = new NoiseMapGenerator(new Random().nextLong());
        //NOISE_MAP.create2DNoiseImage("ObjectDensity");
        OBJECT_MAP = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        copyImage(OBJECT_MAP, HEIGHT_MAP);
        CalculatePlacement();

        try {
            ImageIO.write(OBJECT_MAP, "png", new File("Main/src/main/resources/" + SAVE_FILE + ".png"));
        } catch (IOException e) {
            System.out.println("Failed to create File");
            e.printStackTrace();
        }
    }

    private void copyImage(BufferedImage copyTo, BufferedImage copyFrom) {
        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++)
                copyTo.setRGB(j, i, copyFrom.getRGB(j, i));
    }


    private void CalculatePlacement() { //calculates for each pixel if an object will be placed or not and marks it in OBJECT_MAP
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                Color c = new Color(HEIGHT_MAP.getRGB(j, i));
                if (c.getRed() == 0 && c.getBlue() == 0 && canPlace(c, j, i)) {
                    OBJECT_MAP.setRGB(j, i, new Color(COLOR, c.getGreen(), c.getBlue()).getRGB());
                   // pointList.add(new Point(j,i));
                    j+=(int)Math.round(RADIUS);
                }
            }
        }
    }

    private boolean canPlace(Color c, int x, int y) { //checks if an object can be placed in coordinate(x,y)
        return (isClear(x, y) && Math.random() <= GetProbability(c, x, y));
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
                    Color c = new Color(OBJECT_MAP.getRGB(tempX, tempY));
                    if (c.getRed() > 0) {
                        return false;
                    }
                }
            }
        }
       /* for (Point p : pointList) {
            int tempX = x - p.x;
            int tempY = y - p.y;
            if (tempY < 0) {
                return true;
            }
            if (tempX >= 0 && tempX < WIDTH && (tempX * tempX) + (tempY * tempY) <= (RADIUS * RADIUS)) {
                Color c = new Color(OBJECT_MAP.getRGB(tempX, tempY));
                if (c.getRed() > 0) {
                    return false;
                }
            }*/


        return true;
    }

    private double GetProbability(Color c, int x, int y) { // Calculates the probability for an object to be placed on the point x,y
        double prob = c.getGreen() / 255.0;
        prob = NormalDistribution.solve(prob, OPTIMAL_HEIGHT, 0.01, 0, 0.5);
        prob *= NormalDistribution.solve(NOISE_MAP.getDoubleValue(x / 4, y / 4), 1, 0.02, 0, 0.5);
        prob *= DENSITY;
        return prob;
    }


    private double setToInterval(double fitToInterval, @Nullable Double max, @Nullable Double min) {
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

