package hills.controller.manager;

import hills.controller.EngineSystem;

public class CameraManager extends EngineSystem {

	protected CameraManager(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
	}

	@Override
	protected void update(double delta) {
		
	}

	@Override
	public void render() {
		
	}

	@Override
	public void cleanUp() {
		System.out.println("Camera manager cleaned up!");
	}

}
