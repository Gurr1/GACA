package hills.view;

import hills.util.math.Mat4;
import hills.util.model.Model;
import hills.util.shader.ShaderProgram;

/**
 * @author Anton
 */
public interface IModelRendererBatchable {

	public void batch(ShaderProgram program, Model model, Mat4 transformation);
	public void clearBatch();
}
