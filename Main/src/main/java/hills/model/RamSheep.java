package hills.model;

import hills.controller.ServiceMediator;
import hills.util.math.Vec2;
import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;
import hills.util.model.Model;

import java.util.Random;

/**
 * Created by corne on 5/6/2017.
 */
public class RamSheep extends Mob {

    float radius;
    Random rand = new Random();
    ServiceMediator serviceMediator;
    private float move = 0;
    private final int damagePoints = 10;
    protected Model model;

    public RamSheep(Vec3 pos){
        this.pos = pos;
        this.healthPoints = 30;
        this.speed = 2;
        this.maxHealth = 30;
        this.radius = 1;
        serviceMediator = ServiceMediator.INSTANCE;
    }

    @Override
    void dealDamage() {
        //TODO
    }

    @Override
    public void addVelocity(Vec2 deltaVelocity) {
        //TODO
    }

    @Override
    public void addVelocity(Vec3 deltaVelocity) {
        //TODO
    }

    @Override
    public void setPitch(float pitch) {
        //TODO
    }

    @Override
    public void setYaw(float yaw) {
        //TODO
    }

    @Override
    public void updatePitch(float deltaPitch) {
        //TODO
    }

    @Override
    public void updateYaw(float deltaYaw) {
        //TODO
    }

    @Override
    public void setCurrentUpdate(float delta) {
        //TODO
    }

    @Override
    public Sphere getBoundingSphere() {
        return null;
        //TODO
    }

    public void moveRandomly() {
        //TODO
    }

    //TODO
}
