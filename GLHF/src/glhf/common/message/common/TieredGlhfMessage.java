package glhf.common.message.common;

import glhf.common.message.GlhfMessage;
import glhf.common.message.GlhfMessageType;

import java.io.IOException;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

/**
 * This is for sending raw data.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class TieredGlhfMessage extends GlhfMessage {

	/**
	 * The payload.
	 */
	private final byte[] data;

	/**
	 * Create a new TieredGlhfMessage with a payload.
	 * 
	 * @param data
	 *            The payload.
	 */
	public TieredGlhfMessage( final byte[] data ) {
		super( GlhfMessageType.TIERED );
		if ( data == null ) {
			throw new IllegalArgumentException( "Data cannot be null." );
		}
		this.data = data;
	}

	/**
	 * Gets the payload.
	 * 
	 * @return The payload.
	 */
	public byte[] getData() {
		return this.data;
	}

	@Override
	protected void serializePayload( ByteArrayWriter to ) throws IOException {
		to.writeByteArray( this.data );
	}

	/**
	 * Construct a TieredGlhfMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed TieredGlhfMessage.
	 */
	public static TieredGlhfMessage parse( ByteArrayReader payload ) {
		try {
			int bytes = payload.bytesAvailable();
			byte[] data = new byte[bytes];
			payload.readByteArray( data );
			return new TieredGlhfMessage( data );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing TieredGlhfMessage:", e );
		}

		return null;
	}

}
