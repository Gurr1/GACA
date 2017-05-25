package hills.services.loader;

import hills.services.terrain.mesh.GridMeshData;

/**
 * Created by Anders on 2017-05-24.
 */
public interface ITerrainLoader {
    GridMeshData loadGridMesh(int width, int depth, int terrainWidth, int terrainDepth);
}
