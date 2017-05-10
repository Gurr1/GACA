package hills.services;

import hills.services.camera.ICameraDataService;
import hills.services.camera.ICameraUpdateService;
import hills.services.camera.CameraService;
import hills.services.debug.DebugService;
import hills.services.display.DisplayService;
import hills.services.display.DisplayServiceI;
import hills.services.terrain.TerrainHeightService;
import hills.services.terrain.TerrainRenderDataService;
import hills.services.terrain.TerrainService;
import hills.services.terrain.TerrainTreeService;

public enum ServiceLocator {
	INSTANCE;
	
	private DisplayService displayService;
	private DebugService debugService;
	private CameraService cameraService;
	private TerrainService terrainService;
	
	private ServiceLocator(){	
	}
	
	public TerrainHeightService getTerrainHeightService(){
		return getTerrainServiceInstance();
	}
	
	public TerrainRenderDataService getTerrianRenderDataService(){
		return getTerrainServiceInstance();
	}
	
	public TerrainTreeService getTerrainTreeService(){
		return getTerrainServiceInstance();
	}
	
	public ICameraUpdateService getCameraUpdateService(){
		return getCameraServiceInstance();
	}
	
	public ICameraDataService getCameraDataService(){
		return getCameraServiceInstance();
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
