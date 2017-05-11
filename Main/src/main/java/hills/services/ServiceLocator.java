package hills.services;

import hills.services.camera.ICameraDataService;
import hills.services.camera.ICameraUpdateService;
import hills.services.camera.CameraService;
import hills.services.debug.DebugService;
import hills.services.display.DisplayService;
import hills.services.display.DisplayServiceI;
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

	public IGenerationMediator getGenerationMediator(){
		return null;
	}
	
	public DisplayServiceI getDisplayService(){
		return getDisplayServiceInstance();
	}
	
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



}
