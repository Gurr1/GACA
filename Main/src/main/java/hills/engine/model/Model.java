package hills.engine.model;


public class Model {

	private final Mesh[] meshes;
	
	public Model(Mesh[] meshes){
		this.meshes = new Mesh[meshes.length];
		for(int i = 0; i < meshes.length; i++)
			this.meshes[i] = meshes[i];
	}

	/**
	 * @return Cloned array of all meshes in model.
	 */
	public Mesh[] getMeshes(){
		return meshes.clone();
	}
}
