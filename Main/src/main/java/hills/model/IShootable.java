package hills.model;

import hills.util.math.shape.Sphere;

public interface IShootable {
    
    public Sphere getInteractionSphere();

    public void takeShotDamage(int shotDamage);
}
