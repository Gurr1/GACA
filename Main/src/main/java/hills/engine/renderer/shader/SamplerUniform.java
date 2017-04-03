package hills.engine.renderer.shader;

import org.lwjgl.opengl.GL13;

public enum SamplerUniform {

	DIFFUSE("diffuseMap", GL13.GL_TEXTURE0), NORMAL("normalMap",
			GL13.GL_TEXTURE1), OCCLUSION("occlusionMap", GL13.GL_TEXTURE2), OPACITY(
			"opacityMap", GL13.GL_TEXTURE3), SPECULAR_GLOSS("specularGlossMap",
			GL13.GL_TEXTURE4), EMISSIVE("emissiveMap", GL13.GL_TEXTURE5), TERRAIN_HEIGHT(
			"heightMap", GL13.GL_TEXTURE0), TERRAIN_NORMAL("normalMap",
			GL13.GL_TEXTURE1), TERRAIN_TEST_SAMPLER("testSampler",
			GL13.GL_TEXTURE2), WATER_REFRACTION("refractionTexture",
			GL13.GL_TEXTURE0), WATER_REFLECTION("reflectionTexture",
			GL13.GL_TEXTURE1), CUBE_MAP("cubeMap", GL13.GL_TEXTURE6);

	/**
	 * Name of sampler uniform.
	 */
	private final String name;

	/**
	 * Texture slot that sampler will be bound to.
	 */
	private final int textureSlot;

	/**
	 * Texture slot number that sampler will be bound to.
	 */
	private final int slot;

	private SamplerUniform(String name, int textureSlot) {
		this.name = name;
		this.textureSlot = textureSlot;
		slot = textureSlotToInt(textureSlot);
	}

	public String getName() {
		return name;
	}

	public int getTextureSlot() {
		return textureSlot;
	}

	public int getSlot() {
		return slot;
	}

	/**
	 * GL_TEXTUREX to shader program location. (A.K.A. X)
	 * 
	 * @param textureSlot
	 *            - GL13.GL_TEXTUREX
	 * @return X.
	 */
	private int textureSlotToInt(int textureSlot) {
		switch (textureSlot) {
		case GL13.GL_TEXTURE0:
			return 0;
		case GL13.GL_TEXTURE1:
			return 1;
		case GL13.GL_TEXTURE2:
			return 2;
		case GL13.GL_TEXTURE3:
			return 3;
		case GL13.GL_TEXTURE4:
			return 4;
		case GL13.GL_TEXTURE5:
			return 5;
		case GL13.GL_TEXTURE6:
			return 6;
		case GL13.GL_TEXTURE7:
			return 7;
		case GL13.GL_TEXTURE8:
			return 8;
		case GL13.GL_TEXTURE9:
			return 9;
		case GL13.GL_TEXTURE10:
			return 10;
		case GL13.GL_TEXTURE11:
			return 11;
		case GL13.GL_TEXTURE12:
			return 12;
		case GL13.GL_TEXTURE13:
			return 13;
		case GL13.GL_TEXTURE14:
			return 14;
		case GL13.GL_TEXTURE15:
			return 15;
		case GL13.GL_TEXTURE16:
			return 16;
		case GL13.GL_TEXTURE17:
			return 17;
		case GL13.GL_TEXTURE18:
			return 18;
		case GL13.GL_TEXTURE19:
			return 19;
		case GL13.GL_TEXTURE20:
			return 20;
		case GL13.GL_TEXTURE21:
			return 21;
		case GL13.GL_TEXTURE22:
			return 22;
		case GL13.GL_TEXTURE23:
			return 23;
		case GL13.GL_TEXTURE24:
			return 24;
		case GL13.GL_TEXTURE25:
			return 25;
		case GL13.GL_TEXTURE26:
			return 26;
		case GL13.GL_TEXTURE27:
			return 27;
		case GL13.GL_TEXTURE28:
			return 28;
		case GL13.GL_TEXTURE29:
			return 29;
		case GL13.GL_TEXTURE30:
			return 30;
		case GL13.GL_TEXTURE31:
			return 31;
		}

		System.err
				.println("genTextureSlot(int textureSlot) can only take in GL13.TEXTUREX values as argument.");
		System.exit(-1);
		return 0;
	}
}
