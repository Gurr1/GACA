package hills.model;

import hills.util.Math.Vec2;
import hills.util.Math.Vec3;
import hills.util.Math.shape.Sphere;
import hills.engine.model.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 2017-04-03.
 */
public abstract class Creature implements IWoundable,IMovable, ICollidable{

    /**
     * {@inheritDoc}
     */

    protected Vec3 pos;
    @Setter @Getter protected float speed;
    protected int healthPoints;
    protected int maxHealth;
    protected Model model;
    protected Vec3 velocity;
    private List<OnCreatureMoveListener> listeners = new ArrayList<>();

    @Override
    public abstract Sphere getBoundingSphere();

    public void updatePosition(Vec3 diff) {
        this.pos = this.pos.add(diff);
    }

    @Override
    public void setPosition(Vec3 pos) {
        this.pos = pos;
    }

    @Override
    public Vec2 get2DPos() {
        return new Vec2(pos.getX(), pos.getY());
    }

    @Override
    public Vec3 get3DPos() {
        return pos;
    }

    @Override
    public int getHealth(){return healthPoints;}

    @Override
    public int getMaxHealth(){return maxHealth;}

    @Override
    public void dealDamage(int amount){
        healthPoints -= amount;
    }

    public Vec3 getVelocity(){
        return velocity;
    }

    protected float getHeight(Creature creature){          // This is a Horrible implementation, should be changed.
        return listeners.get(0).getCreaturePosition(creature);
    }

    public void addListener(OnCreatureMoveListener listener){
        listeners.add(listener);
    }
    public abstract void moveRandomly();

}
