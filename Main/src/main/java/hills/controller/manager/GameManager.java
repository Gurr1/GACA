package hills.controller.manager;

import hills.controller.AbstractController;
import hills.controller.EntityFactory;
import hills.controller.ModelInterfaceControllers.AttackController;
import hills.controller.ModelInterfaceControllers.CollidableController;
import hills.controller.ModelInterfaceControllers.MovableController;
import hills.model.Creature;
import hills.model.Player;
import hills.services.ServiceLocator;
import hills.services.terrain.TerrainServiceConstants;
import hills.util.math.Vec3;
import hills.util.model.Model;

import java.util.Random;

public final class GameManager extends AbstractController {
	
	private int nNPCs = 10;
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
		attackController = new AttackController();
		loadGame();
	}

	private void loadGame() {
		loadEntities();
		System.out.println("loaded entities");
		loadStaticObjects();
	}

    private void loadStaticObjects() {
        // Load rocks, trees etc.
    }

    Player p;
    private void loadEntities() {
		p = EntityFactory.createPlayer(
                generateSpawnLocation());
		movableController.setPlayer(p);
		collidableController.addCollidable(p);
		attackController.setPlayer(p);
		for(int i = 0; i < nNPCs; i++){
            Creature sheep = EntityFactory.createSheep(ServiceLocator.INSTANCE.getModelService().getCube(), generateSpawnLocation());
			movableController.addAIMovable(sheep);
			collidableController.addCollidable(sheep);
		}
	}

	private Vec3 generateSpawnLocation(){
		Random random = new Random();
		int x;
		int z;
		float y;
		do {
			x = random.nextInt(TerrainServiceConstants.TERRAIN_WIDTH-1);
			z = random.nextInt(TerrainServiceConstants.TERRAIN_HEIGHT-1);
			y = ServiceLocator.INSTANCE.getTerrainHeightService().getHeight(x,z);
		}while(y < TerrainServiceConstants.WATER_HEIGHT);
		return new Vec3(x, y, z);
	}

	@Override
	protected void update(double delta) {
		runtime += delta;
		movableController.updateMovables((float) delta, runtime);
		collidableController.update();
	}

	@Override
	public void render() {
//		RenderLocator.INSTANCE.getModelBatchable().batch(ShaderProgram.STATIC, cube, Mat4.identity().translate(p.get3DPos().add(new Vec3(0.0f, 1.8f, -5.0f))));//.scale(16.0f * 2, 16.0f * 2, 16.0f * 2).translate(CameraSystem.getInstance().getPosition().mul(new Vec3(1.0f, 0.0f, 1.0f))));
	}

	@Override
	public void cleanUp() {
		System.out.println("GameSystem cleaned up!");
	}
}
