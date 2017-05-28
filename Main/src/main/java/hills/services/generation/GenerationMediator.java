package hills.services.generation;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Author Gustav Engsmyre
 */
public class GenerationMediator implements ITerrainGenerationService, IDirectionGenerationService {

    private RandomWalker walker;
    private Random r;
    private TerrainGenerator terrain;
    private NoiseMapGenerator noiseMapGenerator;

    protected GenerationMediator(){
        r = new Random();
        terrain = new TerrainGenerator(r.nextLong());
        walker = new RandomWalker();
        noiseMapGenerator = new NoiseMapGenerator(r.nextLong());
    }
    @Override
    public void generateWorldImage() {
        terrain.createfinalIsland();
    }


    @Override
    public double generateDirection(float x) {
        return walker.generate(x);
    }

    @Override
    public BufferedImage getRandomNoisemap(double frequency, double scale) {
        return noiseMapGenerator.createNoiseMap(frequency, scale);
    }

}
