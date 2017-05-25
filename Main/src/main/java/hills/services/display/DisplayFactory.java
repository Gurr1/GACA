package hills.services.display;

/**
 * Created by corne on 5/24/2017.
 */
public class DisplayFactory {

    private DisplayFactory(){

    }

    private static DisplayService displayService = null;

    public static DisplayService getDisplayServiceInstance(){
        if(displayService == null)
            displayService = new DisplayService();

        return displayService;
    }

}
