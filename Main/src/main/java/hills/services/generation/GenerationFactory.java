package hills.services.generation;

/**
 * @Author Cornelis T Sjöbeck
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
