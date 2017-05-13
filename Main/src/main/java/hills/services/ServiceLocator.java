package hills.services;

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
	private GenerationMediator generationService;
	
	private ServiceLocator(){	
	}
	
	public ITerrainHeightService getTerrainHeightService(){
		return getTerrainServiceInstance();
	}
	
	public ITerrainRenderDataService getTerrianRenderDataService(){
		return getTerrainServiceInstance();
	}
	
	public ITerrainTreeService getTerrainTreeService(){
		return getTerrainServiceInstance();
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

	public IPictureFileService getFileService() { return getFileServiceInstance(); }
	
	private TerrainService getTerrainServiceInstance(){
		if(terrainService == null)
			terrainService = new TerrainService();
		
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



}
