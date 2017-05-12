package hills.controller;

import hills.util.time.Clock;

public abstract class AbstractController {
	
	/**
	 * Clock used by system. This clock may run faster or slower than "real time".
	 */
	private Clock systemClock;
	
	/**
	 * Initializes the systems clock and adds this system to the game loop for updating.
	 * @param scale - Clock speed scale relative to "real time".
	 * @param isPaused - Is clock paused.
	 * @param startTime - Clock start time.
	 */
	protected AbstractController(float scale, boolean isPaused, float startTime){
		systemClock = new Clock(scale, isPaused, startTime); // Initializes this systems clock
	}
	
	/**
	 * Called by game loop! <br>
	 * Updates system. Calls child update(float) with system clock delta.
	 * @param delta "real time" passed between calls.
	 */
	public final void systemUpdate(double delta){
		systemClock.update(delta);
		update(systemClock.getDeltaAndMark());
	}
	
	/**
	 * Called from EngineSystem with this systems clock delta time.<br>
	 * Used to update system every game loop.
	 * @param delta - Time passed since last call according to this systems clock
	 */
	protected abstract void update(double delta);
	
	/**
	 * Called by game loop!<br>
	 * Used to render system every game loop after all systems have been updated.
	 */
	public abstract void render();
	
	/**
	 * Called by game loop!<br>
	 * Used to clean up everything this system needs cleaned up once game loop is done.
	 */
	public abstract void cleanUp();
	
	public final float getSystemClockScale(){
		return systemClock.getScale();
	}
	
	/**
	 * Set time scale of system clock.
	 * @param scale - New time scale of clock.
	 */
	public final void setSystemClockScale(float scale){
		systemClock.setScale(scale);
	}
	
	/**
	 * Pauses or starts clock.
	 * @param isPaused - If clock should be paused or not.
	 */
	public final void pauseSystemClock(boolean isPaused){
		systemClock.pause(isPaused);
	}
}
