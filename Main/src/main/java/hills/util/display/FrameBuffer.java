package hills.util.display;

import hills.services.ServiceLocator;
import hills.services.display.DisplayServiceI;
import lombok.Getter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrameBuffer {			// Move

	/**
	 * The handle (id) of this frame buffer.
	 */
	private final int handle;

	/**
	 * A list containing all loaded texture attachments for later freeing.
	 */
	private final Map<Integer, Integer> textureAttachmentsHandles = new HashMap<Integer, Integer>();

	/**
	 * A list containing all loaded render buffer attachments for later freeing.
	 */
	private final List<Integer> renderBufferAttachmentsHandles = new ArrayList<Integer>();

	/**
	 * Size variable for frame buffer to resize GL viewport to when binding.
	 */
	@Getter
	private final int width, height;
	

	public FrameBuffer(int width, int height) {
		handle = GL30.glGenFramebuffers();
		this.width = width;
		this.height = height;
	}

	/**
	 * Will bind this frame buffer.
	 */
	public void bind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, handle);
	}

	/**
	 * Will bind this frame buffer and resize the GL viewport<br>
	 * according to this objects stored width and height.
	 * 
	 * @param x
	 *            - viewport x position.
	 * @param y
	 *            - viewport y position.
	 */
	public void bind(int x, int y) {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, handle);
		GL11.glViewport(x, y, width, height);
	}

	/**
	 * Will bind this frame buffer and resize the GL viewport.
	 * 
	 * @param x
	 *            - viewport x position.
	 * @param y
	 *            - viewport y position.
	 * @param width
	 *            - viewport width.
	 * @param height
	 *            - viewport height.
	 */
	public void bind(int x, int y, int width, int height) {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, handle);
		GL11.glViewport(x, y, width, height);
	}

	/**
	 * Will unbind this (all) frame buffer making<br>
	 * the default frame buffer bound. Will also resize<br>
	 * the GL view port to Display.WIDTH and Display.HEIGHT<br>
	 * at x = 0, y = 0.
	 */
	public void unbind(int windowWidth, int windowHeight) {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0,windowWidth,windowHeight);
	}

	/**
	 * Attach a new texture to this frame buffer.
	 * 
	 * @param target
	 *            - The texture target.
	 * @param internalFormat
	 *            - The textures internal format.
	 * @param format
	 *            - The texel data format.
	 * @param attachment
	 *            - The attachment point of the frame buffer.
	 */
	public void attachTexture(int target, int internalFormat, int format,
			int attachment, int windowWidth, int windowHeight) {
		if (width == 0 || height == 0) {
			System.err
					.println("Frambuffer texture attaching failed! Width or height is equal 0");
			return;
		}

		bind();

		int handle = GL11.glGenTextures();
		GL11.glBindTexture(target, handle);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_BASE_LEVEL, 0);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 0);
		GL11.glTexImage2D(target, 0, internalFormat, width, height, 0, format,
				GL11.GL_UNSIGNED_BYTE, MemoryUtil.NULL);
		GL11.glBindTexture(target, 0);

		GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

		if (target == GL11.GL_TEXTURE_1D)
			GL30.glFramebufferTexture1D(GL30.GL_FRAMEBUFFER, attachment,
					GL11.GL_TEXTURE_1D, handle, 0);
		else if (target == GL11.GL_TEXTURE_2D)
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment,
					GL11.GL_TEXTURE_2D, handle, 0);
		else if (target == GL12.GL_TEXTURE_3D)
			GL30.glFramebufferTexture3D(GL30.GL_FRAMEBUFFER, attachment,
					GL12.GL_TEXTURE_3D, handle, 0, 0);

		textureAttachmentsHandles.put(attachment, handle);
		unbind(windowWidth,windowHeight);
	}

	/**
	 * Attach a new render buffer object to this frame buffer.
	 * 
	 * @param internalFormat
	 *            - The textures internal format.
	 * @param attachment
	 *            - The attachment point of the frame buffer.
	 */
	public void attachRenderBuffer(int internalFormat, int attachment, int windowWidth, int windowHeight) {
		if (width == 0 || height == 0) {
			System.err
					.println("Frambuffer render buffer attaching failed! Width or height is equal 0");
			return;
		}

		bind();

		int handle = GL30.glGenRenderbuffers();

		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, handle);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, internalFormat, width,
				height);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);

		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment,
				GL30.GL_RENDERBUFFER, handle);

		renderBufferAttachmentsHandles.add(handle);
		unbind(windowWidth,windowHeight);
	}

	public int getTextureAttachment(int attachment) {
		return textureAttachmentsHandles.get(attachment);
	}

	/**
	 * @return True if glCheckFramebufferStatus(GL_FRAMEBUFFER) =
	 *         GL_FRAMEBUFFER_COMPLETE
	 */
	public boolean complete() {
		return GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) == GL30.GL_FRAMEBUFFER_COMPLETE;
	}

	/**
	 * Clean up this object by releasing all it's previously allocated memory.
	 */
	public void delete() {
		for (int i : textureAttachmentsHandles.values())
			GL11.glDeleteTextures(i);
		textureAttachmentsHandles.clear();

		for (int i : renderBufferAttachmentsHandles)
			GL30.glDeleteRenderbuffers(i);
		renderBufferAttachmentsHandles.clear();

		GL30.glDeleteFramebuffers(handle);
	}

	/**
	 * Set clear color.
	 * 
	 * @param r
	 *            - Amount of red (0.0 - 1.0).
	 * @param g
	 *            - Amount of green (0.0 - 1.0).
	 * @param b
	 *            - Amount of blue (0.0 - 1.0).
	 * @param a
	 *            - Amount of alpha (0.0 - 1.0).
	 */
	public static void setClearColor(float r, float g, float b, float a) {
		GL11.glClearColor(r, g, b, a);
	}

	/**
	 * Enables depth testing.
	 * 
	 * @param zNear
	 *            - Map near plane to in window coordinates. (0.0 - 1.0 utilizes
	 *            full depth buffer range).
	 * @param zFar
	 *            - Map far plane to in window coordinates. (0.0 - 1.0 utilizes
	 *            full depth buffer range).
	 */
	public static void enableDepthTesting(float zNear, float zFar) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthRange(zNear, zFar);
	}

	/**
	 * Disables depth testing.
	 */
	public static void disableDepthTesting() {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	/**
	 * Set depth value used to clear depth buffer.
	 * 
	 * @param depth
	 *            - Value to clear the depth buffer with.
	 */
	public static void setClearDepth(float depth) {
		GL11.glClearDepth(depth);
	}

	/**
	 * Set value used to clear stencil buffer.
	 * 
	 * @param stencil
	 *            - value to clear the stencil buffer with.
	 */
	public static void setClearStencil(int stencil) {
		GL11.glClearStencil(stencil);
	}

	/**
	 * Clear pixel values by setting them to predefined state.
	 * 
	 * @param color
	 *            - Set pixel color value to clear color.
	 * @param depth
	 *            - Set pixel depth value to depth value.
	 * @param stencil
	 *            - Set pixel stencil value to stencil value.
	 */
	public static void clear(boolean color, boolean depth, boolean stencil) {
		GL11.glClear((color ? GL11.GL_COLOR_BUFFER_BIT : 0)
				| (depth ? GL11.GL_DEPTH_BUFFER_BIT : 0)
				| (stencil ? GL11.GL_STENCIL_BUFFER_BIT : 0));
	}

	/**
	 * Sets the depth function that OpenGL should use when depth testing.
	 */
	public static void setDepthFunction(int depthFunc) {
		GL11.glDepthFunc(depthFunc);
	}

	/**
	 * Enables culling on back face. Front face is CCW.
	 */
	public static void enableCulling() {
		GL11.glFrontFace(GL11.GL_CCW);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	/**
	 * Disables culling.
	 */
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
}
