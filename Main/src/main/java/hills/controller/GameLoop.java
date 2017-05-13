package hills.controller;

import hills.services.ServiceLocator;
import hills.util.display.FrameBuffer;
import hills.util.loader.ModelLoader;
import hills.util.loader.TextureLoader;
import hills.util.shader.ShaderProgram;
import hills.view.ModelRenderer;
import hills.view.SkyBoxRenderer;
import hills.view.TerrainRenderer;
import hills.view.WaterRenderer;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public final class GameLoop {
	
	/**
	 * True if game loop is running
	 */
	public boolean isRunning = false;
	
	/**
	 * Keeps track of all systems that need to be updated and rendered by the
	 * game loop.<br>
	 * OBS! Should not be modified! Is filled automatically when a new
	 * EngineSystem is created.
	 */
	private List<AbstractController> systems = new ArrayList<AbstractController>();
	public GameLoop(){} // Private constructor = no instances
	
	/**
	 * Start game loop
	 */
	public void start() {
		if(isRunning)
			return;
		isRunning = true;
		run(); // Run engine
	}
	
	/**
	 * Stop game loop
	 */
	public void stop() {
		isRunning = false;
	}
	
	/**
	 * While isRunning is true <br>
	 * 	- Update systems <br>
	 * 	- Render systems <br>
	 *  - Update input<br>
	 *  - Update Display (If Display has been created)
	 */
	public void run() {
		double lastCycleTime = GLFW.glfwGetTime(); // Time passed since last frame;
		
		while(isRunning){
			
			double cycleTime = GLFW.glfwGetTime();
			double delta = cycleTime - lastCycleTime;
			lastCycleTime = cycleTime;
			
			update(delta); 					// Update
			render();      					// Render
			if(ServiceLocator.INSTANCE.getDisplayService().hasBeenCreated())
				ServiceLocator.INSTANCE.getDisplayService().update();     		// Update display

		}
		
		cleanUp(); // Cleanup data
	}
	
	/**
	 * Update all systems
	 * 
	 * @param delta - IRL time between calls in seconds
	 */
	public void update(double delta){
		for(AbstractController system: systems)

			system.systemUpdate(delta);
	}
	
	/**
	 * Render everything
	 */
	public void render() {
		for (AbstractController system : systems)
			system.render(); // Update all systems rendering code

		FrameBuffer.clear(true, true, false); // Clear the screen

		WaterRenderer.INSTANCE.render(); // Draw water planes
		TerrainRenderer.INSTANCE.render(); // Draw batched terrain nodes
		ModelRenderer.render(); // Draw all batched models
		SkyBoxRenderer.INSTANCE.render(); // Draw the Sky box

		TerrainRenderer.INSTANCE.clearBatch();
		ModelRenderer.clearBatch();
	}

	/**
	 * Add an engine system to be updated by the game loop.
	 */
	public void addSystem(AbstractController system){
		systems.add(system);
	}
	
	/**
	 * Remove engine system from update list. cleanUp() of engine system will be called if removed successfully.
	 * @param system - Engine system to remove from game loop update list.
	 * @return True if system was found and removed.
	 */
	public boolean removeSystem(AbstractController system){
		if(systems.remove(system)){
			system.cleanUp();
			return true;
		}
		
		return false;
	}
	
	/**
	 * Clean up all data that needs to be cleaned up
	 */
	public void cleanUp(){
		for(AbstractController system: systems)
			system.cleanUp();
		
		ShaderProgram.cleanUp();
		TextureLoader.cleanUp();
		ModelLoader.cleanUp();
	}
}
