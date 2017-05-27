package hills.services.camera;

/**
 * Created by corne on 5/24/2017.
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
