package hills.controller.ModelInterfaceControllers;

import hills.model.IRenderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustav on 2017-05-15.
 */
public class RenderController {
    private List<IRenderable> renderables = new ArrayList<>();
    public RenderController(){

    }
    public void addRenderable(IRenderable renderable){
        renderables.add(renderable);
    }
}
