package glhf.common.message.server;

import glhf.client.GlhfClient;
import glhf.common.entity.single.IntegerEntity;
import glhf.common.message.EntityListMessage;
import glhf.common.message.GlhfMessageType;

import java.io.IOException;
import java.util.List;

import crossnet.util.ByteArrayWriter;

/**
 * A list of IDs of all relevant {@link GlhfClient}s. 'Relevant' may depend on context.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class IdsMessage extends EntityListMessage< IntegerEntity > {

	public IdsMessage( List< IntegerEntity > list ) {
		super( GlhfMessageType.S_IDS, list );
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
