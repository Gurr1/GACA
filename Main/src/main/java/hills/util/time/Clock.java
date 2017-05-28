package hills.util.time;

/**
 * @Author Anton Annlöv
 */
public class Clock {

	public static final double TICKS_PER_SECOND = 1000000000.0; // 1 tick per
																// nanosecond

	private long ticks = 0; // Amount of ticks passed since last getDelta() call
	private long mark = 0; // Mark in order to mark a certain time
	private float scale = 1.0f; // Clock speed scale relative to real life
								// speed. Default 1.0f
	private boolean isPaused = false; // If clock is paused

	public Clock(float scale) {
		this(scale, false, 0.0f);
	}

	public Clock(float scale, boolean isPaused) {
		this(scale, isPaused, 0.0f);
	}

	public Clock(float scale, boolean isPaused, float startTime) {
		this.scale = scale;
		this.isPaused = isPaused;

		ticks = secondsToTicks(startTime);
	}

	/**
	 * Should be called once every game loop cycle with "real time" passed in
	 * seconds.<br>
	 * <br>
	 * Updates clock ticks
	 */
	public void update(double delta) {
		if (isPaused)
			return;

		ticks += secondsToTicks(delta * scale);
	}

	/**
	 * Move clock forward 1 time step according to targetFPS and scale.
	 * 
	 * @param targetFPS
	 *            - Time step reference.
	 */
	public void step(float targetFPS) {
		if (!isPaused)
			return;

		ticks += secondsToTicks((1.0f / targetFPS) * scale);
	}

	/**
	 * Mark current time.
	 */
	public void mark() {
		mark = ticks;
	}

	/**
	 * Pauses or starts clock.
	 * 
	 * @param isPaused
	 *            - If clock should be paused or not.
	 */
	public void pause(boolean isPaused) {
		this.isPaused = isPaused;
	}

	/**
	 * Convert seconds to amount of ticks.
	 * 
	 * @param seconds
	 *            - Amount of seconds to be converted.
	 * @return Amount of ticks corresponding to seconds parameter.
	 */
	private long secondsToTicks(double seconds) {
		return (long) (seconds * TICKS_PER_SECOND);
	}

	/**
	 * Convert ticks to amount of seconds.
	 * 
	 * @param ticks
	 *            - Amount of ticks to be converted.
	 * @return Amount of seconds corresponding to ticks parameter.
	 */
	private double ticksToSeconds(long ticks) {
		return (double) ticks / TICKS_PER_SECOND;
	}

	/**
	 * @return Time passed since last mark in seconds. Can be negative if
	 *         setTime(float) has been used!
	 */
	public double getDelta() {
		return ticksToSeconds(ticks - mark);
	}

	/**
	 * Equivalent to calling getDelta() and then mark().
	 * 
	 * @return Time passed since last mark in seconds. Can be negative if
	 *         setTime(float) has been used!
	 */
	public double getDeltaAndMark() {
		double delta = getDelta();
		mark();
		return delta;
	}

	/**
	 * Sets clocks time.
	 * 
	 * @param time
	 *            - Time to set clock to.
	 */
	public void setTime(float time) {
		ticks = secondsToTicks(time);
	}

	/**
	 * Get this clocks time scale.
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Set time scale of clock relative to "real time".
	 * 
	 * @param scale
	 *            - New time scale of clock.
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

}
