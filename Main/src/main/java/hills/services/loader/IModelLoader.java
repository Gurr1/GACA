package hills.services.loader;

import hills.util.math.Mat4;
import hills.util.math.Vertex;
import hills.util.model.Mesh;
import hills.util.model.MeshTexture;

/**
 * Created by Anders on 2017-05-22.
 */
public interface IModelLoader {
    Mesh load(Vertex[] vertices, int[] indices, MeshTexture texture, Mat4 transformation);

    int createEmptyVBO(int target, long byteCount, int usage);

    void addVAOAttribute(int VAO, int VBO, int attribute,
                         int dataSize, int type, boolean normalize, int stride, long offset,
                         int attributeDivisor);

    void deleteVAO(int vao);

    void deleteVBO(int vbo);

    void cleanUp();
}
