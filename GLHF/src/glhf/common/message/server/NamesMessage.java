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
 * Names of relevant {@link GlhfClient}s. 'Relevant' may depend on context.
 * <p>
 * NB: This may be partial within the context.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class NamesMessage extends GlhfListMessage< IdTuple< String > > {

	public NamesMessage( List< IdTuple< String > > names ) {
		super( GlhfMessageType.S_NAMES, names );
	}

	@Override
	protected void serializeStatic( ByteArrayWriter to ) throws IOException {
		// No static information to serialise.
	}

	@Override
	protected void serializeListObject( int atIndex, ByteArrayWriter to ) throws IOException {
		IdTuple< String > idTuple = this.list.get( atIndex );
		to.writeInt( idTuple.getId() );
		to.writeString255( idTuple.getValue() );
	}

	/**
	 * Construct an NamesMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed NamesMessage.
	 */
	public static NamesMessage parse( ByteArrayReader payload ) {
		try {
			List< IdTuple< String > > tuples = new ArrayList<>();
			int count = payload.readInt();
			for ( int i = 0; i < count; i++ ) {
				int id = payload.readInt();
				String name = payload.readString255();
				tuples.add( new IdTuple<>( id, name ) );
			}
			return new NamesMessage( tuples );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing NamesMessage:", e );
		}

		return null;
	}
}
