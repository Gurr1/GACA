package hills.model;

import hills.services.terrain.TerrainService;
import hills.util.math.Vec3;
import hills.view.CameraModel;

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

    public static World createInstance() {
        world = new World();
        return world;
    }

    private Player player;
    private final int HEIGHT = TerrainService.TERRAIN_HEIGHT;
    private final int WIDTH = TerrainService.TERRAIN_WIDTH;
    private List<Coin> coins;
    private int frame = 0;
    double delta;
    private CameraModel cameraModel;
    private List<ICollectible> collectibles = new ArrayList<>();
    private List<Creature> creatureList = new ArrayList<>();
    private int creatureCount = 1;
    Random rand = new Random();

    private World() {
        player = new Player(new Vec3(100, 0, 100));
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
    public int getNNPCs(){
        return creatureCount;
    }
    public void updateWorld(double delta){
        frame++;
        this.delta = delta;
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
    public float getHeight(float x, float z) {
     //   ServiceMediator.
        return 0;
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

    @Override
    public float getCreaturePosition(Creature creature) {
        return getHeight(creature.get3DPos().getX(), creature.get3DPos().getZ());
    }
}
