package hills.controller.manager;

import hills.controller.AbstractController;
import hills.controller.EntityFactory;
import hills.controller.ModelInterfaceControllers.AttackController;
import hills.controller.ModelInterfaceControllers.CollidableController;
import hills.controller.ModelInterfaceControllers.MovableController;
import hills.controller.ModelInterfaceControllers.RenderController;
import hills.model.*;
import hills.services.ModelDataService.ModelFactory;
import hills.services.ServiceLocator;
import hills.services.generation.ObjectPlacer;
import hills.services.terrain.Chunk;
import hills.services.terrain.ITerrainChunkService;
import hills.services.terrain.TerrainServiceConstants;
import hills.util.math.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author Anton Annlöv, Gustav Engsmyre
 */
public final class GameManager extends AbstractController {
	
	private int nNPCs = 15;
	private int nImmovables = 20;
	private RenderController renderController;
	private CollidableController collidableController;
	private MovableController movableController;
	private AttackController attackController;
	private ITerrainChunkService chunkService;
	private long nFrame = 0;
	private double runtime = 0;
	private int nCollectibles = 25;

	public GameManager(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
		movableController = new MovableController();
		collidableController = new CollidableController();
		attackController = new AttackController();
		renderController = new RenderController();
		chunkService = ServiceLocator.INSTANCE.getTerrainChunkService();
		loadGame();
	}

	private void loadGame() {
		loadEntities();
		System.out.println("loaded entities");
		loadStaticObjects();
	}


    private void loadStaticObjects() {
		ObjectPlacer ob = new ObjectPlacer();
		List<Vec3> v;
		v = ob.placeObjects();
		for(Vec3 vec3 : v){
			ImmovableObject t = EntityFactory.createTree(vec3);
			chunkService.addObject(t);
		}
		ob = new ObjectPlacer();
		ob.setOptimalHeight(0.5);
		v = ob.placeObjects();
		for(Vec3 vec3 : v){
			ImmovableObject t = new Rock(vec3, ModelFactory.getModelServiceInstance().getTree());
			chunkService.addObject(t);
		}

    }

    Player p;
    private void loadEntities() {
		p = EntityFactory.createPlayer(
                generateSpawnLocation());
		movableController.setPlayer(p);
		collidableController.addCollidable(p);
		attackController.setPlayer(p);
		for(int i = 0; i < nNPCs; i++){
            Creature sheep = EntityFactory.createSheep(generateSpawnLocation());
			movableController.addAIMovable(sheep);
			collidableController.addCollidable(sheep);
			renderController.addRenderable(sheep);

			Creature goat = EntityFactory.createGoat(generateSpawnLocation());
			movableController.addAIMovable(goat);
			collidableController.addCollidable(goat);
			renderController.addRenderable(goat);
		}

		for(int i = 0; i<nCollectibles; i++){
			CollectibleObject collectible = EntityFactory.createAnyCollectible(generateSpawnLocation());		// Change model.
			renderController.addRenderable(collectible);
			collidableController.addCollidable(collectible);
			movableController.addAIMovable(collectible);
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
			y = ServiceLocator.INSTANCE.getTerrainHeightService(false).getHeight(x,z);
		}while(y < TerrainServiceConstants.WATER_HEIGHT);
		return new Vec3(x, y, z);
	}

	@Override
	protected void update(double delta) {
		runtime += delta;
		collidableController.update(chunkService.getChunk(movableController.getPlayer().get3DPos()).getStaticObjects());
		movableController.updateMovables((float) delta, runtime, true);


		if(collidableController.isRemoved()){
			ICollidable collidable = collidableController.getObjectToRemove();
			renderController.removeObject(collidable);
		}
	}

	@Override
	public void render() {
		List<IRenderable> renderables = new ArrayList<>();
		List<Chunk> chunks = chunkService.getChunks(movableController.getPlayer().getHeadPos());
		for (Chunk c: chunks) {
			renderables.addAll(c.getStaticObjects());
		}
		renderController.updateRender(renderables);
		//.scale(16.0f * 2, 16.0f * 2, 16.0f * 2).translate(CameraSystem.getInstance().getPosition().mul(new Vec3(1.0f, 0.0f, 1.0f))));
	}

	@Override
	public void cleanUp() {
		System.out.println("GameSystem cleaned up!");
	}
}
