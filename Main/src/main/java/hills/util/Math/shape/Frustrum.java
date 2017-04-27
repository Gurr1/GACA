package hills.util.Math.shape;

import hills.util.Math.Vec3;

public class Frustrum {
	
	/* TODO DELETE */
	
	public Vec3[] v = new Vec3[8];
	
	/*             */
	
	public static final int PLANES = 6;
	
	private final Plane[] planes;
	
	/**
	 * Create a new frustrum defined by 6 planes:<br>
	 * [0]: nearPlane<br>
	 * [1]: farPlane<br>
	 * [2]: leftPlane<br>
	 * [3]: topPlane<br>
	 * [4]: rightPlane<br>
	 * [5]: bottomPlane<br>
	 * @param planes - The planes of the frustrum.
	 */
	public Frustrum(Plane[] planes){
		if(planes.length != PLANES)
			throw new IllegalArgumentException("A frustrum has 6 planes! Array passed has: " + planes.length);
		
		this.planes = new Plane[PLANES];
		for(int i = 0; i < planes.length; i++)
			this.planes[i] = planes[i];
	}
	
	/**
	 * Create a new plane defined by 6 planes.
	 * @param nearPlane - The top of the truncated pyramid.
	 * @param farPlane - The bottom of the truncated pyramid.
	 * @param leftPlane - The left plane looking inwards from the near plane.
	 * @param topPlane - The top plane looking inwards from the near plane.
	 * @param rightPlane - The right plane looking inwards from the near plane.
	 * @param bottomPlane - The bottom plane looking inwards from the near plane.
	 */
	public Frustrum(Plane nearPlane, Plane farPlane, Plane leftPlane, Plane topPlane, Plane rightPlane, Plane bottomPlane){
		this(new Plane[]{nearPlane, farPlane, leftPlane, topPlane, rightPlane, bottomPlane});
	}
	
	/**
	 * Create a new frustrum defined by 6 planes, each created from<br>
	 * a point in the plane and a normal.
	 * @param nearPoint - Point in near plane.
	 * @param nearNormal
	 * @param farPoint
	 * @param farNormal
	 * @param leftPoint
	 * @param leftNormal
	 * @param topPoint
	 * @param topNormal
	 * @param rightPoint
	 * @param rightNormal
	 * @param bottomPoint
	 * @param bottomNormal
	 */
	public Frustrum(Vec3 nearPoint, Vec3 nearNormal, Vec3 farPoint,
			Vec3 farNormal, Vec3 leftPoint, Vec3 leftNormal, Vec3 topPoint,
			Vec3 topNormal, Vec3 rightPoint, Vec3 rightNormal,
			Vec3 bottomPoint, Vec3 bottomNormal) {
		this(new Plane[]{new Plane(nearPoint, nearNormal), new Plane(farPoint, farNormal),
						 new Plane(leftPoint, leftNormal), new Plane(topPoint, topNormal), 
						 new Plane(rightPoint, rightNormal), new Plane(bottomPoint, bottomNormal)});
	}
	
