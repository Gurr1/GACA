package hills.util.Math.shape;

import hills.util.Math.Vec3;
import lombok.Getter;

public class Sphere {

	@Getter private final Vec3 pos;
	@Getter private final float radius;
	
	/**
	 * Create a new sphere.
	 * @param pos - The spheres position.
	 * @param radius - The spheres radius.
	 */
	public Sphere(Vec3 pos, float radius) {
		super();
		this.pos = pos;
		this.radius = radius;
	}
	
	public boolean intersects(Plane plane){
		float distance = plane.getDistance(pos);
		if(distance < radius)
			return true;
		
		return false;
	}

	public boolean intersects(Sphere sphere){
		Vec3 v =  pos.sub(sphere.getPos());
		float distance = v.dot(v);
		float collisionDistance = (radius + sphere.getRadius())*(radius + sphere.getRadius());
		return (distance < collisionDistance);
	}
}
