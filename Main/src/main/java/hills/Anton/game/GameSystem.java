package hills.Anton.game;

import hills.Anton.engine.display.Display;
import hills.Anton.engine.loader.ModelLoader;
import hills.Anton.engine.math.Mat4;
import hills.Anton.engine.math.Vec2;
import hills.Anton.engine.math.Vec3;
import hills.Anton.engine.math.Vertex;
import hills.Anton.engine.math.shape.AABox;
import hills.Anton.engine.math.shape.Frustrum;
import hills.Anton.engine.math.shape.Plane;
import hills.Anton.engine.model.Mesh;
import hills.Anton.engine.model.MeshTexture;
import hills.Anton.engine.model.Model;
import hills.Anton.engine.renderer.Renderer;
import hills.Anton.engine.renderer.shader.ShaderProgram;
import hills.Anton.engine.system.EngineSystem;
import hills.Anton.engine.system.camera.CameraSystem;

public final class GameSystem extends EngineSystem {

	/** Singleton instance **/
	private static GameSystem instance = null;
	
	Vertex[] v = {
			new Vertex(new Vec3(-.5f, -.5f, .5f), new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(new Vec3(-.5f, -.5f, .5f), new Vec2(0.0f, 1.0f), new Vec3(0.0f, -1.0f, 0.0f)),
			new Vertex(new Vec3(-.5f, -.5f, .5f), new Vec2(1.0f, 0.0f), new Vec3(-1.0f, 0.0f, 0.0f)),
			
			new Vertex(new Vec3(.5f, -.5f, .5f), new Vec2(1.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(new Vec3(.5f, -.5f, .5f), new Vec2(1.0f, 1.0f), new Vec3(0.0f, -1.0f, 0.0f)),
			new Vertex(new Vec3(.5f, -.5f, .5f), new Vec2(0.0f, 0.0f), new Vec3(1.0f, 0.0f, 0.0f)),
			
			new Vertex(new Vec3(.5f, .5f, .5f), new Vec2(1.0f, 1.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(new Vec3(.5f, .5f, .5f), new Vec2(1.0f, 0.0f), new Vec3(0.0f, 1.0f, 0.0f)),
			new Vertex(new Vec3(.5f, .5f, .5f), new Vec2(0.0f, 1.0f), new Vec3(1.0f, 0.0f, 0.0f)),
			
			new Vertex(new Vec3(-.5f, .5f, .5f), new Vec2(0.0f, 1.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(new Vec3(-.5f, .5f, .5f), new Vec2(0.0f, 0.0f), new Vec3(0.0f, 1.0f, 0.0f)),
			new Vertex(new Vec3(-.5f, .5f, .5f), new Vec2(1.0f, 1.0f), new Vec3(-1.0f, 0.0f, 0.0f)),
			
			new Vertex(new Vec3(-.5f, -.5f, -.5f), new Vec2(1.0f, 0.0f), new Vec3(0.0f, 0.0f, -1.0f)),
			new Vertex(new Vec3(-.5f, -.5f, -.5f), new Vec2(0.0f, 0.0f), new Vec3(0.0f, -1.0f, 0.0f)),
			new Vertex(new Vec3(-.5f, -.5f, -.5f), new Vec2(0.0f, 0.0f), new Vec3(-1.0f, 0.0f, 0.0f)),
			
			new Vertex(new Vec3(.5f, -.5f, -.5f), new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, -1.0f)),
			new Vertex(new Vec3(.5f, -.5f, -.5f), new Vec2(1.0f, 0.0f), new Vec3(0.0f, -1.0f, 0.0f)),
			new Vertex(new Vec3(.5f, -.5f, -.5f), new Vec2(1.0f, 0.0f), new Vec3(1.0f, 0.0f, 0.0f)),
			
			new Vertex(new Vec3(.5f, .5f, -.5f), new Vec2(0.0f, 1.0f), new Vec3(0.0f, 0.0f, -1.0f)),
			new Vertex(new Vec3(.5f, .5f, -.5f), new Vec2(1.0f, 1.0f), new Vec3(0.0f, 1.0f, 0.0f)),
			new Vertex(new Vec3(.5f, .5f, -.5f), new Vec2(1.0f, 1.0f), new Vec3(1.0f, 0.0f, 0.0f)),
			
			new Vertex(new Vec3(-.5f, .5f, -.5f), new Vec2(1.0f, 1.0f), new Vec3(0.0f, 0.0f, -1.0f)),
			new Vertex(new Vec3(-.5f, .5f, -.5f), new Vec2(0.0f, 1.0f), new Vec3(0.0f, 1.0f, 0.0f)),
			new Vertex(new Vec3(-.5f, .5f, -.5f), new Vec2(0.0f, 1.0f), new Vec3(-1.0f, 0.0f, 0.0f)),
	};
	
	int[] ind = {
			0, 3, 6,
			0, 6, 9,
			
			15, 12, 21,
			15, 21, 18,
			
			14, 2, 11,
			14, 11, 23,
			
			5, 17, 20,
			5, 20, 8,
			
			10, 7, 19,
			10, 19, 22,
			
			13, 16, 4,
			13, 4, 1
	};
	
	MeshTexture texture;
	Model model, cube;
	Vec3 pos;
	
	private GameSystem(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);

		texture = new MeshTexture("test.png");
		
		pos = new Vec3(300.0f, 2.0f, 150.0f);
		Vec3 forward = new Vec3(1.0f, 0.0f, 1.0f);
		Vec3 up = new Vec3(0.0f, 1.0f, 0.0f);
		Vec3 right = forward.cross(up);
		
		Frustrum f = new Frustrum(0.1f, 3000.0f, (float) Display.getWidth() / (float) Display.getHeight(), 70.0f, pos, forward, up, right, true);
		
		Frustrum f2 = new Frustrum(0.1f, 10.0f, (float) Display.getWidth() / (float) Display.getHeight(), 70.0f, pos, forward, up, right, true);
		
		Vec3[] fruVerPos = f2.v; //f.v;
		
		Vertex[] fruV = {
			new Vertex(fruVerPos[0], new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(fruVerPos[1], new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(fruVerPos[2], new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(fruVerPos[3], new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(fruVerPos[4], new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(fruVerPos[5], new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(fruVerPos[6], new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(fruVerPos[7], new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
		};
		
		int[] fruI = {
			// Near
			0, 1, 2,
			0, 2, 3,
			
			// Far
			4, 5, 6,
			4, 6, 7,
			
			// Left
			0, 4, 7,
			0, 7, 3,
			
			// Right
			1, 5, 6,
			1, 6, 2,
			
			// Top
			3, 2, 6,
			3, 6, 7,
			
			// Bottom
			0, 1, 5,
			0, 5, 4
		};
		
		Mesh mesh = ModelLoader.load(fruV, fruI, texture, Mat4.identity());
		model = new Model(new Mesh[]{mesh});
		
		Mesh cubeMesh = ModelLoader.load(v, ind, texture, Mat4.identity());
		cube = new Model(new Mesh[]{cubeMesh});
		
		System.out.println(f2.intersects(pos.add(new Vec3(1.0f, 0.0f, 1.0f))));//new AABox(pos.add(new Vec3(3.0f, 0.0f, 10.0f)), new Vec3(3f, 3f, 3f))));
	}

	@Override
	protected void update(double delta) {
		
	}

	@Override
	public void render() {
		Renderer.batch(ShaderProgram.STATIC, model, Mat4.identity());
		Renderer.batch(ShaderProgram.STATIC, cube, Mat4.identity().scale(0.01f, 0.01f, 0.01f).translate(pos.add(new Vec3(1.0f, 0.0f, 1.0f))));
		
		//Renderer.batch(ShaderProgram.STATIC, cube, Mat4.identity().scale(16.0f * 2.0f, 16.0f * 2.0f, 16.0f * 2.0f).translate(pos));
		//Renderer.batch(ShaderProgram.STATIC, cube, Mat4.identity().scale(32.0f * 2.0f, 32.0f * 2.0f, 32.0f * 2.0f).translate(pos));
		//Renderer.batch(ShaderProgram.STATIC, cube, Mat4.identity().scale(64.0f * 2.0f, 64.0f * 2.0f, 64.0f * 2.0f).translate(pos));
		//Renderer.batch(ShaderProgram.STATIC, cube, Mat4.identity().scale(128.0f * 2.0f, 128.0f * 2.0f, 128.0f * 2.0f).translate(pos));
		//Renderer.batch(ShaderProgram.STATIC, cube, Mat4.identity().scale(256.0f * 2.0f, 256.0f * 2.0f, 256.0f * 2.0f).translate(pos));
		//Renderer.batch(ShaderProgram.STATIC, cube, Mat4.identity().scale(512.0f * 2.0f, 512.0f * 2.0f, 512.0f * 2.0f).translate(pos));
		//Renderer.batch(ShaderProgram.STATIC, cube, Mat4.identity().scale(1024.0f * 2.0f, 1024.0f * 2.0f, 1024.0f * 2.0f).translate(pos));
		//Renderer.batch(ShaderProgram.STATIC, cube, Mat4.identity().scale(2048.0f * 2.0f, 2048.0f * 2.0f, 2048.0f * 2.0f).translate(pos));
		//Renderer.batch(ShaderProgram.STATIC, cube, Mat4.identity().scale(4096.0f * 2.0f, 4096.0f * 2.0f, 4096.0f * 2.0f).translate(pos));
		//Renderer.batch(ShaderProgram.STATIC, cube, Mat4.identity().scale(8192.0f * 2.0f, 8192.0f * 2.0f, 8192.0f * 2.0f).translate(pos));
	}

	@Override
	public void cleanUp() {
		System.out.println("GameSystem cleaned up!");
	}

	/**
	 * Creates the singleton instance of GameSystem.
	 * @return False if an instance has already been created.
	 */
	public static boolean createInstance(float scale, boolean isPaused, float startTime) {
		if(instance != null)
			return false;
		
		instance = new GameSystem(scale, isPaused, startTime);
		return true;
	}
	
	/**
	 * @return The singleton instance of GameSystem.
	 * @throws NullPointerException If singleton instance has not been created.
	 */
	public static GameSystem getInstance() throws NullPointerException {
		if(instance == null)
			throw new NullPointerException("Singleton instance not created!");
		
		return instance;
	}
}
