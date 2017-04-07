package hills.engine.system.domainModel;

/**
 * Created by Anders on 2017-04-03.
 */
public interface IWoundable {

    /**
     * Lowers the objects health with the amount parameter
     * @param amount the amount to be lowered
     */
    void dealDamage(int amount);

    /**
     * returns the current amount of healthpoints of the woundable object has
     */
    int getHealth();

    /**
     * returns the max amount of healthpoints of the woundable object can have
     */
    int getMaxHealth();

}