	/**
	 * Create a new frustrum describing the shape of a view.
	 * @param near - Distance to near plane.
	 * @param far - Distance to far plane.
	 * @param aspect - Aspect ratio of view window.
	 * @param FOV - Vertical field of view.
	 * @param pos - Position of frustrum in world coordinates.
	 * @param forward - Forward direction of frustrum.
	 * @param up - Up direction of frustrum.
	 * @param right - Right direction of frustrum.
	 * @param normalize - To normalize direction vectors or not. Should be true if direction vectors aren't already normalized.
	 */
	public Frustrum(float near, float far, float aspect, float FOV, Vec3 pos, Vec3 forward, Vec3 up, Vec3 right, boolean normalize){
		float halfHNear = (float) (Math.tan(Math.toRadians(FOV * 0.5f)) * near);
		float halfWNear = halfHNear * aspect;
		
		if(normalize){
			forward = forward.normalize();
			up = up.normalize();
			right= right.normalize();
		}
		
		// Useful vectors
		final Vec3 nearPlaneCenter = forward.mul(near);
		final Vec3 centerToRightNearPlaneVector = right.mul(halfWNear);
		final Vec3 centerToTopNearPlaneVector = up.mul(halfHNear);
		
		// Near plane
		final Plane nearPlane = new Plane(pos.add(nearPlaneCenter), forward);
		
		// Far plane
		final Plane farPlane = new Plane(pos.add(forward.mul(far)), forward.mul(-1));
		
		// Right plane
		final Vec3 rightPlaneParrallellVector = nearPlaneCenter.add(centerToRightNearPlaneVector).normalize();
		final Plane rightPlane = new Plane(pos, up.cross(rightPlaneParrallellVector));
			
		// Left plane
		final Vec3 leftPlaneParrallellVector = nearPlaneCenter.sub(centerToRightNearPlaneVector).normalize();
		final Plane leftPlane = new Plane(pos, leftPlaneParrallellVector.cross(up));
		
		// Top plane
		final Vec3 topPlaneParrallellVector = nearPlaneCenter.add(centerToTopNearPlaneVector).normalize();
		final Plane topPlane = new Plane(pos, topPlaneParrallellVector.cross(right));
		
		// Bottom plane
		final Vec3 bottomPlaneParrallellVector = nearPlaneCenter.sub(centerToTopNearPlaneVector).normalize();
		final Plane bottomPlane = new Plane(pos, right.cross(bottomPlaneParrallellVector));
		
		planes = new Plane[]{nearPlane, farPlane, leftPlane, topPlane, rightPlane, bottomPlane};
	
		/* TODO DELETE */
		
		v[0] = pos.add(nearPlaneCenter).sub(centerToRightNearPlaneVector).sub(centerToTopNearPlaneVector);
		v[1] = pos.add(nearPlaneCenter).add(centerToRightNearPlaneVector).sub(centerToTopNearPlaneVector);
		v[2] = pos.add(nearPlaneCenter).add(centerToRightNearPlaneVector).add(centerToTopNearPlaneVector);
		v[3] = pos.add(nearPlaneCenter).sub(centerToRightNearPlaneVector).add(centerToTopNearPlaneVector);
		
		float halfHFar = (float) (Math.tan(Math.toRadians(FOV * 0.5f)) * far);
		float halfWFar = halfHFar * aspect;
		
		final Vec3 farPlaneCenter = forward.mul(far);
		final Vec3 centerToRightFarPlaneVector = right.mul(halfWFar);
		final Vec3 centerToTopFarPlaneVector = up.mul(halfHFar);
		
		v[4] = pos.add(farPlaneCenter).sub(centerToRightFarPlaneVector).sub(centerToTopFarPlaneVector);
		v[5] = pos.add(farPlaneCenter).add(centerToRightFarPlaneVector).sub(centerToTopFarPlaneVector);
		v[6] = pos.add(farPlaneCenter).add(centerToRightFarPlaneVector).add(centerToTopFarPlaneVector);
		v[7] = pos.add(farPlaneCenter).sub(centerToRightFarPlaneVector).add(centerToTopFarPlaneVector);
		
		/*             */
	}
	
	/**
	 * Test if point is located inside of the frustrum.
	 * @param point - The point to test.
	 * @return CollisionState.INSIDE if the point is inside the frustrum,<br>
	 * otherwise CollisionState.OUTSIDE if the point is outside the frustrum.
	 */
	public CollisionState testPoint(Vec3 point){
		CollisionState state = CollisionState.INSIDE;
		
		// For every plane test the signed distance between the point and the plane.
		for(int i = 0; i < PLANES; i++)
			if(planes[i].getDistanceSigned(point) < 0)	// If the signed distance is negative.
				return CollisionState.OUTSIDE;			// Point outside of the frustrum since
														// all the frustrum's normals point inwards.
		return state;
	}
	
	/**
	 * Test if point is located inside of the frustrum.
	 * @param point - Point to test.
	 * @return True if point is inside of the frustrum.
	 */
	public boolean intersects(Vec3 point){
		boolean state = true;
		
		// For every plane test the signed distance between the point and the plane.
		for(int i = 0; i < planes.length; i++)
			if(planes[i].getDistanceSigned(point) < 0)	// If the signed distance is negative.
				return false;							// Point outside of the frustrum since
														// all the frustrum's normals point inwards.
		return state;
	}
	
	/**
	 * Test if sphere is inside, intersecting or outside of the frustrum.
	 * @param sphere - The sphere to test.
	 * @return CollisionState.INSIDE if the sphere is inside the frustrum,<br>
	 * CollisionState.INTERSECTS if the sphere is partly inside the frustrum,<br>
	 * CollisionState.OUTSIDE if the sphere is outside the frustrum.
	 */
	public CollisionState testSphere(Sphere sphere){
		CollisionState state = CollisionState.INSIDE;
		
		Vec3 pos = sphere.getPos();
		float radius = sphere.getRadius();
		
		for(int i = 0; i < planes.length; i++){
			float distance = planes[i].getDistanceSigned(pos);
			
			if(distance < -radius)
				return CollisionState.OUTSIDE;
			else if(distance < radius)
				state = CollisionState.INTERSECTS;
		}
		
		return state;
	}
	
