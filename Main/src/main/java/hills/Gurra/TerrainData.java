package hills.Gurra;

import hills.engine.math.Vec3;
import lombok.Getter;

/**
 * Created by gustav on 2017-03-31.
 */
public class TerrainData {
    @Getter private Vec3 position;
    @Getter private Vec3 normal;
    @Getter private TerrainTexture texture;
    public TerrainData(Vec3 position, Vec3 offset1, Vec3 offset2){

        this.position = position;
        Vec3 rel1 = offset1.sub(position);
        Vec3 rel2 = offset2.sub(position);
        normal = rel1.cross(rel2).normalize();
        texture = calculateTerrainTexture();
    }
    public TerrainTexture calculateTerrainTexture() {
        return TerrainTexture.GRASS;

    }
}
