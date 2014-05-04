package glhf.common.entity;

import glhf.common.entity.single.IntegerEntity;

/**
 * An abstract implementation of a tuple consisting of an ID and a SingleEntity.
 * <p>
 * This object wraps an ID and a SingleEntity such that it is guaranteed that it can be serialised. The specific
 * implementations provides the individual serialisations.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 * @param <T>
 */
public abstract class IdSingleEntity< T extends SingleEntity< ? > > implements Entity {

	/**
	 * The ID.
	 */
	protected final IntegerEntity id;

	/**
	 * The paired Entity.
	 */
	protected final T entity;

	/**
	 * Create the wrapper.
	 * 
	 * @param id
	 *            The ID.
	 * @param entity
	 *            The SingleEntity to wrap.
	 */
	public IdSingleEntity( final int id, final T entity ) {
		if ( entity == null ) {
			throw new IllegalArgumentException( "Entity cannot be null." );
		}
		this.id = new IntegerEntity( id );
		this.entity = entity;
	}

	/**
	 * @return The ID.
	 */
	public int getId() {
		return this.id.getObject();
	}

	/**
	 * @return The paired Entity.
	 */
	public T getEntity() {
		return this.entity;
	}

}
