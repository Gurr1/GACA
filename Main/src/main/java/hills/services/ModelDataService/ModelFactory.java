package hills.services.ModelDataService;

/**
 * @author Cornelis T Sjöbeck
 */
public class ModelFactory {

    private ModelFactory(){

    }

    private static IModelService modelService = null;

    public static IModelService getModelServiceInstance() {
        if(modelService == null){
            modelService = new CubeModel();
        }
        return modelService;
    }
}
