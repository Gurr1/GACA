package hills.engine.math.shape;

import hills.engine.math.Vec3;
import lombok.Getter;

public class AABox {

	@Getter private final Vec3 pos;
	@Getter private final Vec3 size;
	
	public AABox(Vec3 pos, Vec3 size) {
		this.pos = pos;
		this.size = size;
	}
	
	public AABox(float x, float y, float z, float width, float height, float depth) {
		this(new Vec3(x, y, z), new Vec3(width, height, depth));
	}
	
	/**
	 * Test if point is located inside of the axis aligned box.
	 * @param point - The point to test.
	 * @return CollisionState.INSIDE if the point is inside the axis aligned box,<br>
	 * otherwise CollisionState.OUTSIDE if the point is outside the axis aligned box.
	 */
	public CollisionState testPoint(Vec3 point){
		Vec3 v = point.sub(pos);
		Vec3 u = point.sub(pos.add(size));
		
		if(v.getX() >= 0.0f && u.getX() <= 0.0f &&
		   v.getY() >= 0.0f && u.getY() <= 0.0f &&
		   v.getZ() >= 0.0f && u.getZ() <= 0.0f)
			return CollisionState.INSIDE;
		
		return CollisionState.OUTSIDE;
	}
	
	/**
	 * Test if point is located inside of the axis aligned box.
	 * @param point - Point to test.
	 * @return True if point is inside of the axis aligned box.
	 */
	public boolean intersects(Vec3 point){
		Vec3 v = point.sub(pos);
		Vec3 u = point.sub(pos.add(size));
		
		if(v.getX() >= 0.0f && u.getX() <= 0.0f &&
		   v.getY() >= 0.0f && u.getY() <= 0.0f &&
		   v.getZ() >= 0.0f && u.getZ() <= 0.0f)
			return true;
		
		return false;
	}
	
	/**
	 * Test if sphere is inside, intersecting or outside of the axis aligned box.
	 * @param sphere - The sphere to test.
	 * @return CollisionState.INSIDE if the sphere is inside the axis aligned box,<br>
	 * CollisionState.INTERSECTS if the sphere is partly inside the axis aligned box,<br>
	 * CollisionState.OUTSIDE if the sphere is outside the axis aligned box.
	 */
	public CollisionState testSphere(Sphere sphere){
		System.out.println("AABox intersects Sphere test not implemented yet! Will return outside.");
		return CollisionState.OUTSIDE;
	}
	
	/**
	 * Test if sphere intersects the axis aligned box (completely or partly).
	 * @param sphere - Sphere to test.
	 * @return True if sphere is inside of the axis aligned box (completely or partly).
	 */
	public boolean intersects(Sphere sphere){
		float minX = pos.getX();
		float maxX = minX + size.getX();
		
		float minY = pos.getY();
		float maxY = minY + size.getY();
		
		float minZ = pos.getZ();
		float maxZ = minZ + size.getZ();
		
		float posX = sphere.getPos().getX();
		float posY = sphere.getPos().getY();
		float posZ = sphere.getPos().getZ();
		
		float radius = sphere.getRadius();
		float radiusSquared = radius * radius;
		
		if(posX < minX)
			radiusSquared -= (posX - minX) * (posX - minX);
		else if(posX > maxX)
			radiusSquared -= (posX - maxX) * (posX - maxX);
		
		if(posY < minY)
			radiusSquared -= (posY - minY) * (posY - minY);
		else if(posY > maxY)
			radiusSquared -= (posY - maxY) * (posY - maxY);
		
		if(posZ < minZ)
			radiusSquared -= (posZ - minZ) * (posZ - minZ);
		else if(posZ > maxZ)
			radiusSquared -= (posZ - maxZ) * (posZ - maxZ);
		
		return radiusSquared > 0;
	}

	/**
	 * Test if axis aligned box is inside, intersecting or outside of this axis aligned box.
	 * @param aaBox - The axis aligned box to test.
	 * @return CollisionState.INSIDE if the axis aligned box is inside this axis aligned box,<br>
	 * CollisionState.INTERSECTS if the axis aligned box is partly inside this axis aligned box,<br>
	 * CollisionState.OUTSIDE if the axis aligned box is outside this axis aligned box.
	 */
	public CollisionState testAABox(AABox aaBox){
		CollisionState state = CollisionState.INTERSECTS;
		
		Vec3 min = pos;
		Vec3 max = min.add(size);
		
		Vec3 testMin = aaBox.getPos();
		Vec3 testMax = testMin.add(aaBox.getSize());
		
		if(!(min.getX() <= testMax.getX() && max.getX() >= testMin.getX() && 
		   min.getY() <= testMax.getY() && max.getX() >= testMin.getX() &&
		   min.getZ() <= testMax.getZ() && max.getX() >= testMin.getX()))
			return CollisionState.OUTSIDE;
		
		Vec3 minToTestMin = testMin.sub(min);
		Vec3 maxToTestMax = testMax.sub(max);
		
		if(minToTestMin.getX() >= 0.0f && maxToTestMax.getX() >= 0.0f &&
		   minToTestMin.getY() >= 0.0f && maxToTestMax.getY() >= 0.0f &&
		   minToTestMin.getZ() >= 0.0f && maxToTestMax.getZ() >= 0.0f)
			return CollisionState.INSIDE;
		
		return state;
	}
	
	/**
	 * Test if an axis aligned box intersects this axis aligned box (completely or partly).
	 * @param aaBOx - The axis aligned box to test.
	 * @return True if the axis aligned box is inside of this axis aligned box (completely or partly).
	 */
	public boolean intersects(AABox aaBox){
		Vec3 min = pos;
		Vec3 max = min.add(size);
		
		Vec3 testMin = aaBox.getPos();
		Vec3 testMax = testMin.add(aaBox.getSize());
		
		if(min.getX() <= testMax.getX() && max.getX() >= testMin.getX() && 
		   min.getY() <= testMax.getY() && max.getX() >= testMin.getX() &&
		   min.getZ() <= testMax.getZ() && max.getX() >= testMin.getX())
			return true;
		
		return false;
	}
	
}
