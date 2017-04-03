package hills.engine.math.shape;

import hills.engine.math.Vec3;
import hills.engine.math.Vec4;
import lombok.Getter;

public class Plane {

	@Getter
	private final Vec3 point;
	@Getter
	private final Vec3 normal;

	/**
	 * Create a new plane.
	 * 
	 * @param point
	 *            - A point on the plane.
	 * @param normal
	 *            - The planes normal.
	 */
	public Plane(Vec3 point, Vec3 normal) {
		this.point = point;
		this.normal = normal;
	}

	/**
	 * Get the signed distance between a point and this plane.
	 * 
	 * @param point
	 *            - A point to get the signed distance to.
	 * @return The signed distance between point and this plane.
	 */
	public float getDistanceSigned(Vec3 point) {
		return normal.dot(point.sub(this.point));
	}

	/**
	 * Get the distance between a point and this plane.
	 * 
	 * @param point
	 *            - A point to get the distance to.
	 * @return The distance between point and this plane.
	 */
	public float getDistance(Vec3 point) {
		return Math.abs(getDistanceSigned(point));
	}

	/**
	 * Get a component of plane when described as ax + by + cz + d = 0.
	 */
	public float getA() {
		return normal.getX();
	}

	/**
	 * Get b component of plane when described as ax + by + cz + d = 0.
	 */
	public float getB() {
		return normal.getX();
	}

	/**
	 * Get c component of plane when described as ax + by + cz + d = 0.
	 */
	public float getC() {
		return normal.getX();
	}

	/**
	 * Get d component of plane when described as ax + by + cz + d = 0.
	 */
	public float getD() {
		return -normal.dot(point);
	}

	/**
	 * @return This plane described in equation form where Vec4.x * x + Vec4.y *
	 *         y + Vec4.z * z + Vec4.w = 0.
	 */
	public Vec4 inEquationForm() {
		return new Vec4(normal, getD());
	}
}
