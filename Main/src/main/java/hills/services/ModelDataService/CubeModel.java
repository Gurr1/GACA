package hills.services.ModelDataService;

import hills.util.loader.ModelLoader;
import hills.util.math.Mat4;
import hills.util.math.Vec2;
import hills.util.math.Vec3;
import hills.util.math.Vertex;
import hills.util.model.Mesh;
import hills.util.model.MeshTexture;
import hills.util.model.Model;

/**
 * Created by gustav on 2017-05-15.
 */
public class CubeModel implements IModelService {
    Vertex[] v = {
            new Vertex(new Vec3(-.5f, -.5f, .5f), new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
            new Vertex(new Vec3(-.5f, -.5f, .5f), new Vec2(0.0f, 1.0f), new Vec3(0.0f, -1.0f, 0.0f)),
            new Vertex(new Vec3(-.5f, -.5f, .5f), new Vec2(1.0f, 0.0f), new Vec3(-1.0f, 0.0f, 0.0f)),

            new Vertex(new Vec3(.5f, -.5f, .5f), new Vec2(1.0f, 0.0f), new Vec3(0.0f, 0.0f, 1.0f)),
            new Vertex(new Vec3(.5f, -.5f, .5f), new Vec2(1.0f, 1.0f), new Vec3(0.0f, -1.0f, 0.0f)),
            new Vertex(new Vec3(.5f, -.5f, .5f), new Vec2(0.0f, 0.0f), new Vec3(1.0f, 0.0f, 0.0f)),

            new Vertex(new Vec3(.5f, .5f, .5f), new Vec2(1.0f, 1.0f), new Vec3(0.0f, 0.0f, 1.0f)),
            new Vertex(new Vec3(.5f, .5f, .5f), new Vec2(1.0f, 0.0f), new Vec3(0.0f, 1.0f, 0.0f)),
            new Vertex(new Vec3(.5f, .5f, .5f), new Vec2(0.0f, 1.0f), new Vec3(1.0f, 0.0f, 0.0f)),

            new Vertex(new Vec3(-.5f, .5f, .5f), new Vec2(0.0f, 1.0f), new Vec3(0.0f, 0.0f, 1.0f)),
            new Vertex(new Vec3(-.5f, .5f, .5f), new Vec2(0.0f, 0.0f), new Vec3(0.0f, 1.0f, 0.0f)),
            new Vertex(new Vec3(-.5f, .5f, .5f), new Vec2(1.0f, 1.0f), new Vec3(-1.0f, 0.0f, 0.0f)),

            new Vertex(new Vec3(-.5f, -.5f, -.5f), new Vec2(1.0f, 0.0f), new Vec3(0.0f, 0.0f, -1.0f)),
            new Vertex(new Vec3(-.5f, -.5f, -.5f), new Vec2(0.0f, 0.0f), new Vec3(0.0f, -1.0f, 0.0f)),
            new Vertex(new Vec3(-.5f, -.5f, -.5f), new Vec2(0.0f, 0.0f), new Vec3(-1.0f, 0.0f, 0.0f)),

            new Vertex(new Vec3(.5f, -.5f, -.5f), new Vec2(0.0f, 0.0f), new Vec3(0.0f, 0.0f, -1.0f)),
            new Vertex(new Vec3(.5f, -.5f, -.5f), new Vec2(1.0f, 0.0f), new Vec3(0.0f, -1.0f, 0.0f)),
            new Vertex(new Vec3(.5f, -.5f, -.5f), new Vec2(1.0f, 0.0f), new Vec3(1.0f, 0.0f, 0.0f)),

            new Vertex(new Vec3(.5f, .5f, -.5f), new Vec2(0.0f, 1.0f), new Vec3(0.0f, 0.0f, -1.0f)),
            new Vertex(new Vec3(.5f, .5f, -.5f), new Vec2(1.0f, 1.0f), new Vec3(0.0f, 1.0f, 0.0f)),
            new Vertex(new Vec3(.5f, .5f, -.5f), new Vec2(1.0f, 1.0f), new Vec3(1.0f, 0.0f, 0.0f)),

            new Vertex(new Vec3(-.5f, .5f, -.5f), new Vec2(1.0f, 1.0f), new Vec3(0.0f, 0.0f, -1.0f)),
            new Vertex(new Vec3(-.5f, .5f, -.5f), new Vec2(0.0f, 1.0f), new Vec3(0.0f, 1.0f, 0.0f)),
            new Vertex(new Vec3(-.5f, .5f, -.5f), new Vec2(0.0f, 1.0f), new Vec3(-1.0f, 0.0f, 0.0f)),
    };
    int[] ind = {
            0, 3, 6,
            0, 6, 9,

            15, 12, 21,
            15, 21, 18,

            14, 2, 11,
            14, 11, 23,

            5, 17, 20,
            5, 20, 8,

            10, 7, 19,
            10, 19, 22,

            13, 16, 4,
            13, 4, 1
    };
    private final Model sheep, tree, coin;

    public CubeModel(){
        MeshTexture texture = new MeshTexture("sheepTexture.png");
        Mesh cubeMesh = ModelLoader.load(v, ind, texture, Mat4.identity());
        sheep = new Model(new Mesh[]{cubeMesh});
        texture = new MeshTexture("barkTexture.png");
        cubeMesh = ModelLoader.load(v, ind, texture, Mat4.identity());
        tree = new Model(new Mesh[]{cubeMesh});
/*        texture = new MeshTexture("rockTexture.png");
        cubeMesh = ModelLoader.load(v, ind, texture, Mat4.identity());
        rock = new Model(new Mesh[]{cubeMesh});*/
        texture = new MeshTexture("coinTexture.png");
        cubeMesh = ModelLoader.load(v, ind, texture, Mat4.identity());
        coin = new Model(new Mesh[]{cubeMesh});
    }
    @Override
    public Model getSheep() {
        return sheep;
    }

    @Override
    public Model getTree() {
        return tree;
    }

    @Override
    public Model getRock() {
        return null;
    }

    @Override
    public Model getCoin() {
        return coin;
    }


}
