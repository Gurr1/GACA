package hills.controller.ModelInterfaceControllers;

import hills.model.ICollectible;
import hills.model.ICollidable;
import hills.model.PlayerCollidable;
import hills.services.ServiceLocator;
import hills.services.collision.CollisionDetection;
import hills.services.collision.ICollisionDetection;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Gustav Engsmyre, Anders Hansson
 */
public class CollidableController {     // Visitor patter?

    private List<ICollidable> collidables;

    private ICollidable objectToRemove;

    public ICollidable getObjectToRemove(){
        return objectToRemove;
    }

    public boolean isRemoved(){
        return objectToRemove!=null;
    }


    public CollidableController(){ //Add collision Service
        this.collidables = new ArrayList<>();
    }

    public CollidableController(List<ICollidable> collidables){
        this.collidables = new ArrayList<>();
        this.collidables.addAll(collidables);
    }


    public void addCollidable(ICollidable collidable){
        collidables.add(collidable);
    }

    public void update() {
        ICollisionDetection cd = ServiceLocator.INSTANCE.getCollisionDetection();
        for (int i = 0; i < collidables.size()-1; i++) {
            for (int j = i+1; j < collidables.size(); j++) {
                if (cd.isColliding(collidables.get(i), collidables.get(j))) {
                    // Check if colliding.
                    Class[] classes = collidables.get(i).getClass().getInterfaces();
                    Class[] classes2 = collidables.get(j).getClass().getSuperclass().getInterfaces();
                    for (Class c : classes) {
                        for (Class c2 : classes2) {
                           if( handleCollision(collidables.get(i), collidables.get(j), c, c2))
                               break;
                        }
                    }
                }
            }
        }
    }
        // Not the best solution. Should handle every other collision type aswell.
    private boolean handleCollision(ICollidable co1, ICollidable co2, Class c, Class c2) {
        if(c == ICollectible.class || c2 == ICollectible.class){
            if(c == PlayerCollidable.class){
                PlayerCollidable pc = (PlayerCollidable) co1;
                pc.collectCollectible((ICollectible) co2);
                collidables.remove(co2);
                objectToRemove = co2;
                return true;
            }
        }
        return false;
    }


}
