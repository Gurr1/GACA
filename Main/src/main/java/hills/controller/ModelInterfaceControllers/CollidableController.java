package hills.controller.ModelInterfaceControllers;

import hills.model.ICollidable;
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

    public void update(){
        //use collision service
    }


}
