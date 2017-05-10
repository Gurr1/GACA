package hills.services;

import hills.services.camera.CameraIService;
import hills.services.camera.ICameraDataService;
import hills.services.camera.ICameraUpdateService;
import hills.services.debug.DebugIService;
import hills.services.display.DisplayIService;
import hills.services.display.IDisplayService;
import hills.services.files.FileService;
import hills.services.files.IPictureFileService;
import hills.services.terrain.ITerrainRenderDataService;
import hills.services.terrain.TerrainIService;
import hills.services.terrain.ITerrainHeightService;
import hills.services.terrain.ITerrainTreeService;

public enum ServiceLocator {
	INSTANCE;
	
	private DebugIService debugService;
	private CameraIService cameraService;
	private TerrainIService terrainService;
	private DisplayIService displayService;
	private FileService fileService;
	
	private ServiceLocator(){
		debugService = new DebugIService();
		cameraService = new CameraIService();
		terrainService = new TerrainIService();
		displayService = new DisplayIService();
		fileService = new FileService();
	}
	
	public ITerrainHeightService getTerrainHeightService(){
		return terrainService;
	}
	
	public ITerrainRenderDataService getTerrianRenderDataService(){
		return terrainService;
	}
	
	public ITerrainTreeService getTerrainTreeService(){
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
