package hills.engine.system.domainModel;

import hills.Gurra.TerrainData;
import hills.engine.math.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deltagare on 2017-03-30.
 */
public class World implements OnMoveListener{
    private static World world;
    private float speedMultiplier = 1000;

    public static World getInstance() {
            return world;
    }

    public static World createInstance(TerrainData[][] terrainDatas) {
        world = new World(terrainDatas);
        return world;
    }


    enum Axis{
        x, y
    }

    Player player;
    private final int HEIGHT = 2048;
    private final int WIDTH = 2048;
    List<Coin> coins;
    TerrainData[][] storedVectors = new TerrainData[WIDTH][HEIGHT];
    double delta;


    private World(TerrainData[][] heights) {
        player = new Player(new Vec3(0, 0, 0)); // TODO: replace z with get height from heightmap
        coins = getCoins(10);
        storedVectors = heights;
        world = this;
        player.addPositionObserver(this);
    }

    public void updateWorld(double delta){
        this.delta = delta;
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


    @Override
    public void moving() {
        player.setPosition(player.get3DPos().add(player.getVelocity().mul(speedMultiplier
                * (float) delta)));
        player.setPosition(player.get3DPos().add(player.getVelocity().mul(speedMultiplier
                * (float) delta)));
        double x = player.get3DPos().getX();
        double z = player.get3DPos().getZ();
        Vec3 newPos = getGroundPosition(x, z);
        player.setPosition(newPos);
    }

    private Vec3 getGroundPosition(double x, double z) {
        return storedVectors[(int) x][(int) z].getPosition();
    }
}
