package glhf.common.entity;

/**
 * An abstract implementation of a single Entity.
 * <p>
 * This object wraps another object such that it is guaranteed that it can be serialised. The specific implementations
 * provides the individual serialisations.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 * @param <T>
 *            The object type to wrap.
 */
public abstract class SingleEntity< T > implements Entity {

	/**
	 * The wrapped Object.
	 */
	protected final T object;

	/**
	 * Creates the wrapper.
	 * 
	 * @param object
	 *            The object to wrap.
	 */
	public SingleEntity( final T object ) {
		if ( object == null ) {
			throw new IllegalArgumentException( "Object cannot be null." );
		}
		this.object = object;
	}

	/**
	 * @return The wrapped object.
	 */
	public T getObject() {
		return this.object;
	}

}
