package glhf.common.message.server;

import glhf.common.message.GlhfMessage;
import glhf.common.message.GlhfMessageType;

import java.io.IOException;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

/**
 * This Message announces the connect or disconnect of a {@link Client}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ConnectionChangeMessage extends GlhfMessage {

	/**
	 * The connection ID of the {@link Client}.
	 */
	private final int id;

	/**
	 * {@code True} iff the {@link Client} connected, false if it disconnected.
	 */
	private final boolean didConnect;

	/**
	 * Announces the connection change for a {@link Client}.
	 * 
	 * @param id
	 *            The ID of the {@link Client} that has changed connection state.
	 * @param didConnect
	 *            {@code True} iff the {@link Client} connected, false if it disconnected.
	 */
	public ConnectionChangeMessage( final int id, final boolean didConnect ) {
		super( GlhfMessageType.S_CONNECTION_CHANGE );
		this.id = id;
		this.didConnect = didConnect;
	}

	/**
	 * Gets the connection ID.
	 * 
	 * @return The connection ID.
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * @return {@code True} iff the {@link Client} connected, false if it disconnected.
	 */
	public boolean didConnect() {
		return this.didConnect;
	}

	@Override
	protected void serializePayload( ByteArrayWriter to ) throws IOException {
		to.writeInt( this.id );
		to.writeBoolean( this.didConnect );
	}

	/**
	 * Construct an ConnectionChangeMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed ConnectionChangeMessage.
	 */
	public static ConnectionChangeMessage parse( ByteArrayReader payload ) {
		try {
			int id = payload.readInt();
			boolean didConnect = payload.readBoolean();
			return new ConnectionChangeMessage( id, didConnect );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing ConnectionChangeMessage:", e );
		}

		return null;
	}

}
