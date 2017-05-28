package hills.services.generation;

import java.awt.image.BufferedImage;

/**
 * @Author Gustav Engsmyre
 */
public interface ITerrainGenerationService {
    /**
     * generates two PNG files, one with Height data, and one with data about Normals.
     */
    void generateWorldImage();



    BufferedImage getRandomNoisemap(double frequency, double scale);
}
