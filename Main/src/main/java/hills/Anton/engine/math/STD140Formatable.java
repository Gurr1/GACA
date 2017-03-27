package hills.Anton.engine.math;

public interface STD140Formatable {
	
	public static final int STD140_FLOATS = 4;
	
	/**
	 * @return - Byte array of this classes data in
	 * std140 format for use in shader uniform buffer objects.
	 */
	public byte[] get140Data();
}
