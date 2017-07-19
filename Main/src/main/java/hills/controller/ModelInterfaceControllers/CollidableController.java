package hills.controller.ModelInterfaceControllers;

import hills.model.*;
import hills.services.ServiceLocator;
import hills.services.collision.ICollisionDetection;
import hills.util.math.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Gustav Engsmyre, Anders Hansson
 */
public class CollidableController {     // Visitor patter?

    private List<ICollidable> collidables;

    private ICollidable objectToRemove;

    private PlayerCollidable player;

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
        Class[] classes = collidable.getClass().getInterfaces();
        for (Class c : classes){
            if(c == PlayerCollidable.class) {
                player = (PlayerCollidable) collidable;
                break;
            }
        }

        collidables.add(collidable);
    }

    public void update(List<ImmovableObject> staticObjects) {
        ICollisionDetection cd = ServiceLocator.INSTANCE.getCollisionDetection();
        for (int i = 0; i < collidables.size()-1; i++) {
            for (int j = i+1; j < collidables.size(); j++) {
                if (cd.isColliding(collidables.get(i), collidables.get(j))) {
                    // Check if colliding.
                    Class[] classes = collidables.get(i).getClass().getInterfaces();
                    Class[] classes2 = collidables.get(j).getClass().getSuperclass().getInterfaces();
                    for (Class c : classes) {
                        for (Class c2 : classes2) {
                            if(collidables.size() <= j){
                                break;
                            }
                           handleCollision(collidables.get(i), collidables.get(j), c, c2);
                        }
                    }
                }
            }
        }
        if(player != null)
            for (ICollidable c : staticObjects ) {
                if(c.getBoundingSphere().intersects(player.getBoundingSphere()))
                    handleCollision(player, c, PlayerMovable.class, ICollidable.class);
            }
    }
        // Not the best solution. Should handle every other collision type aswell.
    private void handleCollision(ICollidable co1, ICollidable co2, Class c, Class c2) {
        if(c == ICollectible.class || c2 == ICollectible.class){
            if(c == PlayerCollidable.class){
                PlayerCollidable pc = (PlayerCollidable) co1;
                pc.collectCollectible((ICollectible) co2);
                collidables.remove(co2);
                objectToRemove = co2;
            }
        }
       else if(c == PlayerMovable.class || c2 == PlayerMovable.class){
            PlayerMovable collide;
            ICollidable non;
            if(c == PlayerMovable.class){
                 collide = (PlayerMovable) co1;
                 non = co2;
            }
            else {
                collide = (PlayerMovable) co2;
                non = co1;
            }

            Vec3 v = non.getBoundingSphere().getPos().sub(collide.get3DPos());
            if(!collide.getVelocity().equals(new Vec3(0,0,0)))
                collide.addVelocity(collide.get3DPos().sub(non.getBoundingSphere().getPos()));

        }
    }


}
