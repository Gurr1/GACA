package hills.model;

import hills.controller.ServiceMediator;
import hills.services.terrain.TerrainServiceConstants;
import hills.util.math.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Deltagare on 2017-03-30.
 */
public class World{
    private static World world;
    public static World getInstance() {
        if(world == null){
            world = new World();
        }
            return world;
    }

    public static World createInstance() {
        world = new World();
        return world;
    }

    private Player player;
    private int frame = 0;
    double delta;
    private List<ICollectible> collectibles = new ArrayList<>();
    private List<Creature> creatureList = new ArrayList<>();
    private int creatureCount = 1;
    Random rand = new Random();

    private World() {
        player = new Player(new Vec3(100, 0, 100));
        world = this;
        for(int i = 0; i < creatureCount; i++){
            creatureList.add(new Sheep(null, ));
        }
    }
    public Vec3 getPlayerPosition(){
        return player.get3DPos();
    }
    public Vec3 getPlayerHeading(){
        return player.getForward();
    }
    public Vec3 getPlayerUp(){
        return player.getUp();
    }
    public Vec3 getPlayerRight(){
        return player.getRight();
    }
    public int getNNPCs(){
        return creatureCount;
    }
    public void setNPCHeight(float height, int i){
        creatureList.get(i).setHeight(height);
    }
    public void updateWorld(double delta){
        frame++;
        this.delta = delta;
        if(frame%100 == 0)
        for (Creature creature : creatureList){
            creature.moveRandomly();
        }
    }
    public Vec3 getCreaturePosition(int i){
        return creatureList.get(i).get3DPos();
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

// Checks if the player is in the general same position as an collectible.
    private void checkCollectibles() {
        for(int i = 0; i<collectibles.size(); i++){
            if(player.getBoundingSphere().intersects(collectibles.get(i).getBoundingSphere())){
                player.collected(collectibles.get(i));
                collectibles.remove(i);
            }
        }
    }

}
