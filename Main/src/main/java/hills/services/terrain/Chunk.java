package hills.services.terrain;

import hills.model.ImmovableObject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 2017-07-17.
 * @Author Anders Hansson
 */
public class Chunk{
    @Getter private List<ImmovableObject> staticObjects;
    @Getter final private int x;
    @Getter final private int y;
    @Getter private int deltaX;
    @Getter private int deltaY;

    Chunk(int x, int y){
        this.x = x;
        this.y = y;
       // this.deltaX = deltaX;
       // this.deltaY = deltaY;
        staticObjects = new ArrayList<>();
    }

    void addObject(ImmovableObject object){
        staticObjects.add(object);
    }
}
