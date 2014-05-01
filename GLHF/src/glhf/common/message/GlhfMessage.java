package glhf.common.message;

import java.io.IOException;

import crossnet.message.crossnet.messages.TieredCrossNetMessage;
import crossnet.util.ByteArrayWriter;

/**
 * Abstract Message that is used internally to maintain state.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public abstract class GlhfMessage extends TieredCrossNetMessage {

	/**
	 * The type of Message.
	 */
	protected final GlhfMessageType glhfMessageType;

	public GlhfMessage( GlhfMessageType glhfMessageType ) {
		this.glhfMessageType = glhfMessageType;
	}

	@Override
	protected void serializeCrossNetPayload( ByteArrayWriter to ) throws IOException {
		to.writeByte( this.glhfMessageType.ordinal() );
		this.serializeGlhfPayload( to );
	}

	/**
	 * Serialises the payload of the GlhfMessage.
	 * 
	 * @param to
	 *            The destination of the serialisation.
	 * @throws IOException
	 *             If a serialisation error occurs.
	 */
	protected abstract void serializeGlhfPayload( ByteArrayWriter to ) throws IOException;

}
