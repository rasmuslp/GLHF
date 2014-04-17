package glhf.message.server;

import glhf.message.GlhfMessageType;
import glhf.message.ListMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

/**
 * A list of all relevant {@link Client}s. 'Relevant' may depend on context.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class IdsMessage extends ListMessage< Integer > {

	public IdsMessage( List< Integer > list ) {
		super( GlhfMessageType.S_IDS, list );
	}

	@Override
	protected void serializeStatic( ByteArrayWriter to ) throws IOException {
		// No static information to serialise.
	}

	@Override
	protected void serializeListObject( int atIndex, ByteArrayWriter to ) throws IOException {
		int value = this.list.get( atIndex );
		to.writeInt( value );
	}

	/**
	 * Construct an IdsMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed IdsMessage.
	 */
	public static IdsMessage parse( ByteArrayReader payload ) {
		try {
			List< Integer > ids = new ArrayList<>();
			int count = payload.readInt();
			for ( int i = 0; i < count; i++ ) {
				int id = payload.readInt();
				ids.add( id );
			}
			return new IdsMessage( ids );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing IdsMessage:", e );
		}

		return null;
	}

}
