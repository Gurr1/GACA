package hills.Anders;

import hills.Gurra.NoiseMapGenerator;

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

    private int width = 2048;
    private int color = 255;
    private double density = 0.5;
    private double radius = 10;
    private BufferedImage densitymap;
    private BufferedImage heightMap;
    private BufferedImage noisemap;

    public ObjectPlacement(String pngFile) {
        init(pngFile);
    }

    public ObjectPlacement(int width, int color, double density, double radius, String pngFile) {
        this.width = width;
        this.density = density;
        this.color = color;
        this.radius = radius;
        init(pngFile);


    }

    private void init(String pngFile) {
        try {
            heightMap = ImageIO.read(new File(pngFile));
            //     new NoiseMapGenerator().create2DNoise(1);
            //   noisemap = ImageIO.read(new File(pngFile));
        } catch (IOException e) {
            System.out.println("failed to read file");
            e.printStackTrace();
            return;
        }
        densitymap = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
        clearImage(densitymap);
        CalculatePlacement();
        try {
            ImageIO.write(densitymap, "png", new File("Main/src/main/resources/ObjectMap.png"));
        } catch (IOException e) {
            System.out.println("Failed to create File");
            e.printStackTrace();
        }

    }

    private void CalculatePlacement() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(heightMap.getRGB(j, i));
                if (c.getRed() == 0 && c.getBlue() == 0 && canPlace(c, j, i)) {
                    densitymap.setRGB(j, i, new Color(color, c.getGreen(), c.getBlue()).getRGB());
                } else {
                    densitymap.setRGB(j, i, c.getRGB());
                }
            }
        }
    }

    private boolean canPlace(Color c, int x, int y) {
        return (isClear(x, y) && Math.random() <= GetProbability(c, x, y));
    }

    private boolean isClear(int x, int y) {
        int rounded = (int) Math.round(radius);
        for (int i = 0; i < rounded; i++) {
            for (int j = -rounded; j < rounded; j++) {
                int tempX = x - j;
                int tempY = y - i;
                if (tempY < 0) {
                    return true;
                }
                if (tempX >= 0 && tempX < width && (j * j) + (i * i) <= (radius * radius)) {
                    Color c = new Color(densitymap.getRGB(tempX, tempY));
                    if (c.getRed() == 255) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private double GetProbability(Color c, int x, int y) {
        double prob = c.getGreen() / 255.0;
        prob = NormalDistribution.solve(prob, 0.5, 0.2, 0, 1);
        // prob *= noisemap.getRGB(x, y);
        prob *= density;
        //add noisemap
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
