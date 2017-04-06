package hills.engine.system.domainModel;

import hills.Gurra.Models.CameraModel;
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
    CameraModel cameraModel;
    List<ICollectible> collectibles = new ArrayList<>();

    private World(TerrainData[][] heights) {
        player = new Player(new Vec3(100, 0, 100)); // TODO: replace z with get height from heightmap
        coins = getCoins(10);
        storedVectors = heights;
        world = this;
        player.addPositionObserver(this);
        cameraModel = CameraModel.getInstance();
    }

    public void updateWorld(double delta){
        this.delta = delta;
        if(player.isToUpdate()) {
            cameraModel.setParams(player.get3DPos(), player.getForward(), player.getRight(), player.getUp());
        }
        player.setToUpdate(false);
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
        float x = player.get3DPos().getX();
        float z = player.get3DPos().getZ();
        float newY = getGroundPosition(x, z);
        player.setPosition(new Vec3(x,newY, z));
        checkCollectibles();
    }

    private void checkCollectibles() {
        for(int i = 0; i<collectibles.size(); i++){
            if(player.getBoundingSphere().intersects(collectibles.get(i).getBoundingSphere())){
                player.collected(collectibles.get(i));
                collectibles.remove(i);
            }
        }
    }

    private float getGroundPosition(double x, double z) {
        return storedVectors[(int) x][(int) z].getPosition().getY();
    }
}