	/**
	 * Test if sphere intersects the frustrum (completely or partly).
	 * @param sphere - Sphere to test.
	 * @return True if sphere is inside of the frustrum (completely or partly).
	 */
	public boolean intersects(Sphere sphere){
		boolean state = true;
		
		Vec3 pos = sphere.getPos();
		float radius = sphere.getRadius();
		
		for(int i = 0; i < planes.length; i++)
			if( planes[i].getDistanceSigned(pos) < -radius)
				return false;
		
		return state;
	}

	/**
	 * Test if axis aligned box is inside, intersecting or outside of the frustrum.
	 * @param aaBox - The axis aligned box to test.
	 * @return CollisionState.INSIDE if the axis aligned box is inside the frustrum,<br>
	 * CollisionState.INTERSECTS if the axis aligned box is partly inside the frustrum,<br>
	 * CollisionState.OUTSIDE if the axis aligned box is outside the frustrum.
	 */
	public CollisionState testAABox(AABox aaBox){
		CollisionState state = CollisionState.INSIDE;
		
		final Vec3 pos = aaBox.getPos();
		final Vec3 size = aaBox.getSize();
		
		final float minX = pos.getX();
		final float maxX = minX + size.getX();
		
		final float minY = pos.getY();
		final float maxY = minY + size.getY();
		
		final float minZ = pos.getZ();
		final float maxZ = minZ + size.getZ();
		
		for(int i = 0; i < planes.length; i++){
			final Plane plane = planes[i];
			final Vec3 planeNormal = plane.getNormal();
			
			// Find the "positive vertex"/"maximum vertex"
			// and find the "negative vertex"/"minimum vertex".
			// The "positive vertex" is the vertex of the aabox
			// that is the farthest away from the plane in the
			// direction of the normal, the "negative vertex" is
			// the opposite.
			float pVertexX = minX;
			float pVertexY = minY;
			float pVertexZ = minZ;
			
			float nVertexX = maxX;
			float nVertexY = maxY;
			float nVertexZ = maxZ;
			
			if(planeNormal.getX() >= 0){
				pVertexX = maxX;
				nVertexX = minX;
			}
			
			if(planeNormal.getY() >= 0){
				pVertexY = maxY;
				nVertexY = minY;
			}
			
			if(planeNormal.getZ() >= 0){
				pVertexZ = maxZ;
				nVertexZ = minZ;
			}
			
			final Vec3 pVertex = new Vec3(pVertexX, pVertexY, pVertexZ);
			final Vec3 nVertex = new Vec3(nVertexX, nVertexY, nVertexZ);
			
			// If positive vertex is outside of frustrum then entire box
			// is outside of the frustrum
			if(plane.getDistanceSigned(pVertex) < 0.0f)
				return CollisionState.OUTSIDE;
			// If negative vertex is outside of frustrum but the positive one
			// is inside then box is intersecting this plane.
			else if(plane.getDistanceSigned(nVertex) < 0.0f)
				state = CollisionState.INTERSECTS;
		}
		
		return state;
	}
	
	/**
	 * Test if axis aligned box intersects the frustrum (completely or partly).
	 * @param aaBox - The axis aligned box to test.
	 * @return True if axis aligned box is inside of the frustrum (completely or partly).
	 */
	public boolean intersects(AABox aaBox){
		boolean state = true;
		
		final Vec3 pos = aaBox.getPos();
		final Vec3 size = aaBox.getSize();
		
		final float minX = pos.getX();
		final float maxX = minX + size.getX();
		
		final float minY = pos.getY();
		final float maxY = minY + size.getY();
		
		final float minZ = pos.getZ();
		final float maxZ = minZ + size.getZ();
		
		for(int i = 0; i < planes.length; i++){
			final Plane plane = planes[i];
			final Vec3 planeNormal = plane.getNormal();
			
			// Find the "positive vertex"/"maximum vertex"
			// The "positive vertex" is the vertex of the aabox
			// that is the farthest away from the plane in the
			// direction of the normal.
			float pVertexX = minX;
			float pVertexY = minY;
			float pVertexZ = minZ;
			
			if(planeNormal.getX() >= 0)
				pVertexX = maxX;
			if(planeNormal.getY() >= 0)
				pVertexY = maxY;
			if(planeNormal.getZ() >= 0)
				pVertexZ = maxZ;
			
			// If positive vertex is outside of frustrum then entire box
			// is outside of the frustrum
			if(plane.getDistanceSigned(new Vec3(pVertexX, pVertexY, pVertexZ)) < 0.0f)
				return false;
		}
		
		return state;
	}
}
