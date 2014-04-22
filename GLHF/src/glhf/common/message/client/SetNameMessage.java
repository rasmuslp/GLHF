package glhf.common.message.client;

import glhf.common.message.GlhfMessage;
import glhf.common.message.GlhfMessageType;

import java.io.IOException;
import java.nio.charset.Charset;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

public class SetNameMessage extends GlhfMessage {

	private final String name;

	public SetNameMessage( final String name ) {
		super( GlhfMessageType.C_NAME );
		if ( name == null ) {
			throw new IllegalArgumentException( "Name cannot be null." );
		}
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	protected void serializePayload( ByteArrayWriter to ) throws IOException {
		to.writeByteArray( this.name.getBytes( Charset.forName( "UTF-8" ) ) );
	}

	/**
	 * Construct an SetNameMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed SetNameMessage.
	 */
	public static SetNameMessage parse( ByteArrayReader payload ) {
		try {
			int bytes = payload.bytesAvailable();
			byte[] data = new byte[bytes];
			payload.readByteArray( data );
			String name = new String( data, Charset.forName( "UTF-8" ) );
			return new SetNameMessage( name );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing SetNameMessage:", e );
		}

		return null;
	}

}
