package hills.controller;

import hills.model.IMovable;
import hills.model.World;
import hills.view.CameraModel;

/**
 * Created by gustav on 2017-04-29.
 */
public enum ModelDataHandler {
    INSTANCE();
    private World w;
    private CameraModel cameraModel;
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
        cameraModel.setParams(w.getPlayerPosition(), w.getPlayerHeading(), w.getPlayerRight(), w.getPlayerUp());
    }

    protected void updateModel(double delta){
        w.updateWorld(delta);
    }

}
