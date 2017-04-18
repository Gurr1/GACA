package hills.engine.system.domainModel;

import hills.Gurra.Models.CameraModel;
import hills.Gurra.Terrain;
import hills.Gurra.TerrainData;
import hills.engine.math.Vec2;
import hills.engine.math.Vec3;
import hills.engine.system.terrain.TerrainSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Deltagare on 2017-03-30.
 */
public class World implements OnMoveListener{
    private static World world;
    private float speedMultiplier = 1000;
    private double waterHeight = 30;
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

    private Player player;
    private final int HEIGHT = 2048;
    private final int WIDTH = 2048;
    private List<Coin> coins;
    private TerrainData[][] storedVectors = new TerrainData[WIDTH][HEIGHT];
    double delta;
    private CameraModel cameraModel;
    private List<ICollectible> collectibles = new ArrayList<>();
    Random rand = new Random();

    private World(TerrainData[][] heights) {
        player = new Player(new Vec3(100, 0, 100));
        storedVectors = heights;
        Vec3 spawnPosition = createSpawn();
        player.setPosition(spawnPosition);
        coins = getCoins(10);
        world = this;
        player.addPositionObserver(this);
        cameraModel = CameraModel.getInstance();
    }

    private Vec3 createSpawn() {
        float x = 0;
        float z = 0;
        float y = 0;
        do{
        x = rand.nextInt(HEIGHT);
        z = rand.nextInt(WIDTH);
        y = getHeight(x,z);

        }while(y<waterHeight);
        return new Vec3(x,y,z);
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

    @Override
    public void moving() {
        System.out.println(player.getVelocity());
        player.setPosition(player.get3DPos().add(player.getVelocity().mul(speedMultiplier
                * (float) delta)));
        player.setPosition(player.get3DPos().add(player.getVelocity().mul(speedMultiplier
                * (float) delta)));
        double heightStep = 100d / 255;
        Vec3 position = player.get3DPos();
        Vec3 revisedPosition = new Vec3(position.getX(), (float) (getHeight(player.get3DPos())*heightStep), position.getZ());
        player.setPosition(revisedPosition);
        checkCollectibles();
    }

    public float getHeight(float x, float z){
        // Handle edge cases
        if(x < 1.0f || x > storedVectors.length - 1 || z < 1.0f || z > storedVectors[0].length - 1)
            return 0.0f;
        int intX = (int) x;
        int intZ = (int) z;

        boolean xGreater = x - intX > z - intZ;
        float heightA = storedVectors[intX][intZ].getPosition().getY(),
                heightB = xGreater ? storedVectors[intX + 1][intZ].getPosition().getY() : storedVectors[intX][intZ + 1].getPosition().getY(),
                heightC = storedVectors[intX + 1][intZ + 1].getPosition().getY();

        // Calculate barycentric coordinates of terrain triangle at x, 0.0, z.
        Vec2 A = new Vec2(intX, intZ);
        Vec2 v0 = (xGreater ? new Vec2(intX + 1, intZ) : new Vec2(intX, intZ + 1)).sub(A),
                v1 = new Vec2(1, 1),
                v2 = new Vec2(x, z).sub(A);

        float d00 = v0.getLengthSqr();
        float d01 = v0.dot(v1);
        float d11 = v1.getLengthSqr();
        float d20 = v2.dot(v0);
        float d21 = v2.dot(v1);

        float denom = d00 * d11 - d01 * d01;

        // Barycentric coordinates
        float a = (d11 * d20 - d01 * d21) / denom;
        float b = (d00 * d21 - d01 * d20) / denom;
        float c = 1.0f - a - b;

        return heightA * a + heightB * b + heightC * c + player.getPlayerHeight();
    }

    public float getHeight(Vec3 pos){
        return getHeight(pos.getX(), pos.getZ());
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
