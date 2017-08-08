package hills.controller.ModelInterfaceControllers;

import hills.controller.InputControllers.InputMediator;
import hills.controller.InputControllers.MouseButtonListener;
import hills.controller.InputControllers.MouseListener;
import hills.model.IShootable;
import hills.model.IShooter;
import hills.services.ServiceLocator;
import hills.services.camera.CameraData;
import hills.services.camera.ICameraDataService;
import hills.util.math.Vec3;
import hills.util.math.Vec4;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

public class ShootController implements MouseButtonListener{

    IShooter shooter;

    List<IShootable> shootables = new ArrayList<IShootable>();

    ICameraDataService cameraDataService;

    public ShootController(){

        InputMediator.INSTANCE.subscribeToMouseButton(this);

        cameraDataService = ServiceLocator.INSTANCE.getCameraDataService();

    }

    @Override
    public void mousePressed(int button, int mods) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_1){
            return;
        }
        System.out.println("IT'S A BANG" + shootables.size());

        Vec4 rayDir = new Vec4(0.0f, 0.0f, -1.0f, 0.0f);

        rayDir = cameraDataService.getCameraMatrix().getInverse().mul(rayDir);

        rayDir = cameraDataService.getPerspectiveMatrix().getInverse().mul(rayDir);

        rayDir = rayDir.normalize();

        Vec3 ray = new Vec3(rayDir.getX(), rayDir.getY(), -rayDir.getW());

        System.out.println(ray.toString());

        for (IShootable s : shootables){
            if (s.getInteractionSphere().intersects(shooter.getStartPos(), ray)){
                s.takeShotDamage(shooter.getShootDamage());
                System.out.println("You hit something!");
            }
        }
    }

    @Override
    public void mouseReleased(int button, int mods) {

    }

    public void setShooter(IShooter shooter){

        this.shooter = shooter;

    }

    public void addShootable(IShootable shootable){

        shootables.add(shootable);

    }

    public void removeShootable(IShootable shootable){

        shootables.remove(shootable);

    }



}
