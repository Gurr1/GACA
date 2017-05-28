package domainModelTests;

import hills.controller.ModelInterfaceControllers.CollidableController;
import hills.model.Coin;
import hills.model.ICollectible;
import hills.model.ICollidable;
import hills.model.Player;
import hills.util.math.Vec3;
import hills.util.model.Mesh;
import hills.util.model.Model;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Anders on 2017-05-25.
 */
public class CollidableControllerTest {
    Coin coin;
    Player player;
    CollidableController collidableController;

    @Before
    public void init(){
        coin = new Coin(new Vec3(4,4,4),new Model(new Mesh[1]));
        player = new Player(new Vec3(1,1,1));
        collidableController = new CollidableController();
        collidableController.addCollidable(player);
        collidableController.addCollidable(coin);

        for (int i = 0; i < 5; i++) {
            collidableController.update();
        }
    }

    @Test
    public void testCollision(){

        player.setPosition(new Vec3(1,1,1));

        Assert.assertFalse(player.getBoundingSphere().intersects(coin.getBoundingSphere()));
        Assert.assertNull(collidableController.getObjectToRemove());
        player.setPosition(new Vec3(3,3,3));
        collidableController.update();
        Assert.assertTrue(player.getBoundingSphere().intersects(coin.getBoundingSphere()));
        Assert.assertNotNull("NonNull",collidableController.getObjectToRemove());
        Assert.assertEquals(coin, collidableController.getObjectToRemove());
    }

}
