package hills.view;

import hills.model.IMovable;
import hills.model.World;

import java.util.List;

/**
 * Created by gustav on 2017-04-29.
 */
public class ModelDataHandler {
    World w;
    CameraModel cameraModel;
    ModelDataHandler(World w){
        this.w = w;
        cameraModel = CameraModel.getInstance();
    }
    protected IMovable getCharacter(int index){
        return null;
    }
    protected void update(double delta){
        updateModel(delta);
        int nChars = w.getNNPCs();
        for(int i = 0; i<nChars; i++){
            // get NPC pos, direction, etc and display that
        }
        updateCamera();
    }

    private void updateCamera() {
      //  w.getPlayerPosition();
       // w.getPlayerHeading();
       // cameraModel.setParams();
    }

    protected void updateModel(double delta){
        w.updateWorld(delta);
    }
}
