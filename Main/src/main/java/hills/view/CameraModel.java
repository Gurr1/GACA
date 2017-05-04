package hills.view;

import hills.util.math.Mat4;
import hills.util.math.Vec3;
import hills.util.math.shape.Frustrum;
import hills.util.shader.ShaderProgram;
import hills.controller.EngineSystem;
import lombok.Getter;
import lombok.Setter;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

/**
 * Created by gustav on 2017-04-04.
 */
public class CameraModel extends EngineSystem{


    /**
     * Going forwards/backwards.
     */
    /**
     * Going left/right.
     */
    /**
     * Forwards/Backwards speed.
     */

    /**
     * Camera world position.
     */
    @Getter
    @Setter private Vec3 position;

    /**
     * Camera forward direction.
     */
    private Vec3 forward;

    /**
     * Camera up direction.
     */
    private Vec3 up;

    /**
     * Camera right direction.
     */
    private Vec3 right;

    /**
     * Camera view frustrum.
     */
    @Getter private Frustrum frustrum;

    /**
     * Distance to near plane of view frustrum
     */
    private float near;

    /**
     * Distance to far plane of view frustrum
     */
    private float far;

    /**
     * Aspect ratio of display area
     */
    private float aspect;

    /**
     * Vertical field of view in degrees
     */
    private float FOV;

    /**
     * To update camera or not (No need to update when nothing has changed).
     */
    private boolean toUpdate;

    private static CameraModel camera;
    private CameraSystem cameraSystem;

    /**
     * Creates the singleton instance of CameraSystem.
     * @return False if an instance has already been created.
     */
    public static boolean createInstance(float scale, boolean isPaused, float startTime) {
        if(camera != null)
            return false;

        camera = new CameraModel(scale, isPaused, startTime);
        return true;
    }

    /**
     *
     * @return The singleton instance of CameraSystem.
     * @throws NullPointerException If singleton instance has not been created.
     */
    public static CameraModel getInstance() throws NullPointerException {
        if(camera == null)
            throw new NullPointerException("Singleton instance not created!");

        return camera;
    }

    private CameraModel(float scale, boolean isPaused, float startTime) {
        super(scale, isPaused, startTime);
        cameraSystem = new CameraSystem();
        this.position = new Vec3(100, 0.0f, 100);
        this.forward = new Vec3(0.0f, 0.0f, -1.0f);
        this.up = new Vec3(0.0f, 1.0f, 0.0f);
        this.right = new Vec3(1.0f, 0.0f, 0.0f);
        frustrum = new Frustrum(near, far, aspect, FOV, position, forward, up, right, false);

        try(MemoryStack stack = MemoryStack.stackPush()){
            Mat4 identityMatrix = Mat4.identity();
            ByteBuffer dataBuffer = stack.calloc(identityMatrix.get140DataSize());
            identityMatrix.get140Data(dataBuffer);
            dataBuffer.flip();

            ShaderProgram.map("VIEW", "CAMERA", dataBuffer);
        }

        try(MemoryStack stack = MemoryStack.stackPush()){
            ByteBuffer dataBuffer = stack.calloc(position.get140DataSize());
            position.get140Data(dataBuffer);
            dataBuffer.flip();

            ShaderProgram.map("VIEW", "CAMPOSWORLD", dataBuffer);
        }

        toUpdate = false;
    }

    @Override
    protected void update(double delta) {
        // If nothing has changed don't update!
        if(!toUpdate)
            return;
        // Make sure forward, up and right vectors are normalized.
        forward = forward.normalize();
        up = up.normalize();
        right = forward.cross(up);
        cameraSystem.update(position, forward, up, right);
        frustrum = new Frustrum(near, far, aspect, FOV, position, forward, up, right, false);
        toUpdate = false;
    }

    public void updatePerspective(float near, float far, float aspect, float FOV) {
        this.near = near;
        this.far = far;
        this.aspect = aspect;
        this.FOV = FOV;
        cameraSystem.updatePerspective(near, far, aspect, FOV);
        toUpdate = true;
    }

    @Override
    public void render() {

    }

    @Override
    public void cleanUp() {

    }
        //TODO Change UP to global up, like in Player
    public void setParams(Vec3 position, Vec3 forward, Vec3 right, Vec3 up) {
        this.position = position;
        this.forward = forward;
        this.right = right;
        this.up = up;
        toUpdate = true;
    }
}
