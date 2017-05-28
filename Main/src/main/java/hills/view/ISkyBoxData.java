package hills.view;

import hills.util.texturemap.CubeMap;

/**
 * @author Anton
 */
public interface ISkyBoxData {
	public void setSkyBoxCubeMap(CubeMap skyBox);
	public CubeMap getSkyBoxCubeMap();
}
