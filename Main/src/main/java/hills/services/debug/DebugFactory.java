package hills.services.debug;

/**
 * @author Cornelis T Sjöbeck
 */
public class DebugFactory {

    private DebugFactory(){

    }

    private static DebugService debugService = null;

    public static IDebugService getDebugServiceInstance(){
        if(debugService == null)
            debugService = new DebugService();

        return debugService;
    }
}
