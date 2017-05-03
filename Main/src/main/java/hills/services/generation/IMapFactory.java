package hills.services.generation;

/**
 * Created by gustav on 2017-04-27.
 */
public interface IMapFactory {
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
}
