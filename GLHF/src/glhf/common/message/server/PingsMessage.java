package glhf.common.message.server;

import glhf.client.GlhfClient;
import glhf.common.message.GlhfListMessage;
import glhf.common.message.GlhfMessageType;
import glhf.common.message.IdTuple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

/**
 * Pings of relevant {@link GlhfClient}s. 'Relevant' may depend on context.
 * <p>
 * NB: This may be partial within the context.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class PingsMessage extends GlhfListMessage< IdTuple< Integer >> {

	public PingsMessage( List< IdTuple< Integer >> list ) {
		super( GlhfMessageType.S_PINGS, list );
	}

	@Override
	protected void serializeStatic( ByteArrayWriter to ) throws IOException {
		// No static information to serialise.

	}

	@Override
	protected void serializeListObject( int atIndex, ByteArrayWriter to ) throws IOException {
		IdTuple< Integer > idTuple = this.list.get( atIndex );
		to.writeInt( idTuple.getId() );
		to.writeInt( idTuple.getValue() );
	}

	/**
	 * Construct an PingsMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed PingsMessage.
	 */
	public static PingsMessage parse( ByteArrayReader payload ) {
		try {
			List< IdTuple< Integer > > tuples = new ArrayList<>();
			int count = payload.readInt();
			for ( int i = 0; i < count; i++ ) {
				int id = payload.readInt();
				int ping = payload.readInt();
				tuples.add( new IdTuple<>( id, ping ) );
			}
			return new PingsMessage( tuples );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing PingsMessage:", e );
		}

		return null;
	}
}
