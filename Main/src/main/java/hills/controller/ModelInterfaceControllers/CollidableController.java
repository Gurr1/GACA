package hills.controller.ModelInterfaceControllers;

import hills.model.ICollectible;
import hills.model.ICollidable;
import hills.model.PlayerCollidable;
import hills.services.collision.CollisionDetection;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 2017-05-06.
 */
public class CollidableController {     // Visitor patter?


   @Getter private List<ICollidable> collidables;


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
        CollisionDetection cd = new CollisionDetection();
        for (int i = 0; i < collidables.size()-1; i++) {
            for (int j = i+1; j < collidables.size(); j++) {
                if (cd.isColliding(collidables.get(i), collidables.get(j))) {
                    // Check if colliding.
                    Class[] classes = collidables.get(i).getClass().getInterfaces();
                    Class[] classes2 = collidables.get(j).getClass().getSuperclass().getInterfaces();
                    for (Class c : classes) {
                        for (Class c2 : classes2) {
                            handleCollision(c, c2);
                        }
                    }
                }
            }
        }
    }
        //use collision service
        // Not the best solution. Handles each of the
    private void handleCollision(Class c, Class c2) {
        System.out.println(c + " " + c2);
        if(c == ICollectible.class || c2 == ICollectible.class){
            System.out.println("collectible");
            if(c == PlayerCollidable.class){
                System.out.println("coins");
                PlayerCollidable pl = (PlayerCollidable) c.cast(PlayerCollidable.class);
                pl.collectCollectible((ICollidable) c2.cast(ICollectible.class));
            }
        }
    }


}
