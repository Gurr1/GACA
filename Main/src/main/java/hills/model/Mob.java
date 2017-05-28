package hills.model;

/**
 *
 */
public abstract class Mob extends Creature{
    /**
     * {@inheritDoc}
     */
    protected int damagePoints;

    abstract void dealDamage(int health);
    }
