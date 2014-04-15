package glhf.message;

public class IdTuple< T > {

	private final int id;
	private final T value;

	public IdTuple( final int id, final T value ) {
		this.id = id;
		this.value = value;
	}

	public int getId() {
		return this.id;
	}

	public T getValue() {
		return this.value;
	}

}
