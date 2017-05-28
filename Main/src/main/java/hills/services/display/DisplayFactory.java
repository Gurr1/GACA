package hills.services.display;

/**
 * @author Cornelis T Sjöbeck
 */
public class DisplayFactory {

    private DisplayFactory(){

    }

    private static DisplayService displayService = null;

    public static DisplayServiceI getDisplayServiceInstance(){
        if(displayService == null)
            displayService = new DisplayService();

        return displayService;
    }

}
