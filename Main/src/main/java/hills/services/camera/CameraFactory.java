package hills.services.camera;

/**
 * @author Cornelis T Sjöbeck
 */
public class CameraFactory {

    private CameraFactory(){

    }

    private static CameraService cameraService = null;

    public static ICameraDataService getCameraDataServiceInstance(){
            if(cameraService == null)
                cameraService = new CameraService();
        return cameraService;
    }
    public static ICameraUpdateService getCameraUpdateServiceInstance(){
        if(cameraService == null){
            cameraService = new CameraService();
        }
        return cameraService;
    }
}
