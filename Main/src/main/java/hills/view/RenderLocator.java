package hills.view;

/**
 * @Author Anton Annlöv
 */
public enum RenderLocator {
	INSTANCE();
	
	private ModelRenderer model;
	private SkyBoxRenderer skyBox;
	private TerrainRenderer terrain;
	private WaterRenderer water;
	
	public IModelRendererBatchable getModelBatchable(){
		return getModelInstance();
	}
	
	public ITerrainRendererBatchable getTerrainBatchable(){
		return getTerrainInstance();
	}

	public IWaterRendererBatchable getWaterBatchable(){
		return getWaterInstance();
	}
	
	public IRendererDrawable getModelDrawable(){
		return getModelInstance();
	}
	
	public IRendererDrawable getTerrainDrawable(){
		return getTerrainInstance();
	}

	public IRendererDrawable getWaterDrawable(){
		return getWaterInstance();
	}
	
	public IRendererDrawable getSkyBoxDrawable(){
		return getSkyBoxInstance();
	}
	
	public ISkyBoxData getSkyBoxData(){
		return getSkyBoxInstance();
	}
	
	private ModelRenderer getModelInstance(){
		if(model == null)
			model = new ModelRenderer();
		
		return model;
	}
	
	private SkyBoxRenderer getSkyBoxInstance(){
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
