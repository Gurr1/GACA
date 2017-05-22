package hills.controller.manager;

import hills.controller.AbstractController;
import hills.controller.EntityFactory;
import hills.controller.ModelInterfaceControllers.AttackController;
import hills.controller.ModelInterfaceControllers.CollidableController;
import hills.controller.ModelInterfaceControllers.MovableController;
import hills.controller.ModelInterfaceControllers.RenderController;
import hills.model.*;
import hills.services.ServiceLocator;
import hills.services.terrain.TerrainServiceConstants;
import hills.util.math.Vec3;

import java.util.Random;

public final class GameManager extends AbstractController {
	
	private int nNPCs = 15;
	private int nImmovables = 20;
	private RenderController renderController;
	private CollidableController collidableController;
	private MovableController movableController;
	private AttackController attackController;
	private long nFrame = 0;
	private double runtime = 0;
	private int nCollectibles = 25;

	public GameManager(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
		movableController = new MovableController();
		collidableController = new CollidableController();
		attackController = new AttackController();
		renderController = new RenderController();
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
            Creature sheep = EntityFactory.createSheep(ServiceLocator.INSTANCE.getModelService().getSheep(), generateSpawnLocation());
			movableController.addAIMovable(sheep);
			collidableController.addCollidable(sheep);
			renderController.addRenderable(sheep);
		}
		for (int i = 0; i<nImmovables; i++){
			ImmovableObject tree = EntityFactory.createTree(ServiceLocator.INSTANCE.getModelService().getTree(), generateTreeSpawnLocation());
			renderController.addRenderable(tree);
			collidableController.addCollidable(tree);
		}
		for(int i = 0; i<nCollectibles; i++){
			CollectibleObject coin = EntityFactory.createCoin(ServiceLocator.INSTANCE.getModelService().getCoin(), generateSpawnLocation());		// Change model.
			renderController.addRenderable(coin);
			collidableController.addCollidable(coin);
		}
	}

	private Vec3 generateTreeSpawnLocation() {
    	Vec3 spawn;
    	do {
			spawn = generateSpawnLocation();
		}while (spawn.getY()>TerrainServiceConstants.WATER_HEIGHT + 10);
    	return spawn;


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
		renderController.updateRender();
		//.scale(16.0f * 2, 16.0f * 2, 16.0f * 2).translate(CameraSystem.getInstance().getPosition().mul(new Vec3(1.0f, 0.0f, 1.0f))));
	}

	@Override
	public void cleanUp() {
		System.out.println("GameSystem cleaned up!");
	}
}
