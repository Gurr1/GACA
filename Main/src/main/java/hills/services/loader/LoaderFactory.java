package hills.services.loader;

import lombok.Getter;

/**
 * Created by Anders on 2017-05-24.
 */
public class LoaderFactory {

    @Getter private IModelLoader modelLoader;
    @Getter private IShaderLoader shaderLoader;
    @Getter private ITerrainLoader terrainLoader;
    @Getter private ITextureLoader textureLoader;

    public LoaderFactory(){
        this.modelLoader = new ModelLoader();
        this.shaderLoader = new ShaderLoader();
        this.terrainLoader = new TerrainLoader(modelLoader);
        this.textureLoader = new TextureLoader();
    }
}
