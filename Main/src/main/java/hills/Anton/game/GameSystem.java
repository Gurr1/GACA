package hills.Anton.game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import hills.Anton.engine.loader.ModelLoader;
import hills.Anton.engine.math.Mat4;
import hills.Anton.engine.math.Vec2;
import hills.Anton.engine.math.Vec3;
import hills.Anton.engine.math.Vertex;
import hills.Anton.engine.model.Mesh;
import hills.Anton.engine.model.MeshTexture;
import hills.Anton.engine.model.Model;
import hills.Anton.engine.renderer.Renderer;
import hills.Anton.engine.renderer.shader.ShaderProgram;
import hills.Anton.engine.system.EngineSystem;

public final class GameSystem extends EngineSystem {

	/** Singleton instance **/
	private static GameSystem instance = null;
	
	Vertex[] v = new Vertex[]{
			new Vertex(new Vec3(-0.5f, -0.5f, -1.0f), new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(new Vec3(0.5f, -0.5f, -1.0f), new Vec2(1.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
			new Vertex(new Vec3(0.0f, 0.5f, -1.0f), new Vec2(0.5f, 1.0f), new Vec3(0.0f, 0.0f, 1.0f))
	};
	
	int[] ind = new int[]{
			0, 1, 2
	};
	
	Model model;
	
	private GameSystem(float scale, boolean isPaused, float startTime) {
		super(scale, isPaused, startTime);
		
		/*
		BufferedImage terrainMap = null;
		try {
			terrainMap = ImageIO.read(this.getClass().getResource("/height_map_test_1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int w = terrainMap.getWidth();
		int h = terrainMap.getHeight();
		
		Vertex[] grid = new Vertex[w * h];
		int[] gridI = new int[(w - 1) * (h - 1) * 6];
		
		for(int row = 0; row < h; row++){
			for(int col = 0; col < w; col++){
				float gridPointHeight = ((terrainMap.getRGB(row, col) >> 8) & 0xFF) / 255.0f * 100.0f;
				grid[col + row * h] = new Vertex(new Vec3(col, gridPointHeight, row), new Vec2(col % 2 == 0 ? 0.0f : 1.0f, row % 2 == 0 ? 1.0f : 0.0f), new Vec3(col, gridPointHeight, row));
			
				if(row < h - 1 && col < w - 1){
					int verIndex = (col + row * (h - 1));
					gridI[verIndex * 6 + 0] = verIndex;
					gridI[verIndex * 6 + 1] = verIndex + w + 1;
					gridI[verIndex * 6 + 2] = verIndex + 1;
					gridI[verIndex * 6 + 3] = verIndex;
					gridI[verIndex * 6 + 4] = verIndex + w;
					gridI[verIndex * 6 + 5] = verIndex + w + 1;
				}
			}
		}		
		
		Mesh mesh = ModelLoader.load(grid, gridI, new MeshTexture("test.png"), Mat4.identity());
		model = new Model(new Mesh[]{mesh});
		*/
	}

	@Override
	protected void update(double delta) {
		
	}

	@Override
	public void render() {
		//Renderer.batch(ShaderProgram.STATIC, model, Mat4.identity());
	}

	@Override
	public void cleanUp() {
		System.out.println("GameSystem cleaned up!");
	}

	/**
	 * Creates the singleton instance of GameSystem.
	 * @return False if an instance has already been created.
	 */
	public static boolean createInstance(float scale, boolean isPaused, float startTime) {
		if(instance != null)
			return false;
		
		instance = new GameSystem(scale, isPaused, startTime);
		return true;
	}
	
	/**
	 * @return The singleton instance of GameSystem.
	 * @throws NullPointerException If singleton instance has not been created.
	 */
	public static GameSystem getInstance() throws NullPointerException {
		if(instance == null)
			throw new NullPointerException("Singleton instance not created!");
		
		return instance;
	}
}
