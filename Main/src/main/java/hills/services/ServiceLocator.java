package hills.services;

import hills.services.camera.ICameraDataService;
import hills.services.camera.ICameraUpdateService;
import hills.services.camera.CameraService;
import hills.services.debug.DebugService;
import hills.services.display.DisplayService;
import hills.services.display.IDisplayService;
import hills.services.files.FileService;
import hills.services.files.IPictureFileService;
import hills.services.terrain.TerrainHeightService;
import hills.services.terrain.TerrainRenderDataService;
import hills.services.terrain.TerrainService;
import hills.services.terrain.TerrainTreeService;

public enum ServiceLocator {
	INSTANCE;
	
	private DebugService debugService;
	private CameraService cameraService;
	private TerrainService terrainService;
	private DisplayService displayService;
	private FileService fileService;
	
	private ServiceLocator(){
		debugService = new DebugService();
		cameraService = new CameraService();
		terrainService = new TerrainService();
		displayService = new DisplayService();
		fileService = new FileService();
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
	
	public ICameraUpdateService getCameraUpdateService(){
		return cameraService;
	}
	
	public ICameraDataService getCameraDataService(){
		return cameraService;
	}
	
	public IDisplayService getDisplayService(){
		return displayService;
	}

	public IPictureFileService getFileService() { return fileService; }
	
}
