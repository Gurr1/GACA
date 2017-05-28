package hills.services.generation;

/**
 * @Author Cornelis T Sjöbeck
 */
public class GenerationFactory {

    private GenerationFactory(){

    }

    private static GenerationMediator generationService = null;

    public static ITerrainGenerationService getTerrainGenerationService() {
        if(generationService == null){
            generationService = new GenerationMediator();
        }
        return generationService;
    }
    public static IDirectionGenerationService getDirectionGenerationService(){
        if(generationService == null){
            generationService = new GenerationMediator();
        }
        return generationService;
    }
}
