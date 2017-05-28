package domainModelTests;

import hills.controller.ModelInterfaceControllers.MovableController;
import hills.model.Player;
import hills.util.math.Vec3;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.glfw.GLFW;

/**
 * @Author Gustav Engsmyre, Anders Hansson
 */
public class PlayerTest {
    private Player player;

    @Before
    public void initPlayer() {
        player = new Player(new Vec3(1, 1, 1));
    }

    @Test
    public void testPosition(){
        Vec3 pos = player.get3DPos();
        Vec3 diff = new Vec3(1,1,1);
        Assert.assertTrue(diff.equals(player.get3DPos()));
        player.setPosition(pos);
        Assert.assertTrue(pos.equals(player.get3DPos()));
    }

    @Test
    public void testForwardVelocity(){       // not done. all move velocity behaviour should be tested.
        MovableController movableController = new MovableController();
        movableController.setPlayer(player);
        movableController.KeyPressed(GLFW.GLFW_KEY_W, 0);
        Vec3 velocity = player.getForwardVector().mul(player.getVelocity().getLength());
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
        Assert.assertTrue(player.getVelocity().equals((player.getForwardVector().add(player.
                getRightVector().mul(-1))).normalize().mul(player.getVelocity().getLength())));
    }

    @Test
    public void testForwardAndBackwardSim(){
        MovableController movableController = new MovableController();
        movableController.setPlayer(player);
        movableController.KeyPressed(GLFW.GLFW_KEY_W, 0);
        movableController.KeyPressed(GLFW.GLFW_KEY_S, 0);
        Assert.assertTrue(player.getVelocity().equals((player.
                getForwardVector().mul(-1*player.getVelocity().getLength()))));
    }

    @Test
    public void testSprint(){
        MovableController movableController = new MovableController();
        movableController.setPlayer(player);
        movableController.KeyPressed(GLFW.GLFW_KEY_W, GLFW.GLFW_MOD_SHIFT);
        Assert.assertTrue(player.getVelocity().equals((player.getForwardVector().mul(player.getVelocity().getLength()))));
    }

    @Test
    public void testSprintForAndBack(){
        MovableController movableController = new MovableController();
        movableController.setPlayer(player);
        movableController.KeyPressed(GLFW.GLFW_KEY_W, GLFW.GLFW_MOD_SHIFT);
        movableController.KeyPressed(GLFW.GLFW_KEY_S, 0);
        Assert.assertTrue(player.getVelocity().equals
                (player.getForwardVector().mul(player.getVelocity().getLength()*-1)));
    }
    @Test
    public void testSprintSide(){
        MovableController movableController = new MovableController();
        movableController.setPlayer(player);
        movableController.KeyPressed(GLFW.GLFW_KEY_W, GLFW.GLFW_MOD_SHIFT);
        movableController.KeyPressed(GLFW.GLFW_KEY_A, 0);
        Assert.assertTrue(player.getVelocity().equals((player.getForwardVector().
                add(player.getRightVector().mul(-1)).normalize()).
                mul(player.getVelocity().getLength())));
    }

}
