package hills.Gurra.Models;

import hills.Gurra.View.CameraSystem;
import hills.engine.math.Mat4;
import hills.engine.math.Quaternion;
import hills.engine.math.Vec3;
import hills.engine.math.shape.Frustrum;
import hills.engine.renderer.shader.ShaderProgram;
import hills.engine.system.EngineSystem;
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
        // Initialize camera at 0, 0, 0. Right is +X, Up is +Y, Depth is -Z.
        this.position = new Vec3(100, 100, 0);
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

    /**
     * Rotate camera along cameras right axis.
     * @param angle - Degrees to rotate.

     * Rotate camera along axis x, y, z.
     * @param angle - degrees to rotate.
     * @param x - X component of axis to rotate around.
     * @param y - Y component of axis to rotate around.
     * @param z - Z component of axis to rotate around.
     */
    public void setRotate(float angle, float x, float y, float z){
        setRotate(angle, new Vec3(x, y, z));
    }


    private void setRotate(float angle, Vec3 axis){
        Quaternion rotQuat = new Quaternion(axis, angle);

        forward = rotQuat.mul(forward).normalize();
        up = rotQuat.mul(up).normalize();
        right = forward.cross(up);
        toUpdate = true;
    }

    public void setPitch(float degrees){
        setRotate(degrees, right);
    }

    /**
     * Rotate camera along cameras up axis.
     * @param degrees - Degrees to rotate.
     */
    public void setYaw(float degrees){
        setRotate(degrees, new Vec3(0,1,0));
    }

    /**
     * Rotate camera along cameras forward axis.
     * @param degrees - Degrees to rotate.
     */
    public void setRoll(float degrees){
        setRotate(degrees, forward);
    }

    @Override
    public void render() {

    }

    @Override
    public void cleanUp() {

    }

    public void setParams(Vec3 position, Vec3 forward, Vec3 right, Vec3 up) {
        this.position = position;
        this.forward = forward;
        this.right = right;
        this.up = up;
        toUpdate = true;
    }
}
