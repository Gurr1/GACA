package hills.view;

import hills.engine.water.WaterPlane;

public interface IWaterRendererBatchable {

	public void batch(WaterPlane waterPlane);
	public void clearBatch();
}
