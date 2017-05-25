package hills.services.loader;

/**
 * Created by Anders on 2017-05-24.
 */
public interface IShaderLoader {
    int load(String fileName, int type);

    void cleanUp();
}
