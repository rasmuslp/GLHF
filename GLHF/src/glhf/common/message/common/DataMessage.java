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
public class DataMessage extends GlhfMessage {

	/**
	 * The payload.
	 */
	private final byte[] data;

	/**
	 * Create a new DataMessage with a payload.
	 * 
	 * @param data
	 *            The payload.
	 */
	public DataMessage( final byte[] data ) {
		super( GlhfMessageType.DATA );
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
	 * Construct a DataMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed DataMessage.
	 */
	public static DataMessage parse( ByteArrayReader payload ) {
		try {
			int bytes = payload.bytesAvailable();
			byte[] data = new byte[bytes];
			payload.readByteArray( data );
			return new DataMessage( data );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing DataMessage:", e );
		}

		return null;
	}

}
