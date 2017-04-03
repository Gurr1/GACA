package hills.engine;

import hills.engine.display.Display;
import hills.engine.display.FrameBuffer;
import hills.engine.input.Keyboard;
import hills.engine.input.Mouse;
import hills.engine.loader.ModelLoader;
import hills.engine.loader.TextureLoader;
import hills.engine.renderer.ModelRenderer;
import hills.engine.renderer.SkyBoxRenderer;
import hills.engine.renderer.TerrainRenderer;
import hills.engine.renderer.shader.ShaderProgram;
import hills.engine.system.EngineSystem;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

public final class GameLoop {

	/**
	 * True if game loop is running
	 */
	public static boolean isRunning = false;

	/**
	 * Keeps track of all systems that need to be updated and rendered by the
	 * game loop.<br>
	 * OBS! Should not be modified! Is filled automatically when a new
	 * EngineSystem is created.
	 */
	private static List<EngineSystem> systems = new ArrayList<EngineSystem>();

	private GameLoop() {
	} // Private constructor = no instances

	/**
	 * Start game loop
	 */
	public static void start() {
		if (isRunning)
			return;

		isRunning = true;
		run(); // Run engine
	}

	/**
	 * Stop game loop
	 */
	public static void stop() {
		isRunning = false;
	}

	/**
	 * While isRunning is true <br>
	 * - Update systems <br>
	 * - Render systems <br>
	 * - Update input<br>
	 * - Update Display (If Display has been created)
	 */
	public static void run() {
		double lastCycleTime = GLFW.glfwGetTime(); // Time passed since last
													// frame;

		while (isRunning) {

			double cycleTime = GLFW.glfwGetTime();
			double delta = cycleTime - lastCycleTime;
			lastCycleTime = cycleTime;

			update(delta); // Update
			render(); // Render

			Keyboard.update(); // Update keyboard input
			Mouse.update(); // Update mouse input

			if (Display.hasBeenCreated())
				Display.update(); // Update display
		}

		cleanUp(); // Cleanup data
	}

	/**
	 * Update all systems
	 * 
	 * @param delta
	 *            - IRL time between calls in seconds
	 */
	public static void update(double delta) {
		for (EngineSystem system : systems)
			system.systemUpdate(delta);
	}

	/**
	 * Render everything
	 */
	public static void render() {
		for (EngineSystem system : systems)
			system.render(); // Update all systems rendering code

		FrameBuffer.clear(true, true, false); // Clear the screen

		// WaterRenderer.INSTANCE.render(); // Draw water planes
		TerrainRenderer.INSTANCE.render(); // Draw batched terrain nodes
		ModelRenderer.render(); // Draw all batched models
		SkyBoxRenderer.INSTANCE.render(); // Draw the Sky box

		TerrainRenderer.INSTANCE.clearBatch();
		ModelRenderer.clearBatch();
	}

	/**
	 * Add an engine system to be updated by the game loop.
	 */
	public static void addSystem(EngineSystem system) {
		systems.add(system);
	}

	/**
	 * Remove engine system from update list. cleanUp() of engine system will be
	 * called if removed successfully.
	 * 
	 * @param system
	 *            - Engine system to remove from game loop update list.
	 * @return True if system was found and removed.
	 */
	public static boolean removeSystem(EngineSystem system) {
		if (systems.remove(system)) {
			system.cleanUp();
			return true;
		}

		return false;
	}

	/**
	 * Clean up all data that needs to be cleaned up
	 */
	public static void cleanUp() {
		for (EngineSystem system : systems)
			system.cleanUp();

		ShaderProgram.cleanUp();
		TextureLoader.cleanUp();
		ModelLoader.cleanUp();
	}
}
