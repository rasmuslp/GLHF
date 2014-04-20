package glhf.common.message;

/**
 * A tuple paring an ID and a type <T>.
 * <p>
 * NB: Immutable.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 * @param <T>
 *            The type to pair with an ID.
 */
public class IdTuple< T > {

	/**
	 * The ID.
	 */
	private final int id;

	/**
	 * The paired value.
	 */
	private final T value;

	/**
	 * Create a tuple of ID and value.
	 * 
	 * @param id
	 *            The ID.
	 * @param value
	 *            The paired value.
	 */
	public IdTuple( final int id, final T value ) {
		this.id = id;
		this.value = value;
	}

	/**
	 * @return The ID.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return The paired value.
	 */
	public T getValue() {
		return this.value;
	}

}
