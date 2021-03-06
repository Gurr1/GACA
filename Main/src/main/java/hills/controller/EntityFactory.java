package hills.controller;

import hills.model.*;
import hills.services.ServiceLocator;
import hills.util.math.Vec3;
import hills.util.model.Model;

import java.util.Random;

/**
 * @Author Cornelis Sjöbeck
 */
public class EntityFactory {

    public static Player createPlayer(Vec3 position){
        return new Player(position);
    }

    public static Sheep createSheep(Vec3 position) {
        Model model = ServiceLocator.INSTANCE.getModelService().getSheep();
        return new Sheep(position, model); }

    public static Goat createGoat(Vec3 position) {
        Model model = ServiceLocator.INSTANCE.getModelService().getSheep();
        return new Goat(position, model); }

    public static Creature createSheepOrGoat(Vec3 position) {
        Random random = new Random();
        int x = random.nextInt(2);
        Model model = ServiceLocator.INSTANCE.getModelService().getSheep();
        if (x == 0){
            return new Sheep(position, model);
        }
        else return new Goat(position, model);
    }

    public static Creature createSheepOrGoat(Vec3 position, int sheepChance){
        Random random = new Random();
        int x = random.nextInt(100);
        Model model = ServiceLocator.INSTANCE.getModelService().getSheep();
        if ((sheepChance >= 100) || (x <= sheepChance)){
            return new Sheep(position, model); }

        else return new Goat(position, model);
    }

    public static CollectibleObject createCoin(Vec3 position) {
        Model model = ServiceLocator.INSTANCE.getModelService().getCoin();
        return new Coin(position, model); }

    public static Bug createBug(Vec3 position){
        Model model = ServiceLocator.INSTANCE.getModelService().getBug();
        return new Bug(position, model);
    }

    public static CollectibleObject createAnyCollectible( Vec3 position){
        int chance = 50;
        Random random = new Random();
        int x = random.nextInt(100);

        if ( (x <= chance)){
            Model model = ServiceLocator.INSTANCE.getModelService().getBug();
            return new Bug(position, model); }
        else {
            Model model = ServiceLocator.INSTANCE.getModelService().getCoin();
            return new Coin(position, model);
        }
    }

    public static ImmovableObject createTree(Vec3 position){
        Model model = ServiceLocator.INSTANCE.getModelService().getTree();
        return new Tree(position, model);
    }

    //TODO bug etc


}
