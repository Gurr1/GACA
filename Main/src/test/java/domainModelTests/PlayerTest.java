package domainModelTests;

import hills.util.math.Vec3;
import hills.model.Player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Anders on 2017-03-31.
 */
public class PlayerTest {
    Player player;

    @Before
    public void initPlayer() {
        player = new Player(new Vec3(0, 0, 0));
    }

    @Test
    public void testDirection() {

        player.updateYaw(400);
        check();
        player.updateYaw(-300);
        check();
        player.updateYaw(-180);
        check();
        player.updateYaw(280);

        player.updatePitch(400);
        check();
        player.updatePitch(-300);
        check();
        player.updatePitch(-180);
        check();
        player.updatePitch(280);

        player.setPitch(90);
        Assert.assertEquals(90,player.getPitch(),0);
        player.setYaw(90);
        Assert.assertEquals(90,player.getYaw(),0);
    }
    private void check(){
        Assert.assertTrue(player.getYaw() <= 360 && player.getYaw() >= 0);
        Assert.assertTrue(player.getPitch() <= 360 && player.getPitch() >= 0);
        System.out.println(player.getYaw() + " yaw");
        System.out.println(player.getPitch() + " pitch");
    }

    @Test
    public void testPosition(){
        Vec3 pos = player.get3DPos();
        Vec3 diff = new Vec3(1,1,1);
        player.updatePosition();
        Assert.assertEquals(diff,player.get3DPos());
        player.setPosition(pos);
        Assert.assertEquals(pos,player.get3DPos());
    }

}
