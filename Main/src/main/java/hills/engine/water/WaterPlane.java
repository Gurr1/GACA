package hills.engine.water;

import lombok.Getter;
import hills.engine.math.Mat4;
import hills.engine.math.Vec2;
import hills.engine.math.Vec3;

public class WaterPlane {

	@Getter private final Vec3 position;
	@Getter private final Vec2 size;
	@Getter private final Vec3 normal;
	@Getter private final Mat4 modelMatrix;
	
	public WaterPlane(Vec3 position, Vec2 size, Vec3 normal){
		this.position = position;
		this.size = size;
		this.normal = normal;
		
		modelMatrix = null;
	}
}
