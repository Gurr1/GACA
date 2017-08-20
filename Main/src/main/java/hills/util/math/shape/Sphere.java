package hills.util.math.shape;

import hills.util.math.Vec3;
import lombok.Getter;

/**
 * @Author Anton Annlöv
 */
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
		Vec3 v = pos.sub(sphere.getPos());
		float distance = v.getLength();
		float collisionDistance = (radius + sphere.getRadius());
		return (distance < collisionDistance);
	}

	public boolean intersects(Vec3 pos, Vec3 ray){
		System.out.println("pos: "+pos +" "+ "ray: "+ray);
		System.out.println("spherepos: "+this.pos +" "+ this.radius);
		float b = ray.dot(this.pos.sub(pos));
		float c = this.pos.sub(pos).getLengthSqr() - (radius*radius);

		if (b * b - c >= 0){
			return
					true;
		}
		return
				false;
	}
}
