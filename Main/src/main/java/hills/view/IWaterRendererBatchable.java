package hills.view;

import hills.engine.water.WaterPlane;

/**
 * @author Anton
 */
public interface IWaterRendererBatchable {

	public void batch(WaterPlane waterPlane);
	public void clearBatch();
}
