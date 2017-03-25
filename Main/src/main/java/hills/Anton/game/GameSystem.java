package hills.Anton.game;

import hills.Anton.engine.loader.ModelLoader;
import hills.Anton.engine.math.Mat4;
import hills.Anton.engine.math.Vec2;
import hills.Anton.engine.math.Vec3;
import hills.Anton.engine.math.Vertex;
import hills.Anton.engine.model.Mesh;
import hills.Anton.engine.model.MeshTexture;
import hills.Anton.engine.model.Model;
import hills.Anton.engine.renderer.Renderer;
import hills.Anton.engine.renderer.shader.ShaderProgram;
import hills.Anton.engine.system.EngineSystem;

public final class GameSystem extends EngineSystem {

	/** Singleton instance **/
	private static GameSystem instance = null;
	
	Vertex[] v = new Vertex[]{
			new Vertex(new Vec3(-0.5f, -0.5f, -1.0f), new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(new Vec3(0.5f, -0.5f, -1.0f), new Vec2(1.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(new Vec3(0.0f, 0.5f, -1.0f), new Vec2(0.5f, 1.0f), new Vec3(0.0f, 0.0f, 1.0f))
	};
	
	int[] ind = new int[]{
			0, 1, 2
	};
	
	Model model;
	
	private GameSystem(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
		
		Mesh mesh = ModelLoader.load(v, ind, new MeshTexture("test.png"), Mat4.identity());
		model = new Model(new Mesh[]{mesh});
	}

	@Override
	protected void update(double delta) {
		
	}

	@Override
	public void render() {
		Renderer.batch(ShaderProgram.STATIC, model, Mat4.identity());
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
