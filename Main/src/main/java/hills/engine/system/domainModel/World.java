package hills.engine.system.domainModel;

import hills.engine.math.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Deltagare on 2017-03-30.
 */
public class World {

    Player player;
    List<Coin> coins;

    public World() {
        player = new Player(new Vec3(0, 0, 0)); // TODO: replace z with get height from heightmap
        coins = getCoins(10);
    }

    private List<Coin> getCoins(int nrOfCoins) { // TODO: add feature
        return new ArrayList<>();
    }

    private List<Colideable> checkCollision(Colideable collidable, List<Colideable> toCheck) {
        List<Colideable> isColiding = new ArrayList<>();
        for (Colideable c : toCheck) {
            if (collidable.getBoundingSphere().intersects(c.getBoundingSphere()))
                isColiding.add(c);
        }
        return isColiding;
    }
}
