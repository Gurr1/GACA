package hills.services;

import hills.services.ModelDataService.CubeModel;
import hills.services.ModelDataService.IModelService;
import hills.services.camera.ICameraDataService;
import hills.services.camera.ICameraUpdateService;
import hills.services.camera.CameraService;
import hills.services.debug.DebugService;
import hills.services.display.DisplayService;
import hills.services.display.DisplayServiceI;
import hills.services.files.FileService;
import hills.services.files.IPictureFileService;
import hills.services.generation.GenerationMediator;
import hills.services.generation.IGenerationMediator;
import hills.services.loader.LoaderFactory;
import hills.services.terrain.ITerrainHeightService;
import hills.services.terrain.ITerrainRenderDataService;
import hills.services.terrain.TerrainService;
import hills.services.terrain.ITerrainTreeService;

public enum ServiceLocator {
	INSTANCE;
	
	private DisplayService displayService;
	private DebugService debugService;
	private CameraService cameraService;
	private TerrainService terrainService;
	private FileService fileService;
	private IGenerationMediator generationService;
	private IModelService modelService;
	private LoaderFactory loaderFactory;
	
	private ServiceLocator(){	
	}
	
	public ITerrainHeightService getTerrainHeightService(){
		return getTerrainServiceInstance(false);
	}
	
	public ITerrainRenderDataService getTerrianRenderDataService(){
		return getTerrainServiceInstance(false);
	}

	public ITerrainHeightService getTerrainHeightTestService(){
		return getTerrainServiceInstance(true);
	}
	
	public ITerrainTreeService getTerrainTreeService(){
		return getTerrainServiceInstance(false);
	}
	
	public ICameraUpdateService getCameraUpdateService(){
		return getCameraServiceInstance();
	}

	public ICameraDataService getCameraDataService(){
		return getCameraServiceInstance();
	}

	public IGenerationMediator getGenerationService(){
		return getGemerationServiceInstance();
	}

	private IGenerationMediator getGemerationServiceInstance() {
		if(generationService == null){
			generationService = new GenerationMediator();
		}
		return generationService;
	}

	public DisplayServiceI getDisplayService(){
		return getDisplayServiceInstance();
	}

	public IModelService getModelService(){
		return getModelServiceInstance();
	}

	private IModelService getModelServiceInstance() {
		if(modelService == null){
			modelService = new CubeModel();
		}
		return modelService;
	}

	public IPictureFileService getFileService() { return getFileServiceInstance(); }
	
	private TerrainService getTerrainServiceInstance(boolean b){
		if(terrainService == null)
			terrainService = new TerrainService(b);
		
		return terrainService;
	}
	
	private DisplayService getDisplayServiceInstance(){
		if(displayService == null)
			displayService = new DisplayService();
		
		return displayService;
	}

	private CameraService getCameraServiceInstance(){
		if(cameraService == null)
			cameraService = new CameraService();

		return cameraService;
	}

	private DebugService getDebugServiceInstance(){
		if(debugService == null)
			debugService = new DebugService();

		return debugService;
	}

	private FileService getFileServiceInstance(){
		if(fileService == null)
			fileService = new FileService();

		return fileService;
	}

	public LoaderFactory getLoaderFactory(){return getLoaderFactoryInstance();}

	private LoaderFactory getLoaderFactoryInstance(){
		if(loaderFactory == null)
			loaderFactory = new LoaderFactory();
		return loaderFactory;
	}



}
