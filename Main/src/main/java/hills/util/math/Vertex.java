package hills.util.math;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author Anton Annlöv
 */
//TODO Make immutable?
public class Vertex {

	@Getter @Setter private Vec3 position;
	@Getter @Setter private Vec2 textureCoordinate;
	@Getter @Setter private Vec3 normal;
	@Getter @Setter private Vec4 tangent;
	
	/**
	 * Create a new vertex.
	 * @param pos - Position coordinates of vertex.
	 * @param tex - Texture coordinates of vertex.
	 * @param nor - Normal vector of vertex.
	 * @param tan - Tangent vector of vertex.
	 */
	public Vertex(final Vec3 pos, final Vec2 tex, final Vec3 nor, final Vec4 tan){
		position = pos;
		textureCoordinate = tex;
		normal = nor;
		tangent = tan;
	}
	
	/**
	 * Create a new vertex.With tangent vectors all zero vectors.
	 * @param pos - Position coordinates of vertex.
	 * @param tex - Texture coordinates of vertex.
	 * @param nor - Normal vector of vertex.
	 */
	public Vertex(final Vec3 pos, final Vec2 tex, final Vec3 nor){
		this(pos, tex, nor, new Vec4(0.0f, 0.0f, 0.0f, 0.0f));
	}
	
	/**
	 * Create a new vertex. With normal, tangent vectors all zero vectors.
	 * @param pos - Position coordinates of vertex.
	 * @param tex - Texture coordinates of vertex.
	 */
	public Vertex(final Vec3 pos, final Vec2 tex){
		this(pos, tex, new Vec3(0.0f, 0.0f, 0.0f));
	}
	
	/**
	 * Create a new vertex. With texture, normal, tangent vectors all zero vectors.
	 * @param pos - Position coordinates of vertex.
	 */
	public Vertex(final Vec3 pos){
		this(pos, new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 0.0f));
	}
	/**
	 * Create a new vertex. With position, texture, normal, tangent vectors all zero vectors.
	 */
	public Vertex(){
		this(new Vec3(0.0f, 0.0f, 0.0f), new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 0.0f));
	}
}
