package domainModelTests;

import hills.controller.ModelInterfaceControllers.MovableController;
import hills.model.Player;
import hills.util.math.Vec3;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Anders on 2017-03-31.
 */
public class PlayerTest {
    private Player player;

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
        Assert.assertEquals(diff,player.get3DPos());
        player.setPosition(pos);
        Assert.assertEquals(pos,player.get3DPos());
    }

    @Test
    public void testForwardVelocity(){       // not done. all move velocity behaviour should be tested.
        MovableController movableController = new MovableController();
        movableController.setPlayer(player);
        movableController.KeyPressed(GLFW.GLFW_KEY_W, 0);
        Vec3 velocity = player.getForwardVector();
        for(int i = 0; i<10; i++) {     // Tests if adding velocity works properly.
            movableController.KeyPressed(GLFW.GLFW_KEY_W, 0);
            Assert.assertFalse(velocity == player.getVelocity());
            Assert.assertTrue(velocity.equals(player.getVelocity()));
        }
        movableController.keyReleased(GLFW.GLFW_KEY_W, 0);
        Assert.assertTrue(player.getVelocity().equals(new Vec3(0,0,0)));
    }

    @Test
    public void testNonLinearVelocity(){
        MovableController movableController = new MovableController();
        movableController.setPlayer(player);
        movableController.KeyPressed(GLFW.GLFW_KEY_W, 0);
        movableController.KeyPressed(GLFW.GLFW_KEY_A, 0);
        System.out.println(player.getVelocity());
        Assert.assertTrue(player.getVelocity().equals((player.getForwardVector().add(player.getRightVector().mul(-1))).normalize()));
    }

    @Test
    public void testForwardAndBackwardSim(){
        MovableController movableController = new MovableController();
        movableController.setPlayer(player);
        movableController.KeyPressed(GLFW.GLFW_KEY_W, 0);
        movableController.KeyPressed(GLFW.GLFW_KEY_S, 0);
        System.out.println(player.getVelocity());
        Assert.assertTrue(player.getVelocity().equals((player.getForwardVector().mul(-1))));
    }

    @Test
    public void testSprint(){
        MovableController movableController = new MovableController();
        movableController.setPlayer(player);
        movableController.KeyPressed(GLFW.GLFW_KEY_W, GLFW.GLFW_MOD_SHIFT);
        System.out.println(player.getVelocity());
        Assert.assertTrue(player.getVelocity().equals((player.getForwardVector().mul(2))));
    }

    @Test
    public void testSprintForAndBack(){
        MovableController movableController = new MovableController();
        movableController.setPlayer(player);
        movableController.KeyPressed(GLFW.GLFW_KEY_W, GLFW.GLFW_MOD_SHIFT);
        movableController.KeyPressed(GLFW.GLFW_KEY_S, 0);
        System.out.println(player.getVelocity());
        Assert.assertTrue(player.getVelocity().equals(player.getForwardVector()));
    }
    @Test
    public void testSprintSide(){
        MovableController movableController = new MovableController();
        movableController.setPlayer(player);
        movableController.KeyPressed(GLFW.GLFW_KEY_W, GLFW.GLFW_MOD_SHIFT);
        movableController.KeyPressed(GLFW.GLFW_KEY_A, 0);
        System.out.println(player.getVelocity());
        Assert.assertTrue(player.getVelocity().equals((player.getForwardVector().mul(2).add(player.getRightVector().mul(-1)).normalize())));
    }

}
