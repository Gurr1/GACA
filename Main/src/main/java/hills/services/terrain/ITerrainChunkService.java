package hills.services.terrain;

import hills.model.ImmovableObject;
import hills.util.math.Vec3;

import java.util.List;

/**
 * Created by Anders on 2017-07-17.
 * @Author Anders Hansson
 */
public interface ITerrainChunkService {
    List<Chunk> getChunks(Vec3 PlayerPos);
    Chunk getChunk(Vec3 PlayerPos);
    void addObject(ImmovableObject object);
}
