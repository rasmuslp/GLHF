package glhf.common.message.server;

import glhf.client.GlhfClient;
import glhf.common.entity.tuple.IdIntegerEntity;
import glhf.common.message.GlhfEntityListMessage;
import glhf.common.message.GlhfMessageType;

import java.io.IOException;
import java.util.List;

import crossnet.util.ByteArrayWriter;

/**
 * Pings of relevant {@link GlhfClient}s. 'Relevant' may depend on context.
 * <p>
 * NB: This may be partial within the context.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class PingsMessage extends GlhfEntityListMessage< IdIntegerEntity > {

	public PingsMessage( List< IdIntegerEntity > list ) {
		super( GlhfMessageType.S_PINGS, list );
	}

	@Override
	protected void serializeStatic( ByteArrayWriter to ) throws IOException {
		// No static information to serialise.

	}

	@Override
	protected void serializeListObject( int atIndex, ByteArrayWriter to ) throws IOException {
		this.list.get( atIndex ).serialise( to );
	}

}
