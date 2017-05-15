package hills.controller;

import hills.model.*;
import hills.util.math.Vec3;
import hills.util.model.Model;

import java.util.Random;

/**
 * Created by gustav on 2017-05-06.
 */
public class EntityFactory {

    public static Player createPlayer(Vec3 position){
        return new Player(position);
    }

    public static Sheep createSheep(Model model, Vec3 position) {return new Sheep(position, model); }

    public static RamSheep createRamSheep(Vec3 position) {return new RamSheep(position); }

    public static Creature createAnySheep(Model model, Vec3 position) {
        Random random = new Random();
        int x = random.nextInt(2);

        if (x == 0){
            return new Sheep(position, model);
        }
        else return new RamSheep(position);
    }

    public static Creature createAnySheep(Model model, Vec3 position, int sheepChance){
        Random random = new Random();
        int x = random.nextInt(100);

        if ((sheepChance >= 100) || (x <= sheepChance)){return new Sheep(position, model); }

        else return new RamSheep(position);
    }

    public static Coin createCoin(Vec3 position) {return new Coin(position); }

    //TODO bug etc


}
