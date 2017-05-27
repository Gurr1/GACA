package hills.services.debug;

/**
 * Created by corne on 5/24/2017.
 */
public class DebugFactory {

    private DebugFactory(){

    }

    public static DebugService debugService = null;

    public static IDebugService getDebugServiceInstance(){
        if(debugService == null)
            debugService = new DebugService();

        return debugService;
    }
}
