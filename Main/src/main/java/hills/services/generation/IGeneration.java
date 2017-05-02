package hills.services.generation;

/**
 * Created by gustav on 2017-04-27.
 */
public interface IGeneration {
    /**
     * generates two PNG files, one with Height data, and one with data about Normals.
     */
    void generateWorldImage();

    /**
     * generates both an PNG file of the map, but also an array of TerrainData with data on Heigt
     * @return a 2D array with data on each point of terrain.
     */
    double generateDirection(float x);
}
