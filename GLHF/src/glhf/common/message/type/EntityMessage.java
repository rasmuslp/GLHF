package glhf.common.message.type;

import glhf.common.entity.Entity;
import glhf.common.message.GlhfMessage;
import glhf.common.message.GlhfMessageType;

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
public abstract class EntityMessage< T extends Entity > extends GlhfMessage {

	/**
	 * The Entity.
	 */
	private final T entity;

	/**
	 * Create a new EntityMessage of provided type and Entity.
	 * 
	 * @param glhfMessageType
	 *            The type of GlhfMessage.
	 * @param value
	 *            The Entity.
	 */
	public EntityMessage( GlhfMessageType glhfMessageType, T value ) {
		super( glhfMessageType );
		this.entity = value;
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
