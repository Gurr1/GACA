package hills.model;

/**
 * Created by Anders on 2017-04-03.
 */
public abstract class Mob extends Creature{
    /**
     * {@inheritDoc}
     */
    protected int damagePoints;

    abstract void dealDamage();
    }
