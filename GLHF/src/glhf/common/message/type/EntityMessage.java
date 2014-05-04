package glhf.common.message.type;

import glhf.common.entity.SingleEntity;
import glhf.common.message.GlhfMessage;
import glhf.common.message.GlhfMessageType;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

public abstract class EntityMessage< T extends SingleEntity< ? > > extends GlhfMessage {

	private final T entity;

	public EntityMessage( GlhfMessageType glhfMessageType, T value ) {
		super( glhfMessageType );
		this.entity = value;
	}

	public T getEntity() {
		return this.entity;
	}

	@Override
	protected void serializeGlhfPayload( ByteArrayWriter to ) throws IOException {
		this.entity.serialise( to );
	}

}
