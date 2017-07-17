package hills.controller.ModelInterfaceControllers;

import hills.model.ICollidable;
import hills.model.IRenderable;
import hills.util.shader.ShaderProgram;
import hills.view.RenderLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Gustav Engsmyre
 */
public class RenderController {
    private List<IRenderable> renderables = new ArrayList<>();
    public RenderController(){

    }
   public void addRenderable(IRenderable renderable){
        renderables.add(renderable);
    }

    public void updateRender(List<IRenderable> renderables2){
        for(IRenderable renderable : renderables) {
            RenderLocator.INSTANCE.getModelBatchable().batch(ShaderProgram.STATIC,
                    renderable.getModel(), renderable.getMatrix());
        }
        for(IRenderable renderable : renderables2){
            RenderLocator.INSTANCE.getModelBatchable().batch(ShaderProgram.STATIC,
                    renderable.getModel(), renderable.getMatrix());
        }
    }

    public void removeObject(ICollidable objectToRemove) {
        renderables.remove(objectToRemove);
    }

}
