package hills.engine.system.domainModel;

import hills.Gurra.TerrainData;
import hills.engine.math.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deltagare on 2017-03-30.
 */
public class World {
    private static World world;
    public static World getInstance() {
        return world;
    }

    public void setPlayerPosition(Vec3 position) {
        float z = storedVectors[(int) position.getX()][(int) position.getZ()].getPosition().getY();
        position = new Vec3(position.getX(), position.getZ(), z);
        player.setPos(position);
        System.out.println(position);
    }

    enum Axis{
        x, y
    }

    Player player;
    private final int HEIGHT = 2048;
    private final int WIDTH = 2048;
    List<Coin> coins;
    Vec3 pos;
    TerrainData[][] storedVectors = new TerrainData[WIDTH][HEIGHT];

    public World(TerrainData[][] heights) {
        world = this;
        player = new Player(new Vec3(100, 100, 0)); // TODO: replace z with get height from heightmap
        coins = getCoins(10);
        storedVectors = heights;
    }

    private List<Coin> getCoins(int nrOfCoins) { // TODO: add feature
        return new ArrayList<>();
    }

    private List<ICollidable> checkCollision(ICollidable collidable, List<ICollidable> toCheck) {
        List<ICollidable> isColiding = new ArrayList<>();
        for (ICollidable c : toCheck) {
            if (collidable.getBoundingSphere().intersects(c.getBoundingSphere()))
                isColiding.add(c);
        }
        return isColiding;
    }
    public Vec3 getHeight(int x, int y){
        return storedVectors[x][y].getPosition();
    }
}
