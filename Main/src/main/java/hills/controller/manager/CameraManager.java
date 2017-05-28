package hills.controller.manager;

import hills.controller.AbstractController;
import hills.services.ServiceLocator;
import hills.services.camera.ICameraUpdateService;

/**
 * @author Anton
 */
public class CameraManager extends AbstractController {

	ICameraUpdateService updateService;
	
	public CameraManager(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
		
		updateService = ServiceLocator.INSTANCE.getCameraUpdateService();
		updateService.updateGPUPerspectiveMatrix();
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
