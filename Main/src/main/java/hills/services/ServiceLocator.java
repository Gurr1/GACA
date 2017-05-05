package hills.services;

import hills.services.camera.CameraDataService;
import hills.services.camera.CameraService;
import hills.services.debug.DebugService;
import hills.services.terrain.TerrainHeightService;
import hills.services.terrain.TerrainRenderDataService;
import hills.services.terrain.TerrainService;
import hills.services.terrain.TerrainTreeService;

public enum ServiceLocator {
	INSTANCE;
	
	private DebugService debugService;
	private CameraService cameraService;
	private TerrainService terrainService;
	
	private ServiceLocator(){
		debugService = new DebugService();
		cameraService = new CameraService();
		terrainService = new TerrainService();
	}
	
	public TerrainHeightService getTerrainHeightService(){
		return terrainService;
	}
	
	public TerrainRenderDataService getTerrianRenderDataService(){
		return terrainService;
	}
	
	public TerrainTreeService getTerrainTreeService(){
		return terrainService;
	}
	
	public CameraDataService getCameraDataService(){
		return cameraService;
	}
	
	
}
