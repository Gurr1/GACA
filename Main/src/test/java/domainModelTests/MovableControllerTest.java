package domainModelTests;

import hills.controller.ModelInterfaceControllers.MovableController;
import hills.model.IMovable;
import hills.model.Player;
import hills.model.Sheep;
import hills.util.math.Vec2;
import hills.util.math.Vec3;
import hills.util.model.Mesh;
import hills.util.model.Model;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Anders on 2017-05-25.
 */
public class MovableControllerTest {
    MovableController movableController;
    IMovable sheep;
    @Before
    public void init(){
        movableController = new MovableController();
        sheep = new Sheep(new Vec3(12,654,3), new Model(new Mesh[1]));

        movableController.addAIMovable(sheep);
        movableController.setPlayer(new Player(new Vec3(1,1,1)));

        for (int i = 0; i < 4; i++) {
            movableController.updateMovables(0.5f,i*0.5, true);
        }
    }

    @Test
    public void testMoving(){
        Vec2 prev;
        Vec2 neew;

        for (int i = 0; i < 10000; i++) {
            prev = sheep.get2DPos();
            movableController.updateMovables(0.5f,((float)i)*0.5f, true);
            neew = sheep.get2DPos();
            testPos(prev , neew);
        }
    }

    private void testPos(Vec2 prev, Vec2 neew) {
        Vec2 diff = neew.sub(prev);
        Assert.assertNotSame(prev,neew);
        Assert.assertEquals(0.5*sheep.getVelocity().getLength(),diff.getLength(),0.1);
    }

}
