package hills.services.generation;

import java.awt.image.BufferedImage;

/**
 * @Author Gustav Engsmyre
 */
public interface IGenerationMediator {
    /**
     * generates two PNG files, one with Height data, and one with data about Normals.
     */
    void generateWorldImage();


    /**
     * Generates a direction that is psudo random, but correlates closely to the previous direction
      * @param x the number used for generating the direction. Should depending on wanted randomness be close to previous used value
     * @return returns a number between 0 and 1 depending on params.
     */
    double generateDirection(float x);

    BufferedImage getRandomNoisemap(double frequency, double scale);
}
