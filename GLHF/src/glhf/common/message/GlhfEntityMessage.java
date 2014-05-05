package glhf.common.message;

import glhf.common.entity.Entity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

/**
 * Abstract Message that can transport an Entity.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 * @param <T>
 *            The type of Entity.
 */
public abstract class GlhfEntityMessage< T extends Entity > extends GlhfMessage {

	/**
	 * The Entity.
	 */
	private final T entity;

	/**
	 * Create a new GlhfEntityMessage of provided type and Entity.
	 * 
	 * @param glhfMessageType
	 *            The type of GlhfMessage.
	 * @param entity
	 *            The Entity.
	 */
	public GlhfEntityMessage( GlhfMessageType glhfMessageType, T entity ) {
		super( glhfMessageType );
		this.entity = entity;
	}

	/**
	 * @return The Entity.
	 */
	public T getEntity() {
		return this.entity;
	}

	@Override
	protected void serializeGlhfPayload( ByteArrayWriter to ) throws IOException {
		this.entity.serialise( to );
	}

}
