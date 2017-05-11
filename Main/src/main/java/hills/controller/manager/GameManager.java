package hills.controller.manager;

import hills.controller.EngineSystem;
import hills.controller.EntityFactory;
import hills.controller.ModelInterfaceControllers.AttackController;
import hills.controller.ModelInterfaceControllers.CollidableController;
import hills.controller.ModelInterfaceControllers.MovableController;
import hills.controller.ServiceMediator;
import hills.model.Creature;
import hills.model.Player;
import hills.util.math.Vec2;
import hills.util.math.Vec3;
import hills.util.math.Vertex;
import hills.util.model.MeshTexture;
import hills.util.model.Model;

public final class GameManager extends EngineSystem {
	
	private int nNPCs = 10;
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
	private CollidableController collidableController;
	private MovableController movableController;
	private AttackController attackController;
	private long nFrame = 0;
	private double runtime = 0;

	public GameManager(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
		movableController = new MovableController();
		collidableController = new CollidableController();
		loadGame();
		attackController = new AttackController();
		//texture = new MeshTexture("test.png");
		
		//Mesh cubeMesh = ModelLoader.load(v, ind, texture, Mat4.identity());
		//cube = new model(new Mesh[]{cubeMesh});
	}

	private void loadGame() {
		loadEntities();
		System.out.println("loaded entities");
		loadStaticObjects();
	}

    private void loadStaticObjects() {
        // Load rocks, trees etc.
    }

    private void loadEntities() {
		System.out.println("loading player");
		Player p = EntityFactory.createPlayer(
                ServiceMediator.INSTANCE.generateSpawnLocation());
		System.out.println("player created");
		movableController.setPlayer(p);
		collidableController.addCollidable(p);
		attackController.setPlayer(p);
		for(int i = 0; i < nNPCs; i++){
            Creature sheep = EntityFactory.createSheep(ServiceMediator.INSTANCE.generateSpawnLocation());
			movableController.addAIMovable(sheep);
			collidableController.addCollidable(sheep);
		}
	}

	@Override
	protected void update(double delta) {
		runtime += delta;
		movableController.updateMovables((float) delta, runtime);
		collidableController.update();
	}

	@Override
	public void render() {
	//	ModelRenderer.batch(ShaderProgram.STATIC, cube, Mat4.identity().scale(16.0f * 2, 16.0f * 2, 16.0f * 2).translate(CameraSystem.getInstance().getPosition().mul(new Vec3(1.0f, 0.0f, 1.0f))));
	}

	@Override
	public void cleanUp() {
		System.out.println("GameSystem cleaned up!");
	}
}
