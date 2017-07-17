package hills.services;

/**
 * @author Anton Annlöv, Cornelis T Sjöbeck
 */

import hills.services.ModelDataService.IModelService;
import hills.services.ModelDataService.ModelFactory;
import hills.services.camera.CameraFactory;
import hills.services.camera.ICameraDataService;
import hills.services.camera.ICameraUpdateService;
import hills.services.collision.CollisionFactory;
import hills.services.collision.ICollisionDetection;
import hills.services.debug.DebugFactory;
import hills.services.debug.DebugService;
import hills.services.debug.IDebugService;
import hills.services.display.DisplayFactory;
import hills.services.display.DisplayService;
import hills.services.display.DisplayServiceI;
import hills.services.files.FileFactory;
import hills.services.files.FileService;
import hills.services.files.IPictureFileService;
import hills.services.generation.GenerationFactory;
import hills.services.generation.IDirectionGenerationService;
import hills.services.generation.ITerrainGenerationService;
import hills.services.terrain.*;


public enum ServiceLocator {
	INSTANCE;
	
	private DisplayService displayService;
	private DebugService debugService;
	private TerrainService terrainService;
	private FileService fileService;
	private ITerrainGenerationService generationService;
	private IModelService modelService;
	
	private ServiceLocator(){	
	}
	
	public ITerrainHeightService getTerrainHeightService(boolean test){
		return TerrainFactory.getTerrainHeightServiceInstance(test);
	}
	
	public ITerrainRenderDataService getTerrianRenderDataService(){
		return TerrainFactory.getTerrainRenderDataServiceInstance(false);
	}

	public ITerrainHeightService getTerrainHeightTestService(){
		return TerrainFactory.getTerrainHeightServiceInstance(true);
	}
	
	public ITerrainTreeService getTerrainTreeService(){
		return TerrainFactory.getTerrainTreeServiceInstance(false);
	}
	
	public ICameraUpdateService getCameraUpdateService(){
		return CameraFactory.getCameraUpdateServiceInstance();
	}

	public ICameraDataService getCameraDataService(){
		return CameraFactory.getCameraDataServiceInstance();
	}

	public ITerrainGenerationService getTerrainGenerationService(){
		return GenerationFactory.getTerrainGenerationService();
	}

	public ITerrainChunkService getTerrainChunkService(){
		return TerrainFactory.getTerrainChunkServiceInstance(false);
	}

	public IDirectionGenerationService getDirectionGenerationService(){
		return GenerationFactory.getDirectionGenerationService();
	}

	public DisplayServiceI getDisplayService(){
		return DisplayFactory.getDisplayServiceInstance();
	}


	public IModelService getModelService(){
		return ModelFactory.getModelServiceInstance();
	}

	public IPictureFileService getPictureFileService() { return FileFactory.getPictureFileServiceInstance(); }

	public ICollisionDetection getCollisionDetection() { return CollisionFactory.getCollisionDetectionInstance(); }

	public IDebugService getDebugService() { return DebugFactory.getDebugServiceInstance(); }
}
