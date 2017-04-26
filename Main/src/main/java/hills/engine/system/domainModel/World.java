package hills.engine.system.domainModel;

import hills.Gurra.Models.CameraModel;
import hills.Gurra.TerrainData;
import hills.engine.math.Vec2;
import hills.engine.math.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Deltagare on 2017-03-30.
 */
public class World implements OnMoveListener, OnCreatureMoveListener{
    private static World world;
    private double waterHeight = 30;
    public static World getInstance() {
            return world;
    }

    public static World createInstance(TerrainData[][] terrainDatas) {
        world = new World(terrainDatas);
        return world;
    }

    private Player player;
    private final int HEIGHT = 2048;
    private final int WIDTH = 2048;
    private List<Coin> coins;
    private int frame = 0;
    private TerrainData[][] storedVectors = new TerrainData[WIDTH][HEIGHT];
    double delta;
    private CameraModel cameraModel;
    private List<ICollectible> collectibles = new ArrayList<>();
    private List<Creature> creatureList = new ArrayList<>();
    private int creatureCount = 1;
    Random rand = new Random();

    private World(TerrainData[][] heights) {
        player = new Player(new Vec3(100, 0, 100));
        storedVectors = heights;
        Vec3 spawnPosition = createSpawn();
        player.setPosition(spawnPosition);
        coins = getCoins(10);
        world = this;
        for(int i = 0; i < creatureCount; i++){
            creatureList.add(new Sheep(null, createSpawn()));
            creatureList.get(i).addListener(this);
        }
        player.addPositionObserver(this);
        cameraModel = CameraModel.getInstance();
    }

    private Vec3 createSpawn() {
        float x;
        float z;
        float y;
        do{
        x = rand.nextInt(HEIGHT);
        z = rand.nextInt(WIDTH);
        y = getHeight(x,z);

        }while(y<waterHeight);
        return new Vec3(x,y,z);
    }

    public void updateWorld(double delta){
        frame++;
        this.delta = delta;
        if(player.isToUpdate()) {
            cameraModel.setParams(player.get3DPos(), player.getForward(), player.getRight(), player.getUp());
        }
        player.setToUpdate(false);
        if(frame%100 == 0)
        for (Creature creature : creatureList){
            creature.moveRandomly();

        }
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

    /**
     * Corrects the position so "up" position corresponds to the world floor.
     * @param movable The object that's moving
     */
    @Override
    public void moving(IMovable movable) {
        movable.setPosition(movable.get3DPos().add(player.getVelocity().mul((float) delta)));
        double heightStep = 100d / 255;
        Vec3 position = movable.get3DPos();
        Vec3 revisedPosition = new Vec3(position.getX(), (float) (getHeight(movable.get3DPos())*heightStep), position.getZ());
        movable.setPosition(revisedPosition);
        checkCollectibles();
    }

    /**
     *  Will return the height of the terrain at the x, z coordinate.<br>
     *  OBS! If out of bounds will return 0.0f.
     * @param x - The x coordinate to check height from terrain.
     * @param z - The z coordinate to check height from terrain.
     * @return The height of the terrain at the x, z coordinate.
     */
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


// Checks if the player is in the general same position as an collectible.
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
