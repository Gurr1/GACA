package hills.engine.water;

import hills.util.Math.Mat4;
import hills.util.Math.STD140Formatable;
import hills.util.Math.Vec2;
import hills.util.Math.Vec3;
import hills.util.Math.Vertex;
import hills.util.Math.shape.Plane;

import java.nio.ByteBuffer;

import lombok.Getter;

public class WaterPlane implements STD140Formatable {

	@Getter
	private final Plane plane;
	@Getter
	private final Vec2 size;
	@Getter
	private final Mat4 modelMatrix;

	public WaterPlane(Vec3 position, Vec2 size, Vec3 normal) {
		this.plane = new Plane(position, normal);
		this.size = size;

		// Rodrigues rotation formula
		Vec3 up = new Vec3(0.0f, 1.0f, 0.0f);
		Vec3 planeNormal = plane.getNormal();
		Vec3 rotationVec = up.cross(planeNormal);

		float cos = up.dot(planeNormal);
		float sin = rotationVec.getLength();

		float x = rotationVec.getX();
		float y = rotationVec.getY();
		float z = rotationVec.getZ();

		float[] crossMatrixValues = { 0.0f, z, -y, 0.0f, // First column
				-z, 0.0f, x, 0.0f, // Second column
				y, -x, 0.0f, 0.0f, // Third column
				0.0f, 0.0f, 0.0f, 1.0f // Fourth column
		};

		Mat4 crossMatrix = new Mat4(crossMatrixValues);

		// Create a model matrix for the water plane translation * rotation *
		// scale
		Mat4 translationMatrix = Mat4.translationMatrix(plane.getPoint());
		Mat4 rotationMatrix = Mat4.identity().add(crossMatrix.mul(sin))
				.add(crossMatrix.mul(crossMatrix).mul(1.0f - cos));
		Mat4 scaleMatrix = Mat4.scaleMatrix(size.getX(), 1.0f, size.getY());
		modelMatrix = translationMatrix.mul(rotationMatrix.mul(scaleMatrix));
	}

	@Override
	public void get140Data(ByteBuffer buffer) {
		modelMatrix.get140Data(buffer);
	}

	@Override
	public int get140DataSize() {
		return modelMatrix.get140DataSize();
	}

	public static final Vertex[] WATER_PLANE_VERTICES = {
			new Vertex(new Vec3(-0.5f, 0.0f, 0.5f), new Vec2(0.0f, 0.0f),
					new Vec3(0.0f, 1.0f, 0.0f)),
			new Vertex(new Vec3(0.5f, 0.0f, 0.5f), new Vec2(1.0f, 0.0f),
					new Vec3(0.0f, 1.0f, 0.0f)),
			new Vertex(new Vec3(0.5f, 0.0f, -0.5f), new Vec2(1.0f, 1.0f),
					new Vec3(0.0f, 1.0f, 0.0f)),
			new Vertex(new Vec3(-0.5f, 0.0f, -0.5f), new Vec2(0.0f, 1.0f),
					new Vec3(0.0f, 1.0f, 0.0f)) };

	public static final int[] WATER_PLANE_INDICES = { 0, 1, 2, 0, 2, 3 };
}
