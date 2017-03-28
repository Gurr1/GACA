package hills.Anton.engine.math.shape;

import hills.Anton.engine.math.Vec3;
import lombok.Getter;

public class Plane {
	
	@Getter private final Vec3 point;
	@Getter private final Vec3 normal;
	
	/**
	 * Create a new plane.
	 * @param point - A point on the plane.
	 * @param normal - The planes normal.
	 */
	public Plane(Vec3 point, Vec3 normal){
		this.point = point;
		this.normal = normal;
	}
	
	/**
	 * Get the signed distance between a point and this plane.
	 * @param point - A point to get the signed distance to.
	 * @return The signed distance between point and this plane.
	 */
	public float getDistanceSigned(Vec3 point){
		return normal.dot(point.sub(this.point));
	}
	
	/**
	 * Get the distance between a point and this plane.
	 * @param point - A point to get the distance to.
	 * @return The distance between point and this plane.
	 */
	public float getDistance(Vec3 point){
		return Math.abs(getDistanceSigned(point));
	}
	
}
