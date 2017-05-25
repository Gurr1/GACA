package hills.services.loader;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by Anders on 2017-05-24.
 */
public interface ITextureLoader {
    ByteBuffer PNGToByteBuffer(String path, IntBuffer width, IntBuffer height, boolean flip);

    ByteBuffer PNGToByteBuffer(String path, boolean flip);

    int loadTexture(String path, boolean flip);

    int loadTexture(String path);

    int loadCubeMapTexture(String[] paths, String name, boolean flip);

    void setTexParameters(int WRAP_S, int WRAP_T, int WRAP_R, int MAG, int MIN);

    int getTexture(String name);

    void freeTexture(String name) throws Exception;

    void freeTexture(int handle) throws Exception;

    void cleanUp();
}
