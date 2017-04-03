package hills.engine.model;


public class Model {

	private final Mesh[] meshes;
	
	public Model(Mesh[] meshes){
		this.meshes = new Mesh[meshes.length];
		for(int i = 0; i < meshes.length; i++)
			this.meshes[i] = meshes[i];
	}
	
	public boolean contains(Mesh mesh){
	//	for(Mesh m: meshes)
		//	if(mesh.getId() == m.getId())
		//		return true;
		
		return false;
	}
	
	/**
	 * @return Cloned array of all meshes in model.
	 */
	public Mesh[] getMeshes(){
		return meshes.clone();
	}
}
