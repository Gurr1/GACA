package hills.model;

/**
 *
 */
public class Gun extends Weapon{
    private double baseDamage = 10;
    private double spread = 1.03;       // How much the "bullet can deviate from straight on the screen. "
    @Override
    protected void dealDamage(IWoundable target) {

    }
}
