package hills.engine.system.game;

import hills.model.World;
import hills.util.Math.Vec2;
import hills.util.Math.Vec3;
import hills.util.Math.Vertex;
import hills.engine.model.MeshTexture;
import hills.engine.model.Model;
import hills.engine.system.EngineSystem;

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
	World world;

	private GameSystem(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
		world = World.getInstance();
		//texture = new MeshTexture("test.png");
		
		//Mesh cubeMesh = ModelLoader.load(v, ind, texture, Mat4.identity());
		//cube = new model(new Mesh[]{cubeMesh});
	}

	@Override
	protected void update(double delta) {
		world.updateWorld(delta);
	}

	@Override
	public void render() {
	//	ModelRenderer.batch(ShaderProgram.STATIC, cube, Mat4.identity().scale(16.0f * 2, 16.0f * 2, 16.0f * 2).translate(CameraSystem.getInstance().getPosition().mul(new Vec3(1.0f, 0.0f, 1.0f))));
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
