package hills.view.renderer;

import hills.util.display.FrameBuffer;
import hills.util.loader.ModelLoader;
import hills.util.math.Mat4;
import hills.util.math.Vec4;
import hills.util.model.MeshData;
import hills.util.shader.SamplerUniform;
import hills.util.shader.ShaderAttribute;
import hills.util.shader.ShaderProgram;
import hills.util.texturemap.TextureMap2D;
import hills.engine.water.WaterPlane;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

public enum WaterRenderer {
	INSTANCE();

	private final ShaderProgram shaderProgram = ShaderProgram.WATER;
	private final MeshData meshData;

	private final FrameBuffer refractionFrameBuffer;
	private final FrameBuffer reflectionFrameBuffer;

	private List<WaterPlane> waterPlanes = new ArrayList<WaterPlane>();

	private final int REFRACTION_FB_WIDTH = 300;
	private final int REFRACTION_FB_HEIGHT = 300;
	private final int REFLECTION_FB_WIDTH = 300;
	private final int REFLECTION_FB_HEIGHT = 300;

	private WaterRenderer() {
		meshData = ModelLoader.load(WaterPlane.WATER_PLANE_VERTICES,
				WaterPlane.WATER_PLANE_INDICES, null, Mat4.identity())
				.getMeshData();

		refractionFrameBuffer = new FrameBuffer(REFRACTION_FB_WIDTH, REFRACTION_FB_HEIGHT);
		refractionFrameBuffer.attachTexture(GL11.GL_TEXTURE_2D, GL11.GL_RGBA, GL11.GL_RGBA, GL30.GL_COLOR_ATTACHMENT0);
		refractionFrameBuffer.attachRenderBuffer(GL11.GL_TEXTURE_2D, GL30.GL_DEPTH24_STENCIL8);

		reflectionFrameBuffer = new FrameBuffer(REFLECTION_FB_WIDTH, REFLECTION_FB_HEIGHT);
		reflectionFrameBuffer.attachTexture(GL11.GL_TEXTURE_2D, GL11.GL_RGBA, GL11.GL_RGBA, GL30.GL_COLOR_ATTACHMENT0);
		reflectionFrameBuffer.attachRenderBuffer(GL11.GL_TEXTURE_2D, GL30.GL_DEPTH24_STENCIL8);
	}

	public void batch(WaterPlane waterPlane) {
		waterPlanes.add(waterPlane);
	}

	public void render() {
		for (WaterPlane waterPlane : waterPlanes) {
			// Enable vertex clipping for create refraction and reflection
			// texture creation
			GL11.glEnable(GL30.GL_CLIP_DISTANCE5);

			/* FIRST DRAW PASS: Refraction texture */
			refractionFrameBuffer.bind(0, 0);

			// Clear the bound frame buffer
			FrameBuffer.clear(true, true, false);

			// Make sure everything over the water surface gets clipped
			try (MemoryStack stack = MemoryStack.stackPush()) {
				Vec4 vec = waterPlane.getPlane().inEquationForm().mul(new Vec4(1.0f, -1.0f, 1.0f, 1.0f));
				ByteBuffer buffer = stack.calloc(vec.get140DataSize());
				vec.get140Data(buffer);
				buffer.flip();
				ShaderProgram.map("WATER_CLIP_PLANES", "WATER_CLIP_PLANE", buffer);
			}

			// Render world
			TerrainRenderer.INSTANCE.render();
			ModelRenderer.render();

			// Unbind refraction frame buffer
			refractionFrameBuffer.unbind();

			/* SECOND DRAW PASS: Reflection texture */
			reflectionFrameBuffer.bind(0, 0);

			// Clear the bound frame buffer
			FrameBuffer.clear(true, true, false);

			// Make sure everything under the water surface gets clipped
			try (MemoryStack stack = MemoryStack.stackPush()) {
				Vec4 vec = waterPlane.getPlane().inEquationForm();
				ByteBuffer buffer = stack.calloc(vec.get140DataSize());
				vec.get140Data(buffer);
				buffer.flip();
				ShaderProgram.map("CLIP_PLANES", "CLIP_PLANE", buffer);
			}

			// Render world
			TerrainRenderer.INSTANCE.render();
			ModelRenderer.render();

			// Unbind reflection frame buffer
			reflectionFrameBuffer.unbind();

			// Disable vertex clipping so that everything can now be rendered
			GL11.glDisable(GL30.GL_CLIP_DISTANCE5);

			/* FINAL DRAW PASS: Water */

			// Activate shader program
			shaderProgram.enable();

			SkyBoxRenderer.INSTANCE.getSkyBoxCubeMap().bind(); // Unable to call after bindVertexArray call ?!

			// Bind mesh VAO
			GL30.glBindVertexArray(meshData.getVao());

			// Enable attributes
			GL20.glEnableVertexAttribArray(ShaderAttribute.POSITION.getLocation()); // Position attribute
			GL20.glEnableVertexAttribArray(ShaderAttribute.TEXTURECOORD.getLocation()); // Texture coordinate attribute
			GL20.glEnableVertexAttribArray(ShaderAttribute.NORMAL.getLocation()); // Normal attribute

			// Bind refraction, reflection and sky box texture
			new TextureMap2D(refractionFrameBuffer.getTextureAttachment(GL30.GL_COLOR_ATTACHMENT0), SamplerUniform.WATER_REFRACTION.getTextureSlot()).bind();
			new TextureMap2D(reflectionFrameBuffer.getTextureAttachment(GL30.GL_COLOR_ATTACHMENT0), SamplerUniform.WATER_REFLECTION.getTextureSlot()).bind();

			// Upload water plane model matrix
			try (MemoryStack stack = MemoryStack.stackPush()) {
				ByteBuffer buffer = stack.calloc(waterPlane.get140DataSize());
				waterPlane.get140Data(buffer);
				buffer.flip();
				ShaderProgram.map("MODEL", buffer);
			}

			// Render water plane
			GL11.glDrawElements(GL11.GL_TRIANGLES, meshData.getIndicesAmount(), GL11.GL_UNSIGNED_INT, 0);

			// Disable attributes
			GL20.glDisableVertexAttribArray(ShaderAttribute.POSITION
					.getLocation()); // Position attribute
			GL20.glDisableVertexAttribArray(ShaderAttribute.TEXTURECOORD
					.getLocation()); // Texture coordinate attribute
			GL20.glDisableVertexAttribArray(ShaderAttribute.NORMAL
					.getLocation()); // Normal attribute
			GL20.glDisableVertexAttribArray(ShaderAttribute.TANGENT
					.getLocation()); // Tangent attribute

			// Unbind mesh VAO
			GL30.glBindVertexArray(0);
		}

		// Deactivate shader.
		GL20.glUseProgram(0);
	}

	public void clearBatch() {
		refractionFrameBuffer.delete();
		reflectionFrameBuffer.delete();
		waterPlanes.clear();
	}
}
