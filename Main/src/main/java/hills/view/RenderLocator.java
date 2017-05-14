package hills.view;

public class RenderLocator {
	
	private ModelRenderer model;
	private SkyBoxRenderer skyBox;
	private TerrainRenderer terrain;
	private WaterRenderer water;
	
	
	
	private ModelRenderer getModelInstance(){
		if(model == null)
			model = new ModelRenderer();
		
		return model;
	}
	
	private SkyBoxRenderer getSkyBoxlInstance(){
		if(skyBox == null)
			skyBox = new SkyBoxRenderer();
		
		return skyBox;
	}
	
	private TerrainRenderer getTerrainInstance(){
		if(terrain == null)
			terrain = new TerrainRenderer();
		
		return terrain;
	}
	
	private WaterRenderer getWaterInstance(){
		if(water == null)
			water = new WaterRenderer();
		
		return water;
	}

}
