package hills.services.terrain;

import com.sun.org.apache.xpath.internal.operations.String;
import hills.model.ImmovableObject;
import hills.util.math.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 2017-07-17.
 * @Author Anders Hansson
 */
public class ChunkService implements ITerrainChunkService{
    private Chunk[][] map;
    private int delta;

    ChunkService (){
        map = new Chunk[16][16];
        delta = 128;
        for (int i = 0; i < map.length ; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = new Chunk(j,i);
            }
        }

    }

    public void addObject(ImmovableObject object){
        Vec3 v = object.getBoundingSphere().getPos();
        int x = (int) (v.getX()/delta);
        int y = (int) (v.getZ()/delta);
        map[y][x].addObject(object);
    }

    public List<Chunk> getChunks(Vec3 PlayerPos){
        List<Chunk> chunks = new ArrayList<>();
        int x = (int) (PlayerPos.getX()/delta);
        int y = (int) (PlayerPos.getZ()/delta);
        for (int i = y-2; i < y+2; i++) {
            if(i >= 0 && i < 16)
            for (int j = x-2; j < x+2; j++) {
                if (j >= 0 && j < 16){
                    chunks.add(map[i][j]);
                }
            }
        }
        return chunks;
    }
}
