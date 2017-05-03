package hills.view;

import hills.model.IMovable;
import hills.model.World;

/**
 * Created by gustav on 2017-04-29.
 */
public class ModelDataHandler {
    private World w;
    private CameraModel cameraModel;
    private static ModelDataHandler instance;
    private ModelDataHandler(){
        this.w = World.createInstance();
        cameraModel = CameraModel.getInstance();
    }
    private IMovable getCharacter(int index){
        return null;
    }
    public void update(double delta){
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

    public static ModelDataHandler getInstance(){
        if(instance == null){
            instance = new ModelDataHandler();
        }
        return instance;
    }
}
