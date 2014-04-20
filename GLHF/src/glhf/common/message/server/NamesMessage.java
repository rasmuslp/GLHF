package glhf.common.message.server;

import glhf.common.message.GlhfMessageType;
import glhf.common.message.IdTuple;
import glhf.common.message.ListMessage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

/**
 * Names of relevant {@link Client}s. 'Relevant' may depend on context.
 * <p>
 * NB: This may be partial.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class NamesMessage extends ListMessage< IdTuple< String > > {

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
		byte[] name = idTuple.getValue().getBytes( Charset.forName( "UTF-8" ) );
		to.writeByte( name.length );
		to.writeByteArray( name );
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
				int nameLength = payload.readUnsignedByte();
				byte[] nameBytes = new byte[nameLength];
				payload.readByteArray( nameBytes );
				String name = new String( nameBytes, Charset.forName( "UTF-8" ) );
				tuples.add( new IdTuple<>( id, name ) );
			}
			return new NamesMessage( tuples );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing NamesMessage:", e );
		}

		return null;
	}
}
