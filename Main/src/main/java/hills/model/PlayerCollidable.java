package hills.model;

/**
 *
 */
public interface PlayerCollidable extends ICollidable {

    void collectCollectible(ICollectible collectible);

    void takeDamage(int damage);
}
