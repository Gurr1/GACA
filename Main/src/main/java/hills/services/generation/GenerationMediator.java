package hills.services.generation;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by gustav on 2017-04-27.
 */
public class GenerationMediator implements IGenerationMediator {

    RandomWalker walker;
    Random r;
    Terrain terrain;
    NoiseMapGenerator noiseMapGenerator;
    protected GenerationMediator(){
        r = new Random();
        terrain = new Terrain(r.nextLong());
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
