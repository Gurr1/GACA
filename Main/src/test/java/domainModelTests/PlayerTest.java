package domainModelTests;

import hills.controller.InputControllers.PlayerControllerKeyboard;
import hills.util.math.Vec3;
import hills.model.Player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.glfw.GLFW;

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

    @Test public void testVelocity(){       // not done. all move velocity behaviour should be tested.
        PlayerControllerKeyboard playerControllerKeyboard = new PlayerControllerKeyboard();
        playerControllerKeyboard.keyEvent(GLFW.GLFW_KEY_W, 0, GLFW.GLFW_PRESS, 0);
        Vec3 velocity = player.getVelocity();
        for(int i = 0; i<10; i++) {
            playerControllerKeyboard.keyEvent(GLFW.GLFW_KEY_W, 0, GLFW.GLFW_PRESS, 0);
            Assert.assertFalse(velocity == player.getVelocity());
            Assert.assertTrue(velocity.equals(player.getVelocity()));
        }
        playerControllerKeyboard.keyEvent(GLFW.GLFW_KEY_W, 0, GLFW.GLFW_RELEASE, 0);
        Assert.assertFalse(velocity.equals(player.getVelocity()));
    }

}
