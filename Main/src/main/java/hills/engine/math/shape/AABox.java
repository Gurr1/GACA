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
	
	
	
}
