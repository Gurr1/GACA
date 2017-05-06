package hills.controller;

import hills.model.Player;
import hills.util.math.Vec3;

/**
 * Created by gustav on 2017-05-06.
 */
public class EntityFactory {
    public static Player createPlayer(Vec3 position){
        return new Player(position);
    }
}
