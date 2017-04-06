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
    public TerrainData(Vec3 position, Vec3 vec1Norm, Vec3 vec2Norm, Vec3 vec3Norm, Vec3 vec4Norm){
        this.position = position;
        Vec3 rel1 = vec1Norm.sub(vec2Norm);
        Vec3 rel2 = vec3Norm.sub(vec4Norm);
        normal = rel1.cross(rel2).normalize();
        texture = calculateTerrainTexture();
    }
    public TerrainTexture calculateTerrainTexture() {
        return TerrainTexture.GRASS;
    }
}
