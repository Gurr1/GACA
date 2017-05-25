package hills.services.ModelDataService;

/**
 * Created by corne on 5/25/2017.
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
