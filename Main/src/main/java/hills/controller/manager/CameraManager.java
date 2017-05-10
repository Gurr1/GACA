package hills.controller.manager;

import hills.controller.EngineSystem;
import hills.services.ServiceLocator;
import hills.services.camera.ICameraUpdateService;

public class CameraManager extends EngineSystem {

	ICameraUpdateService updateService;
	
	protected CameraManager(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
		
		ServiceLocator.INSTANCE.getCameraUpdateService();
	}

	@Override
	protected void update(double delta) {
		updateService.updateGPUCameraMatrix();
	}

	@Override
	public void render() {
		
	}

	@Override
	public void cleanUp() {
		System.out.println("Camera manager cleaned up!");
	}

}
