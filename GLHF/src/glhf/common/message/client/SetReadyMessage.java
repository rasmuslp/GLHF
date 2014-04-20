package glhf.common.message.client;

import glhf.common.message.GlhfMessage;
import glhf.common.message.GlhfMessageType;

import java.io.IOException;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

public class SetReadyMessage extends GlhfMessage {

	private final boolean ready;

	public SetReadyMessage( final boolean ready ) {
		super( GlhfMessageType.C_READY );
		this.ready = ready;
	}

	public boolean isReady() {
		return this.ready;
	}

	@Override
	protected void serializePayload( ByteArrayWriter to ) throws IOException {
		to.writeBoolean( this.ready );
	}

	/**
	 * Construct an SetReadyMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed SetReadyMessage.
	 */
	public static SetReadyMessage parse( ByteArrayReader payload ) {
		try {
			boolean ready = payload.readBoolean();
			return new SetReadyMessage( ready );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing SetReadyMessage:", e );
		}

		return null;
	}

}