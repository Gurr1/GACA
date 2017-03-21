package hills.Anders;

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
public class ObjectPlacement {

    int width = 2048;
    int color = 255;
    double density = 0.5;
    double radius = 5;
    BufferedImage densitymap;
    BufferedImage heightMap;
    BufferedImage noisemap;
    Point currentPoint;
    List<Point> objects;

    ObjectPlacement(String pngFile) {
        init(pngFile);
    }

    ObjectPlacement(int width, int color, double density, double radius, String pngFile) {
        this.width = width;
        this.density = density;
        this.color = color;
        this.radius = radius;
        init(pngFile);


    }

    private void init(String pngFile) {
        try {
            heightMap = ImageIO.read(new File(pngFile));
        } catch (IOException e) {
            System.out.println("failed to read file");
            e.printStackTrace();
            return;
        }
        densitymap = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
        clearImage(densitymap);
        objects = new ArrayList<>();
        CalculatePlacement();
    }

    private void CalculatePlacement() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                currentPoint = new Point(j, i);
                Color c = new Color(heightMap.getRGB(j, i));
                if (c.getBlue() == 0 && canPlace(c)) {
                    densitymap.setRGB(j, i, new Color(color, c.getGreen(), c.getBlue()).getRGB());
                    objects.add(new Point(j,i));
                    j += (int) Math.round(radius);
                } else
                    densitymap.setRGB(j, i, c.getRGB());
            }
        }
    }

    private boolean canPlace(Color c) {
        return ( Math.random() <= GetProbability(c)); // add isClear Later
    }

    private boolean isClear() {     //TODO
        int rounded = (int) Math.round(radius);
        for (int i = 0; i < rounded; i++) {
            for (int j = -rounded; j < rounded ; j++) {

            }
        }
        return true;
    }

    private double GetProbability(Color c) {
        double prob = c.getGreen() / 255;
        prob = NormalDistribution.solve(prob, 0.5, 0.2, 0, 1);
        prob *= noisemap.getRGB(((int) currentPoint.getX()), ((int) currentPoint.getY()));
        prob *= density;
        return prob;
    }

    /**
     * Makes an image entirely black.
     *
     * @param image The image to make black
     */
    private static void clearImage(final BufferedImage image) {
        for (int i = 0; i < image.getWidth(); i++)
            for (int j = 0; j < image.getHeight(); j++)
                image.setRGB(i, j, Color.BLACK.getRGB());
    }


}
