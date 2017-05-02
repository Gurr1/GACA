package hills.view;

import hills.model.IMovable;
import hills.model.World;

import java.util.List;

/**
 * Created by gustav on 2017-04-29.
 */
public class ModelDataHandler {
    private static World w;
    private static CameraModel cameraModel;
    ModelDataHandler(World w){
        this.w = w;
        cameraModel = CameraModel.getInstance();
    }
    protected static IMovable getCharacter(int index){
        return null;
    }
    protected static void update(double delta){
        updateModel(delta);
        int nChars = w.getNNPCs();
        for(int i = 0; i<nChars; i++){
            // get NPC pos, direction, etc and display that
        }
        updateCamera();
    }

    private static void updateCamera() {
      //  w.getPlayerPosition();
       // w.getPlayerHeading();
       // cameraModel.setParams();
    }

    protected static void updateModel(double delta){
        w.updateWorld(delta);
    }
}
