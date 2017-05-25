package hills.services.generation;

/**
 * Created by corne on 5/25/2017.
 */
public class GenerationFactory {

    private GenerationFactory(){

    }

    private static GenerationMediator generationService = null;

    public static IGenerationMediator getGenerationServiceInstance() {
        if(generationService == null){
            generationService = new GenerationMediator();
        }
        return generationService;
    }
}
