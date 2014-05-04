package glhf.common.message.server;

import glhf.client.GlhfClient;
import glhf.common.entity.tuple.IdStringEntity;
import glhf.common.message.GlhfMessageType;
import glhf.common.message.type.EntityListMessage;

import java.io.IOException;
import java.util.List;

import crossnet.util.ByteArrayWriter;

/**
 * Names of relevant {@link GlhfClient}s. 'Relevant' may depend on context.
 * <p>
 * NB: This may be partial within the context.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class NamesMessage extends EntityListMessage< IdStringEntity > {

	public NamesMessage( List< IdStringEntity > names ) {
		super( GlhfMessageType.S_NAMES, names );
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
